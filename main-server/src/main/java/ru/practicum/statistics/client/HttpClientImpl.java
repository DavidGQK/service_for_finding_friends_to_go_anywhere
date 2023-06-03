package ru.practicum.statistics.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.statistics.model.EndpointHit;
import ru.practicum.statistics.model.ViewStats;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utilities.DateTime.dateTimeToString;

@Service
public class HttpClientImpl implements HttpClient {
    private final RestTemplate rest;
    private final String url;

    private static final String APP_NAME = "service_for_finding_friends_to_go_anywhere";
    private static final String API_HIT = "/hit";
    private static final String API_STATS = "/stats";

    public HttpClientImpl(@Value("${stats-server.url}") String url, RestTemplateBuilder builder) {
        this.rest = builder.build();
        this.url = url;
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Override
    public HttpStatus addStatistics(String uri, String ip) {
        EndpointHit body = new EndpointHit(null, APP_NAME, uri, ip, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<EndpointHit> entity = new HttpEntity<>(body, headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url + API_HIT);
        ResponseEntity<Object> response = rest.exchange(
                uriBuilder.build().encode().toUri(),
                HttpMethod.POST,
                entity,
                Object.class);

        return response.getStatusCode();
    }

    @Override
    public ResponseEntity<List<ViewStats>> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris,
                                                         Boolean unique) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url + API_STATS);
        uriBuilder.queryParam("start", dateTimeToString(start));
        uriBuilder.queryParam("end", dateTimeToString(end));
        uriBuilder.queryParam("unique", unique);
        for (String s : uris) {
            uriBuilder.queryParam("uris", s);
        }
        URI uri = uriBuilder.build().encode().toUri();
        return rest.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<ViewStats>>() {
        });
    }
}