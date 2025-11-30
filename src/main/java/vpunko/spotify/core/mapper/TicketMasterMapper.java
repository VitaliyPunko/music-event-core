package vpunko.spotify.core.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vpunko.spotify.core.constant.TicketSaleEnum;
import vpunko.spotify.core.dto.TicketMasterEventResponse;
import vpunko.spotify.core.dto.TicketmasterResponseEvent;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static vpunko.spotify.core.constant.TicketSaleEnum.*;

@Slf4j
@Component
public class TicketMasterMapper {


    public List<TicketmasterResponseEvent> map(TicketMasterEventResponse response) {
        List<TicketMasterEventResponse.Event> events = response.get_embedded().getEvents();

        List<TicketmasterResponseEvent> ticketmasterResponseEventList = new ArrayList<>();


        for (TicketMasterEventResponse.Event event : events) {
            var musicEventDto = new TicketmasterResponseEvent();
            //name
            musicEventDto.setName(event.getName());

            //dates
            TicketMasterEventResponse.Event.Dates dates = event.getDates();
            if (dates != null) {
                String eventDateTime = getDates(dates);
                if (eventDateTime != null) {
                    musicEventDto.setStartTime(OffsetDateTime.parse(eventDateTime));
                }
                String timeZone = dates.getTimezone();
                musicEventDto.setTimeZone(timeZone);

                TicketMasterEventResponse.Event.Dates.Status status = dates.getStatus();
                String code = status.getCode();
                musicEventDto.setTicketSaleStatus(getTicketStatus(code));
            }

            //price
            List<TicketMasterEventResponse.Event.PriceRange> priceRanges = event.getPriceRanges();
            if (priceRanges != null && !priceRanges.isEmpty()) {
                var priceRange = priceRanges.get(0);
                String currency = priceRange.getCurrency();
                Double min = priceRange.getMin();
                double max = priceRange.getMax();

                musicEventDto.setCurrency(currency);
                musicEventDto.setPrice(min != null ? min : max);
            }

            //sale
            Optional<TicketMasterEventResponse.Event.Sales.PublicSale> publicSale =
                    Optional.ofNullable(event.getSales())
                            .map(s -> s.getPublicSale())
                            .filter(Objects::nonNull);

            if (publicSale.isPresent()) {
                musicEventDto.setSaleStartDateTime(publicSale.get().getStartDateTime());
                musicEventDto.setSaleEndDateTime(publicSale.get().getEndDateTime());
            }

            //description
            musicEventDto.setDescription(event.getPleaseNote());
            //image
            List<TicketMasterEventResponse.Event.Image> images = event.getImages();
            if (images != null && !images.isEmpty()) {
                musicEventDto.setImageUrl(images.get(0).getUrl());
            }

            //venue
            var embedded = event.get_embedded();
            if (embedded != null) {
                getVenue(embedded, musicEventDto);
            }

            ticketmasterResponseEventList.add(musicEventDto);
        }

        return ticketmasterResponseEventList;
    }


    private void getVenue(TicketMasterEventResponse.Event.EmbeddedVenuesAttractions embedded, TicketmasterResponseEvent ticketmasterResponseEvent) {
        List<TicketMasterEventResponse.Event.EmbeddedVenuesAttractions.Venue> venues = embedded.getVenues();
        if (venues != null && !venues.isEmpty()) {
            var venue = venues.get(0);

            TicketmasterResponseEvent.MusicEventVenue musicEventVenue = new TicketmasterResponseEvent.MusicEventVenue();
            musicEventVenue.setName(venue.getName());
            musicEventVenue.setUrl(venue.getUrl());
            var country = venue.getCountry();
            if (country != null) {
                musicEventVenue.setCountry(country.getName());
            }
            var city = venue.getCity();
            if (city != null) {
                musicEventVenue.setCity(city.getName());
            }
            var address = venue.getAddress();
            if (address != null) {
                musicEventVenue.setAddress(address.getLine1());
            }
            ticketmasterResponseEvent.setVenue(musicEventVenue);
        }

    }

    private String getDates(TicketMasterEventResponse.Event.Dates dates) {
        TicketMasterEventResponse.Event.Dates.Start start = dates.getStart();
        if (start != null) {
            String dateTime = start.getDateTime();
            if (dateTime == null) {
                String localDate = start.getLocalDate();
                String localTime = start.getLocalTime();
                return localDate.concat("T").concat(localTime);
            }
            return dateTime;
        }
        return null;
    }

    private TicketSaleEnum getTicketStatus(String code) {
        if (code == null) {
            return null;
        }
        TicketSaleEnum ticketStatus = switch (code) {
            case "onsale" -> ON_SALE;
            case "offsale" -> OFF_SALE;
            case "canceled" -> CANCELLED;
            case "postponed" -> POSTPONED;
            case "rescheduled" -> RESCHEDULED;
            default -> UNKNOWN;
        };

        if (ticketStatus == UNKNOWN)
            log.error("Unexpected ticket status from ticketmaster");

        return ticketStatus;
    }
}
