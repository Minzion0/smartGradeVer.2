package com.green.smartgradever2.openapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//todo 해보자고
@Slf4j
@Service
public class NaverOpenApiService {

    //애플리케이션 클라이언트 시크릿
    private final WebClient webClient;

    @Autowired
    public NaverOpenApiService(@Value("${open-api.client-id}") String clientId,@Value("${open-api.client-secret}") String clientSecret) {

        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(conn -> {
                    conn.addHandlerLast(new ReadTimeoutHandler(5000));
                    conn.addHandlerLast(new WriteTimeoutHandler(5000));
                });

        this.webClient = WebClient.builder()
                .baseUrl("https://openapi.naver.com")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-Naver-Client-Id", clientId) // 네이버 클라이언트 아이디
                .defaultHeader("X-Naver-Client-Secret", clientSecret) // 네이버 클라이언트 시크릿
                .build();
    }



    /**naver open api**/
    public BookVo getTimetable(String isbn) {
        // 네이버 책 검색 API 호출 및 응답 받기
        String json = webClient.get()
                .uri("/v1/search/book_adv.json?d_isbn={isbn}", isbn)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // JSON 파싱 및 가장 중요한 설정
        ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        BookVo vo = null;

        try {

            JsonNode jsonNode = om.readTree(json);
            vo = om.convertValue(jsonNode.at("/items/0"), new TypeReference<BookVo>() {});

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vo;
    }
}