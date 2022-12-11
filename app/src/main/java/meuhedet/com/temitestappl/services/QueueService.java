package meuhedet.com.temitestappl.services;


import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import meuhedet.com.temitestappl.dto.RecordDto;
import meuhedet.com.temitestappl.utils.Constants;

public class QueueService {

    static RestTemplate restTemplate = new RestTemplate();

    public String orderQueue(String request) {
        String httpUrl = Constants.QUEUE_SERVICE + request;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        RequestEntity<String> requestEntity = new RequestEntity(HttpMethod.GET, builder.build().toUri());
        ResponseEntity<RecordDto> responseEntity = restTemplate.exchange(requestEntity, RecordDto.class);
        return responseEntity.getBody().getNumberOfQueue();
    }
}
