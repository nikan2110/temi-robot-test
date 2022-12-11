package meuhedet.com.temitestappl.services;

import android.util.Log;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import meuhedet.com.temitestappl.dto.ResponseDto;

public class ChatBotService {

    final static String BID = "168718";
    final static String KEY = "WAivGFqFHfUv9mVb";
    final static String UID = "[uid]";
    private static final String CHAT_BOT_SERVICE = "ChatBotService";
    static RestTemplate restTemplate = new RestTemplate();



    public String translateRequest(String request) {
        Log.i(CHAT_BOT_SERVICE, "Received request for translate: " + request);
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Detection detection = translate.detect(request);
        Translation translation = translate.translate(request, Translate.TranslateOption.sourceLanguage(detection.getLanguage()),
                Translate.TranslateOption.targetLanguage("en"));
        Log.i(CHAT_BOT_SERVICE, "Finished translate");
        return translation.getTranslatedText();
    }

    public String translateResponse(String assistantResponse) {
        Log.i(CHAT_BOT_SERVICE, "Received response for translate: " + assistantResponse);
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Detection detection = translate.detect(assistantResponse);
        Translation translation = translate.translate(assistantResponse,
                Translate.TranslateOption.sourceLanguage(detection.getLanguage()), Translate.TranslateOption.targetLanguage("he"));
        return translation.getTranslatedText();
    }

    public String sendRequestAndReceiveResponse(String message) {
        Log.i(CHAT_BOT_SERVICE, "Received message for bot: " + message);
        String httpUrl = "http://api.brainshop.ai/get";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpUrl).queryParam("bid", BID)
                .queryParam("key", KEY).queryParam("uid", UID).queryParam("msg", message);
        RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.GET, builder.build().toUri());
        ResponseEntity<ResponseDto> responseEntity = restTemplate.exchange(requestEntity, ResponseDto.class);
        String response = responseEntity.getBody().getCnt();
        Log.i(CHAT_BOT_SERVICE, "Bot finished and send response  " + response);
        return response;
    }
}
