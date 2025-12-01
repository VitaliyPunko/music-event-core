package vpunko.spotify.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vpunko.spotify.core.dto.TicketMasterResponseEvent;
import vpunko.spotify.core.dto.TicketmasterEvent;
import vpunko.spotify.core.dto.UserMessageRequestEvent;
import vpunko.spotify.core.publisher.TicketmasterResponseEventKafkaPublisher;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MusicEventBotSenderService {

    private final MusicEventServiceImpl musicEventService;
    private final TicketmasterResponseEventKafkaPublisher publisher;

    public void sendMessageToMusicBot(UserMessageRequestEvent data) {
        List<TicketmasterEvent> musicEventByArtist = musicEventService.getMusicEventByArtist(data.getMessage());

        TicketMasterResponseEvent event = new TicketMasterResponseEvent();
        event.setEvents(musicEventByArtist);
        event.setChatId(data.getChatId());
        event.setForTest(data.isForTest());

        publisher.sendMessage(event);
    }
}
