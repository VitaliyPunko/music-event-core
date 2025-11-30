package vpunko.spotify.core.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vpunko.spotify.core.TestUtils;
import vpunko.spotify.core.dto.TicketMasterEventResponse;
import vpunko.spotify.core.dto.TicketmasterResponseEvent;

import java.util.List;

class TicketMasterMapperTest {

    private TicketMasterMapper mapper = new TicketMasterMapper();

    @Test
    void successWithAllFieldsTest() {
        //given
        String jsonFile = "mapper/ticketmastermapper/success/input.json";
        var given = TestUtils.parse(jsonFile, TicketMasterEventResponse.class);

        //when
        List<TicketmasterResponseEvent> ticketmasterResponseEventList = mapper.map(given);

        //then
        String jsonExpected = "mapper/ticketmastermapper/success/expected.json";
        var expected = TestUtils.parse(jsonExpected, TicketmasterResponseEvent.class);
        List<TicketmasterResponseEvent> expectedList = List.of(expected);

        Assertions.assertEquals(ticketmasterResponseEventList.size(), expectedList.size());
        Assertions.assertEquals(expectedList, ticketmasterResponseEventList);
    }

    @Test
    void successNotAllFieldsPresentFieldsTest() {
        //given
        String jsonFile = "mapper/ticketmastermapper/notAllFieldsPresentSuccess/input.json";
        var given = TestUtils.parse(jsonFile, TicketMasterEventResponse.class);

        //when
        List<TicketmasterResponseEvent> ticketmasterResponseEventList = mapper.map(given);

        //then
        String jsonExpected = "mapper/ticketmastermapper/notAllFieldsPresentSuccess/expected.json";
        var expected = TestUtils.parse(jsonExpected, TicketmasterResponseEvent.class);
        List<TicketmasterResponseEvent> expectedList = List.of(expected);

        Assertions.assertEquals(ticketmasterResponseEventList.size(), expectedList.size());
        Assertions.assertEquals(expectedList, ticketmasterResponseEventList);
    }

}