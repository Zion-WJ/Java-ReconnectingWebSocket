package com.dempe.websocket.example;

import com.dempe.websocket.ReconnectingWSClient;
import org.apache.log4j.Logger;
import org.java_websocket.handshake.ServerHandshake;

import javax.print.attribute.standard.Finishings;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/6/2
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
public class ExampleReconnectingClient extends ReconnectingWSClient {

    private final static Logger LOGGER = Logger.getLogger(ExampleReconnectingClient.class);

    public ExampleReconnectingClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpenEvent(ServerHandshake handshakedata) {
        LOGGER.info("========onOpenEvent===========");

    }

    @Override
    public void onErrorEvent(Exception ex) {
        LOGGER.info("========onErrorEvent===========");
    }

    @Override
    public void onMessageEvent(String message) {
        LOGGER.info("========onMessageEvent===========");
    }

    public static void main(String[] args) throws URISyntaxException {
        ExampleReconnectingClient client = new ExampleReconnectingClient(new URI("ws://localhost:8887"));
        client.connect();
    }
}
