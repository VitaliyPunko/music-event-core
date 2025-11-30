package vpunko.spotify.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
public class TicketMasterEventClientResponse {

    private Embedded _embedded;
    private Links _links;
    private Page page;

    @Data
    public static class Embedded {
        private List<Event> events;
    }

    @Data
    public static class Event {
        private String name;
        private String type;
        private String id;
        private boolean test;
        private String url;
        private String locale;
        private List<Image> images;
        private Sales sales;
        private Dates dates;
        private List<Classification> classifications;
        @JsonIgnore
        private Object promoter;
        @JsonIgnore
        private List<Object> promoters;
        private List<PriceRange> priceRanges;
        private Ticketing ticketing;
        private String info;
        private String pleaseNote;
        private Links _links;
        private EmbeddedVenuesAttractions _embedded;

        @Data
        public static class Image {
            private String ratio;
            private String url;
            private int width;
            private int height;
            private boolean fallback;
        }

        @Data
        public static class Sales {
            @JsonProperty("public") // Map JSON field "public" to this Java field
            private PublicSale publicSale;

            @Data
            public static class PublicSale {
                private String startDateTime;
                private boolean startTBD;
                private boolean startTBA;
                private String endDateTime;
            }
        }

        @Data
        public static class Dates {
            @JsonIgnore
            private Object access;
            @JsonIgnore
            private Object end;
            private Start start;
            private String timezone;
            private Status status;
            private boolean spanMultipleDays;

            @Data
            public static class Start {
                private String localDate;
                private String localTime;
                private String dateTime;
                private boolean dateTBD;
                private boolean dateTBA;
                private boolean timeTBA;
                private boolean noSpecificTime;
            }

            @Data
            public static class Status {
                private String code;
            }
        }

        @Data
        public static class Classification {
            private boolean primary;
            private Segment segment;
            private Genre genre;
            private SubGenre subGenre;
            private boolean family;
            @JsonIgnore
            private Object type;
            @JsonIgnore
            private Object subType;

            @Data
            public static class Segment {
                private String id;
                private String name;
            }

            @Data
            public static class Genre {
                private String id;
                private String name;
            }

            @Getter
            @Setter
            public static class SubGenre {
                private String id;
                private String name;
            }
        }

        @Data
        public static class PriceRange {
            private String type;
            private String currency;
            private double min;
            private double max;
        }

        @Data
        public static class Ticketing {
            private SafeTix safeTix;
            private String id;

            @Data
            public static class SafeTix {
                private boolean enabled;
            }
        }

        @Data
        public static class Links {
            private Self self;
            private List<AttractionLink> attractions;
            private List<VenueLink> venues;

            @Data
            public static class Self {
                private String href;
            }

            @Data
            public static class AttractionLink {
                private String href;
            }

            @Data
            public static class VenueLink {
                private String href;
            }
        }

        @Data
        public static class EmbeddedVenuesAttractions {
            private List<Venue> venues;
            private List<Attraction> attractions;

            @Data
            public static class Venue {
                private String name;
                private String type;
                private String id;
                private boolean test;
                private String url;
                private String locale;
                @JsonIgnore
                private List<Image> images;
                private String postalCode;
                private String timezone;
                private City city;
                private Country country;
                private Address address;
                private Location location;
                @JsonIgnore
                private UpcomingEvents upcomingEvents;
                private Links _links;
                @JsonIgnore
                private Object state;

                @Data
                public static class City {
                    private String name;
                }

                @Data
                public static class Country {
                    private String name;
                    private String countryCode;
                }

                @Data
                public static class Address {
                    private String line1;
                }

                @Data
                public static class Location {
                    private String longitude;
                    private String latitude;
                }
            }

            @Data
            public static class Attraction {
                private String name;
                private String type;
                private String id;
                private boolean test;
                private String url;
                private String locale;
                private Map<String, List<ExternalLink>> externalLinks;
                private List<Image> images;
                private List<Classification> classifications;
                private UpcomingEvents upcomingEvents;
                private Links _links;

                @Data
                public static class ExternalLink {
                    private String url;
                }
            }
        }
    }

    @Data
    public static class UpcomingEvents {
        private int mfxNl;
        private int _total;
        private int _filtered;
    }

    @Data
    public static class Links {
        private Link first;
        private Link self;
        private Link next;
        private Link last;

        @Data
        public static class Link {
            private String href;
        }
    }

    @Data
    public static class Page {
        private int size;
        private int totalElements;
        private int totalPages;
        private int number;
    }
}