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

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//todo 해보자고
//@Slf4j
//@Service
//public class NaverOpenApiService {
//
//    private final   String clientId ; //애플리케이션 클라이언트 아이디
//   private final String clientSecret ; //애플리케이션 클라이언트 시크릿
//
//    @Autowired
//    public NaverOpenApiService(@Value("${open-api.client-id}") String clientId,@Value("${open-api.client-secret}") String clientSecret) {
//        this.clientId = clientId;
//        this.clientSecret = clientSecret;
//    }
//
//    public void openApi(String isbn){
//    String text = null;
//
//    try {
//        text = URLEncoder.encode(isbn, StandardCharsets.UTF_8);
//    } catch (Exception e) {
//        throw new RuntimeException("검색어 인코딩 실패",e);
//    }
//
//
//    String apiURL = "https://openapi.naver.com/v1/search/book.json" + text;    // JSON 결과
//
//
//
//    Map<String, String> requestHeaders = new HashMap<>();
//    requestHeaders.put("X-Naver-Client-Id", clientId);
//    requestHeaders.put("X-Naver-Client-Secret", clientSecret);
//    String responseBody = get(apiURL,requestHeaders);
//
//
//    System.out.println(responseBody);
//}
//
//
//    private static String get(String apiUrl, Map<String, String> requestHeaders){
//        HttpURLConnection con = connect(apiUrl);
//        try {
//            con.setRequestMethod("GET");
//            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
//                con.setRequestProperty(header.getKey(), header.getValue());
//            }
//
//
//            int responseCode = con.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
//                return readBody(con.getInputStream());
//            } else { // 오류 발생
//                return readBody(con.getErrorStream());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("API 요청과 응답 실패", e);
//        } finally {
//            con.disconnect();
//        }
//    }
//
//
//    private static HttpURLConnection connect(String apiUrl){
//        try {
//            URL url = new URL(apiUrl);
//            return (HttpURLConnection)url.openConnection();
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
//        } catch (IOException e) {
//            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
//        }
//    }
//
//
//    private static String readBody(InputStream body){
//        InputStreamReader streamReader = new InputStreamReader(body);
//
//
//        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
//            StringBuilder responseBody = new StringBuilder();
//
//
//            String line;
//            while ((line = lineReader.readLine()) != null) {
//                responseBody.append(line);
//            }
//
//
//            return responseBody.toString();
//        } catch (IOException e) {
//            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
//        }
//}
//
//}