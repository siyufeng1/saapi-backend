package com.siyufeng.saapiclientsdk;

import com.siyufeng.saapiclientsdk.client.SaApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 司雨枫
 */
@Configuration
@ConfigurationProperties("saapi-client")
@Data
@ComponentScan
public class SaApiClientConfig {
    private String accessKey;

    private String secretKey;

    @Bean
    public SaApiClient saApiClient() {
        return new SaApiClient(accessKey, secretKey);
    }
}
