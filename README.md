# Java-ReconnectingWebSocket
>基于Nathan Rajlich的Java-Websocket实现的java自动重连的客户端


## example

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
