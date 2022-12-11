package meuhedet.com.temitestappl.services;

import android.util.Log;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import meuhedet.com.temitestappl.dto.ArticleDto;
import meuhedet.com.temitestappl.dto.ResponseDtoNews;
import meuhedet.com.temitestappl.utils.Constants;


public class NewsService {

    static RestTemplate restTemplate = new RestTemplate();

    public List<ArticleDto> returnArticlesByTheme(String theme) {
        Log.i("NewsService", "Received theme: " + theme);
        String httpUrl = Constants.NEWS_SERVICE + theme;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl);
        RequestEntity<String> requestEntity = new RequestEntity(HttpMethod.GET, builder.build().toUri());
        ResponseEntity<ResponseDtoNews> responseEntity = restTemplate.exchange(requestEntity, ResponseDtoNews.class);
        return responseEntity.getBody().getArticles();
    }

}
