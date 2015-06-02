package com.dempe.websocket;

import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.channels.NotYetConnectedException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自动重连的webSocket的客户端
 * User: Dempe
 * <p/>
 * <p/>
 * Date: 2015/6/2
 * Time: 12:51
 * To change this template use File | Settings | File Templates.
 */
public abstract class ReconnectingWSClient {
    private final static Logger LOGGER = Logger.getLogger(ReconnectingWSClient.class);

    /**
     * 默认重试时间间隔
     */
    private final static int DEFAULT_INTERVAL = 1;

    private WebSocketClient ws;

    private URI serverURI;
    private Draft draft;
    /**
     * 重试时间间隔
     */
    private int interval;

    private AtomicInteger attemptCounter = new AtomicInteger(0);

    public ReconnectingWSClient(URI serverURI, Draft draft, int interval) {
        this.serverURI = serverURI;
        this.draft = draft;
        this.interval = interval > 0 ? interval : 1;
        ws = getWs();
    }

    public ReconnectingWSClient(URI serverURI, Draft draft) {
        this(serverURI, draft, DEFAULT_INTERVAL);
    }

    public ReconnectingWSClient(URI serverURI) {
        this(serverURI, new Draft_10());
    }

    public void reConnect() {
        attemptCounter.incrementAndGet();
        try {
            TimeUnit.SECONDS.sleep(interval);
            LOGGER.info("thread sleep " + interval + ", attemptCount = " + attemptCounter.get());
        } catch (InterruptedException e) {
            LOGGER.info(e.getMessage(), e);
        }

        ws = getWs();
        ws.connect();


    }

    public void connect() {
        ws.connect();
    }


    public void close() {
        ws.close();
    }


    public void send(String text) throws NotYetConnectedException {
        ws.send(text);
    }


    public void send(byte[] bytes) throws IllegalArgumentException, NotYetConnectedException {
        ws.send(bytes);
    }


    public InetSocketAddress getRemoteSocketAddress() {
        return null;
    }

    public InetSocketAddress getLocalSocketAddress() {
        return ws.getLocalSocketAddress(ws.getConnection());
    }

    public boolean isOpen() {
        return ws.getConnection().isOpen();
    }

    public boolean isClosing() {
        return ws.getConnection().isClosing();
    }

    public boolean isFlushAndClose() {
        return ws.getConnection().isFlushAndClose();
    }

    public boolean isClosed() {
        return ws.getConnection().isClosed();
    }

    public Draft getDraft() {
        return ws.getDraft();
    }


    public boolean isConnecting() {
        return ws.getConnection().isConnecting();
    }

    public WebSocketClient getWs() {
        return new WebSocketClient(serverURI, draft) {
            @Override
            public void onClose(int code, String reason, boolean remote) {
                onCloseEvent(code, reason, remote);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                attemptCounter = new AtomicInteger(0);
                onOpenEvent(handshakedata);
            }

            @Override
            public void onMessage(String message) {
                onMessageEvent(message);
            }

            @Override
            public void onError(Exception ex) {
                onErrorEvent(ex);
            }
        };
    }

    public abstract void onOpenEvent(ServerHandshake handshakedata);

    public abstract void onErrorEvent(Exception ex);

    public abstract void onMessageEvent(String message);

    public void onCloseEvent(int code, String reason, boolean remote) {
        LOGGER.info("[onClose Event]:code = " + code + ", reason = " + reason + ", remote = " + remote);
        reConnect();
    }


}
