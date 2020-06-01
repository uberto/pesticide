package com.ubertob.pesticide.examples.restapi.http;

import spark.Request;
import spark.Response;
import spark.Route;

public class HelloRoute implements Route {

    @Override
    public Object handle(Request request, Response response) {
        return "Hello World";
    }
}
