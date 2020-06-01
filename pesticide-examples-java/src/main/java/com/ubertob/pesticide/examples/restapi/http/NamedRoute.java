package com.ubertob.pesticide.examples.restapi.http;

import spark.Request;
import spark.Response;
import spark.Route;

public class NamedRoute implements Route {

    private String paramName;

    public NamedRoute(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return "Hello: " + request.params(paramName);
    }
}
