package kr.hhplus.be.server.support;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class LoggingFilterTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void 요청_응답_로깅이_정상적으로_동작한다() throws Exception {
        // given
        String requestBody = """
                {
                    "amount": 10000
                }
                """;

        // when & then
        mockMvc.perform(put("/api/v1/points")
                        .header("USER-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void GET_요청은_request_body가_로깅되지_않는다() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/points")
                        .header("USER-ID", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}