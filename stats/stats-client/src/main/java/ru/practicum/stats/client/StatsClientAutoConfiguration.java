package ru.practicum.stats.client;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@AutoConfiguration
@Import(RestTemplateConfig.class)
public class StatsClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public StatsClient statsClient(RestTemplate restTemplate,
                                   org.springframework.core.env.Environment environment) {
        String serverUrl = environment.getProperty("stats-server.url", "http://localhost:9090");
        return new StatsClientImpl(restTemplate, serverUrl);
    }
}