package aj8gh.gameoflife.config;

import aj8gh.gameoflife.apiclient.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiClientConfig {
    @Value("${api.client.scheme}")
    private String scheme;

    @Value("${api.client.host}")
    private String host;

    @Value("${api.client.port}")
    private int port;

    @Value("${api.client.path}")
    private String path;

    @Bean
    public ApiClient apiClient() {
        return new ApiClient(scheme, host, port, path, new RestTemplate());
    }
}
