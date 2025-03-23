package com.bubble.buubleforprofessor.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // HTTP 클라이언트 설정: 리다이렉션 허용
        HttpClient httpClient = HttpClient.create()
                .followRedirect(true);

        // WebClient 빌더로 구성
        WebClient webClient = WebClient.builder()
                .baseUrl("http://safemap.go.kr/openApiService")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> {
                    configurer.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder());
                    configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024); // 버퍼 크기 256-> 10MB로 증가
                })
                .build();

        return webClient;
    }
}