package vpunko.spotify.core.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import vpunko.spotify.core.dto.TicketMasterEventClientResponse;
import vpunko.spotify.core.exception.TicketMasterClientException;

import java.util.Optional;

/**
 * <a href="https://developer.ticketmaster.com/products-and-docs/apis/discovery-api/v2/">Discovery ticketmaster API</a>
 */
@Component
public class TicketMasterRestClient {

    private final String apiKey;
    private final Integer pageSize;
    private final RestClient restClient;

    public TicketMasterRestClient(RestClient.Builder restClient,
                                  @Value("${app.credentials.ticketmaster.apikey}")
                                  String apiKey,
                                  @Value("${app.urls.ticketmaster}")
                                  String baseUrl,
                                  @Value("${telegram.ticketmaster_page_size}")
                                  Integer pageSize) {
        this.apiKey = apiKey;
        this.pageSize = pageSize;
        this.restClient = restClient.baseUrl(baseUrl).build();
    }

    public TicketMasterEventClientResponse getEvent(String keyWord, String startDate) {
        TicketMasterEventClientResponse body = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/events")
                        .queryParam("keyword", keyWord)
                        .queryParam("size", pageSize)
                        .queryParam("apikey", apiKey)
                        .queryParamIfPresent("startDateTime", Optional.ofNullable(startDate))
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new TicketMasterClientException(
                            response.getStatusCode(),
                            response.getHeaders(),
                            response.getBody().toString());
                })
                .body(TicketMasterEventClientResponse.class);

        if (body == null) {
            throw new TicketMasterClientException("TicketMasterClient return null body");
        }

        return body;
    }

}
