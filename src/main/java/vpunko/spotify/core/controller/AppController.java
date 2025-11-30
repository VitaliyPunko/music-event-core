package vpunko.spotify.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vpunko.spotify.core.dto.TicketmasterEvent;
import vpunko.spotify.core.service.MusicEventServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final MusicEventServiceImpl musicEventService;


    @GetMapping("/hello")
    public String hello() {
     return "Hello";
    }

    @GetMapping("/getEventByArtist")
    public List<TicketmasterEvent> getEventByArtist(@RequestParam String artist) {
        return musicEventService.getMusicEventByArtist(artist);
    }
}
