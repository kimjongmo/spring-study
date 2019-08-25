package com.spring.core.di.factoryMethod;

public class SearchClientFactoryBuilder {

    private SearchClientFactory searchClientFactory;
    private String server;
    private Integer port;
    private String contentType;
    private String encoding;

    public SearchClientFactoryBuilder(){
        searchClientFactory = new SearchClientFactory();
    }

    public SearchClientFactoryBuilder server(String server) {
        searchClientFactory.setServer(server);
        return this;
    }

    public SearchClientFactoryBuilder port(Integer port) {
        searchClientFactory.setPort(port);
        return this;
    }

    public SearchClientFactoryBuilder contentType(String contentType) {
        searchClientFactory.setContentType(contentType);
        return this;
    }

    public SearchClientFactoryBuilder encoding(String encoding) {
        searchClientFactory.setEncoding(encoding);
        return this;
    }

    public SearchClientFactory build() {
        return searchClientFactory;
    }


}
