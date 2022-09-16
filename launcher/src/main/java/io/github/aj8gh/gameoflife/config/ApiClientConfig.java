package io.github.aj8gh.gameoflife.config;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import io.github.aj8gh.gameoflife.api.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiClientConfig {

  @Value("${api.client.baseUrl}")
  private String baseUrl;

  @Value("${api.client.path}")
  private String path;

  @Bean
  public ApiClient apiClient() {
    return new ApiClient(apiClientRestTemplate(), path);
  }

  @Bean
  public RestTemplate apiClientRestTemplate() {
    return new RestTemplateBuilder()
        .rootUri(baseUrl)
        .defaultHeader(CONTENT_TYPE, APPLICATION_JSON.toString())
        .defaultHeader(ACCEPT, APPLICATION_JSON.toString())
        .build();
  }
}
