package trap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import trap.client.TrapDataClient;

@Configuration
class HttpClientConfig {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    TrapDataClient trapDataClient(RestClient.Builder builder, TrapProperties properties) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) properties.http().connectTimeout().toMillis());
        requestFactory.setReadTimeout((int) properties.http().readTimeout().toMillis());

        var restClient = builder.requestFactory(requestFactory).build();
        var adapter = RestClientAdapter.create(restClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(TrapDataClient.class);
    }
}
