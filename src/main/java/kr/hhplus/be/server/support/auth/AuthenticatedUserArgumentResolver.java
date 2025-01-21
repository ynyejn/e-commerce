package kr.hhplus.be.server.support.auth;

import jakarta.servlet.http.HttpServletRequest;
import kr.hhplus.be.server.domain.user.IUserRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final IUserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUser.class)
                && parameter.getParameterType().equals(User.class);
    }

    @Override
    public User resolveArgument(MethodParameter parameter,
                                ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) {
        String userId = findUserIdFromRequest(webRequest);

        if (userId == null) {
            throw new ApiException(ApiErrorCode.UNAUTHORIZED);
        }

        return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ApiException(ApiErrorCode.NOT_FOUND));
    }


    private String findUserIdFromRequest(NativeWebRequest webRequest) {
        // Header
        String userId = webRequest.getHeader("USER-ID");
        if (userId != null) {
            return userId;
        }
        // PathVariable
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Map<String, String> pathVariables =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null && pathVariables.containsKey("userId")) {
            return pathVariables.get("userId");
        }

        // RequestParam
        return webRequest.getParameter("userId");
    }
}