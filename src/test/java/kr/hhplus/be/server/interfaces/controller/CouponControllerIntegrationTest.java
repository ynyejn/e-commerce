package kr.hhplus.be.server.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class CouponControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 테스트에서 사용할 인증 헤더 상수
    private static final String X_USER_ID = "X-USER-ID";
    private static final String TEST_USER_ID = "1";

    @Test
    void 인증된_사용자의_쿠폰_발급시_200_응답이_반환된다() throws Exception {
        // given
        Long couponId = 1L;

        // when & then
        mockMvc.perform(post("/api/v1/coupons/{couponId}/issue", couponId)
                        .header(X_USER_ID, TEST_USER_ID)  // 인증 헤더 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 내_쿠폰_목록_조회시_200_응답이_반환된다() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/coupons/my")
                        .header(X_USER_ID, TEST_USER_ID))
                .andExpect(status().isOk())
                .andDo(print());
    }
}