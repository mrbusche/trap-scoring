package trap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import trap.client.TrapDataClient;

@SpringBootApplication
class Application {
    static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    TrapDataClient trapDataClient(RestClient.Builder builder) {
        // 1. Configure the underlying RestClient (timeouts, etc.)
        RestClient restClient = builder
                .requestFactory(new SimpleClientHttpRequestFactory())
                .build();

        // 2. Create the Proxy Factory using the RestClient
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        // 3. Generate the client interface implementation
        return factory.createClient(TrapDataClient.class);
    }
}
