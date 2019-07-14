package receive;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class ReceiveHandler {

    public void bind(int port) {
        try (
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ) {
            serverSocketChannel.bind(new InetSocketAddress(port));

            while (true) {
                new AcceptThread(serverSocketChannel.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ReceiveHandler().bind(1999);
    }
}
