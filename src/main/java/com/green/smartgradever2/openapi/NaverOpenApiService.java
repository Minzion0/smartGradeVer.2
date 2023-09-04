package com.green.smartgradever2.openapi;

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

import java.net.URLEncoder;
//todo 해보자고
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class NaverOpenApiService {
//
//    private final String clientId;
//    private final String secret;
//    private WebClient webClient;
//
//    @Autowired
//    public NaverOpenApiService(@Value("${open-api.client-id}") String clientId, @Value("${open-api.client-secret}") String secret) {
//        this.clientId = clientId;
//        this.secret = secret;
//        TcpClient tcpClient= TcpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
//                .doOnConnected(conn->{
//                    conn.addHandlerLast(new ReadTimeoutHandler(5000));
//                    conn.addHandlerLast(new WriteTimeoutHandler(5000));
//                });
//        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
//                .codecs(config-> config.defaultCodecs().maxInMemorySize(-1)).build();
//
//
//        this.webClient = WebClient.builder()
//                .exchangeStrategies(exchangeStrategies)
//                .baseUrl("https://openapi.naver.com/v1/search/book.json")
//                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//    }
//
//
//    public void test(String isbn){
//        webClient.get().uri(uriBuilder ->)
//
//
//    }
//}
