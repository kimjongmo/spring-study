package com.spring.core.di.factoryMethod;

import org.springframework.beans.factory.FactoryBean;

public class SearchClientFactoryBean implements FactoryBean<SearchClientFactory> {

    private SearchClientFactory searchClientFactory;

    private String server;
    private Integer port;
    private String contentType;
    private String encoding = "utf-8";

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public SearchClientFactory getObject() throws Exception {
        if (searchClientFactory != null)
            return searchClientFactory;

        SearchClientFactoryBuilder builder = new SearchClientFactoryBuilder();
        builder.server(server)
                .port(port)
                .contentType(contentType)
                .encoding(encoding);

        searchClientFactory = builder.build();
        searchClientFactory.init();
        return searchClientFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return SearchClientFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
