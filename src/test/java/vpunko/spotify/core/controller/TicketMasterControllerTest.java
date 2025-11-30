package vpunko.spotify.core.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import vpunko.spotify.core.client.TicketMasterRestClient;
import vpunko.spotify.core.dto.TicketMasterEventClientResponse;
import vpunko.spotify.security.TestConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketMasterController.class)
@Import(TestConfig.class)
class TicketMasterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketMasterRestClient ticketMasterRestClient;

    private TicketMasterEventClientResponse response = new TicketMasterEventClientResponse();

    @Test
    void getEventShouldReturnMockResponse() throws Exception {
        when(ticketMasterRestClient.getEvent(anyString(), any())).thenReturn(response);

        MvcResult result = mockMvc.perform(
                        get("/getEvent")
                                .param("keyWord", "test")
                                .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        assertNotNull(jsonResponse);
        verify(ticketMasterRestClient, times(1)).getEvent(anyString(), any());
    }

    @Test
    void getEventShouldReturn500OnServiceFailure() throws Exception {
        when(ticketMasterRestClient.getEvent(anyString(), any()))
                .thenThrow(new RuntimeException("Service failed"));

        ResultActions result = mockMvc.perform(
                get("/getEvent")
                        .param("keyWord", "test"))
                .andExpect(status().isInternalServerError());

        assertNotNull(result);
    }
}