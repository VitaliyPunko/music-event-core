package vpunko.spotify.core.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vpunko.spotify.core.TestUtils;
import vpunko.spotify.core.dto.TicketMasterEventClientResponse;
import vpunko.spotify.core.dto.TicketmasterEvent;

import java.util.List;

class TicketMasterMapperTest {

    private TicketMasterMapper mapper = new TicketMasterMapper();

    @Test
    void successWithAllFieldsTest() {
        //given
        String jsonFile = "mapper/ticketmastermapper/success/input.json";
        var given = TestUtils.parse(jsonFile, TicketMasterEventClientResponse.class);

        //when
        List<TicketmasterEvent> ticketmasterEventList = mapper.map(given);

        //then
        String jsonExpected = "mapper/ticketmastermapper/success/expected.json";
        var expected = TestUtils.parse(jsonExpected, TicketmasterEvent.class);
        List<TicketmasterEvent> expectedList = List.of(expected);

        Assertions.assertEquals(ticketmasterEventList.size(), expectedList.size());
        Assertions.assertEquals(expectedList, ticketmasterEventList);
    }

    @Test
    void successNotAllFieldsPresentFieldsTest() {
        //given
        String jsonFile = "mapper/ticketmastermapper/notAllFieldsPresentSuccess/input.json";
        var given = TestUtils.parse(jsonFile, TicketMasterEventClientResponse.class);

        //when
        List<TicketmasterEvent> ticketmasterEventList = mapper.map(given);

        //then
        String jsonExpected = "mapper/ticketmastermapper/notAllFieldsPresentSuccess/expected.json";
        var expected = TestUtils.parse(jsonExpected, TicketmasterEvent.class);
        List<TicketmasterEvent> expectedList = List.of(expected);

        Assertions.assertEquals(ticketmasterEventList.size(), expectedList.size());
        Assertions.assertEquals(expectedList, ticketmasterEventList);
    }

}