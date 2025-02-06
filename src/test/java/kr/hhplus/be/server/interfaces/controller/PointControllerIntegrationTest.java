package kr.hhplus.be.server.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.interfaces.point.PointChargeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class PointControllerIntegrationTest {

    private static final String USER_ID = "USER-ID";
    private static final String TEST_USER_ID = "1";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 인증된_사용자의_포인트_충전시_200_응답이_반환된다() throws Exception {
        // given
        PointChargeRequest request = new PointChargeRequest(BigDecimal.valueOf(10000));

        // when & then
        mockMvc.perform(put("/api/v1/points")
                        .header(USER_ID, TEST_USER_ID)  // 인증 헤더 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").exists())
                .andDo(print());
    }

    @Test
    void 인증되지_않은_사용자의_포인트_충전_요청시_401_응답이_반환된다() throws Exception {
        // given
        PointChargeRequest request = new PointChargeRequest(BigDecimal.valueOf(10000));

        // when & then
        mockMvc.perform(put("/api/v1/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void 인증된_사용자의_포인트_조회시_200_응답이_반환된다() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/points")
                        .header(USER_ID, TEST_USER_ID))  // 인증 헤더 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").exists())
                .andDo(print());
    }

    @Test
    void 인증되지_않은_사용자의_포인트_조회_요청시_401_응답이_반환된다() throws Exception {
        // given
        PointChargeRequest request = new PointChargeRequest(BigDecimal.valueOf(10000));

        // when & then
        mockMvc.perform(put("/api/v1/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}