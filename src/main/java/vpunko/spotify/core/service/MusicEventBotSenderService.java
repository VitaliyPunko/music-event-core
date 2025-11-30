package vpunko.spotify.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vpunko.spotify.core.dto.TicketmasterResponseEvent;
import vpunko.spotify.core.dto.UserMessageRequestEvent;
import vpunko.spotify.core.publisher.TicketmasterResponseEventKafkaPublisher;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MusicEventBotSenderService {

    private final MusicEventServiceImpl musicEventService;
    private final TicketmasterResponseEventKafkaPublisher publisher;

    public void sendMessageToMusicBot(UserMessageRequestEvent data) {
        List<TicketmasterResponseEvent> musicEventByArtist = musicEventService.getMusicEventByArtist(data.getMessage());

        for (TicketmasterResponseEvent ticketmasterResponseEvent : musicEventByArtist) {
            publisher.sendMessage(ticketmasterResponseEvent, data.getChatId());
        }

    }
}
