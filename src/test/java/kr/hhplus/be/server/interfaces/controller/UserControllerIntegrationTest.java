package kr.hhplus.be.server.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.interfaces.user.PointChargeRequest;
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
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 포인트_충전시_200_응답이_반환된다() throws Exception {
        // given
        PointChargeRequest request = new PointChargeRequest(BigDecimal.valueOf(10000));

        // when & then
        mockMvc.perform(put("/api/v1/users/1/point")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").exists())
                .andDo(print());
    }

    @Test
    void 포인트_조회시_200_응답이_반환된다() throws Exception {
        mockMvc.perform(get("/api/v1/users/1/point"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").exists())
                .andDo(print());
    }

    @Test
    void 쿠폰_목록_조회시_200_응답이_반환된다() throws Exception {
        mockMvc.perform(get("/api/v1/users/1/coupons"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}