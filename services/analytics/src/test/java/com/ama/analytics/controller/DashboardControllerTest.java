package com.ama.analytics.controller;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.response.DashboardSummaryResponse;
import com.ama.analytics.service.DashboardService;
import com.ama.common.dto.DateRangeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DashboardService dashboardService;

    @Test
    void shouldReturnDashboardSummary() throws Exception {
        when(dashboardService.getSummary(any())).thenReturn(new DashboardSummaryResponse(
                new DashboardSummaryResponse.SummaryMetrics(100, 10, 20, 5, 2, 50, 10, 12),
                new DashboardSummaryResponse.PeriodDelta(0, 0, 0)
        ));
        AnalyticsFilterRequest request = new AnalyticsFilterRequest();
        request.setTenantId(1001L);
        request.setDateRange(new DateRangeDto(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31)));

        mockMvc.perform(post("/api/analytics/v1/dashboard/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
