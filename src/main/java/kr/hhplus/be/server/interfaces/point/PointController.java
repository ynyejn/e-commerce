package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.domain.point.PointInfo;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.auth.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController implements PointControllerDocs {
    private final PointService pointService;

    @PutMapping
    public ResponseEntity<PointResponse> chargePoint(
            @AuthenticatedUser User user,
            @RequestBody PointChargeRequest request
    ) {
        PointInfo response = pointService.chargePoint(user, request.toCommand());
        return ResponseEntity.ok(PointResponse.from(response));
    }

    @GetMapping
    public ResponseEntity<PointResponse> getPoint(@AuthenticatedUser User user) {
        PointResponse response = PointResponse.from(pointService.getPoint(user));
        return ResponseEntity.ok(response);
    }
}
