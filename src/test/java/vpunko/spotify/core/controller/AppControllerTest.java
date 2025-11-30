package vpunko.spotify.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import vpunko.spotify.core.dto.TicketmasterEvent;
import vpunko.spotify.core.service.MusicEventServiceImpl;
import vpunko.spotify.security.TestConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppController.class)
@Import(TestConfig.class)
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MusicEventServiceImpl musicEventService;

    private List<TicketmasterEvent> musicEventsResponse = List.of();

    @Test
    void getEventByArtistShouldReturnMockResponse() throws Exception {
        when(musicEventService.getMusicEventByArtist(anyString())).thenReturn(musicEventsResponse);

        MvcResult result = mockMvc.perform(
                        get("/getEventByArtist")
                                .param("artist", "test")
                                .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<TicketmasterEvent> actualResponse = new ObjectMapper().readValue(jsonResponse, List.class);

        assertNotNull(actualResponse);
        verify(musicEventService, times(1)).getMusicEventByArtist(any());
    }

    @Test
    void getEventByArtistShouldReturn500OnServiceFailure() throws Exception {
        when(musicEventService.getMusicEventByArtist(anyString()))
                .thenThrow(new RuntimeException("Service failed"));

        ResultActions result = mockMvc.perform(
                get("/getEventByArtist")
                        .param("artist", "test"))
                .andExpect(status().isInternalServerError());

        assertNotNull(result);
    }
}