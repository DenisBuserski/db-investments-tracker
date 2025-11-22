package com.investments.tracker.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.investments.tracker.service.report.WeeklyProductPositionDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class ReportGenerationIT {
    private static final String WEEKLY_REPORT_JSON = "src/test/resources/json/weekly-report-response.json";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Value("${application.test.report.prepare-url}")
    private String prepareUrl;

    @Value("${application.test.report.generate-url}")
    private String generateUrl;


    @Test
    void test() throws Exception {
        String expectedResponse = Files.readString(Paths.get(WEEKLY_REPORT_JSON));

        String actualResponseAfterPrepareCall = mockMvc
                .perform(post(prepareUrl)
                        .param("lastDayOfTheWeek", "2024-12-22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<WeeklyProductPositionDTO> positions = mapper
                .readValue(actualResponseAfterPrepareCall, new TypeReference<List<WeeklyProductPositionDTO>>() {});

        String finalResponse = mockMvc
                .perform(post(generateUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(positions))  // send updated JSON
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals(expectedResponse, finalResponse, false);

    }
}
