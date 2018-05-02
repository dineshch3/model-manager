package com.predera.dmml.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataServiceConfiguration {
	
	private String hostname;
	private int port;
	private String scheme;

	public DataServiceConfiguration(ApplicationProperties applicationProperties) {
		this.hostname = applicationProperties.getDataService().getElasticsearch().getHostname();
		this.port = applicationProperties.getDataService().getElasticsearch().getPort();
		this.scheme = applicationProperties.getDataService().getElasticsearch().getScheme();
	}

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(hostname, port, scheme)));
    }

}
