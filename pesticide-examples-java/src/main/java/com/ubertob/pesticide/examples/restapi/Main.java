package com.ubertob.pesticide.examples.restapi;

import com.ubertob.pesticide.examples.restapi.http.HelloRoute;
import com.ubertob.pesticide.examples.restapi.http.NamedRoute;
import com.ubertob.pesticide.examples.restapi.model.TicketHub;

import static spark.Spark.get;
import static spark.Spark.port;

public class Main {
    public static void main(String[] args) {
        TicketHub hub = new TicketHub();
        port(8080);
        get("/hello", new HelloRoute());
        get("/hello/:name", new NamedRoute(":name"));
    }
}
