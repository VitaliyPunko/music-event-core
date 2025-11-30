package vpunko.spotify.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vpunko.spotify.core.client.TicketMasterRestClient;
import vpunko.spotify.core.dto.TicketMasterEventClientResponse;
import vpunko.spotify.core.dto.TicketmasterEvent;
import vpunko.spotify.core.mapper.TicketMasterMapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusicEventServiceImpl {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private final TicketMasterMapper mapper;
    private final TicketMasterRestClient ticketMasterRestClient;

    public List<TicketmasterEvent> getMusicEventByArtist(String artist) {
        //get big response. Get only some simple fields
        String offsetDateTimeRequiredFormat = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.UTC).format(formatter);

        TicketMasterEventClientResponse response = ticketMasterRestClient.getEvent(artist, offsetDateTimeRequiredFormat);
        if (response == null || response.get_embedded() == null) {
            return List.of();
        }
        return mapper.map(response);
    }

}
