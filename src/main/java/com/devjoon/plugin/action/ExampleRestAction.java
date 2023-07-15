package com.devjoon.plugin.action;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.rest.RestRequest.Method.GET;

public class ExampleRestAction extends BaseRestHandler {

    @Override
    public String getName() {
        return "Example Rest Action";
    }

    @Override
    public List<Route> routes() {
        return List.of(
                new Route(GET, "/devjoon/_example")
        );
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        return channel -> channel.sendResponse(new BytesRestResponse(RestStatus.OK, "Example Action"));
    }
}
