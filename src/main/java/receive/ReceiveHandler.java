package receive;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ReceiveHandler {

    private static final Logger LOGGER = LogManager.getLogger(ReceiveHandler.class);

    public void bind(int port) {
        try (
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()
        ) {
            serverSocketChannel.bind(new InetSocketAddress(port));
            LOGGER.info("Bind To: " + port);

            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                new AcceptThread(socketChannel).start();
            }
        } catch (IOException e) {
            LOGGER.error("Error");
            e.printStackTrace();
        }
    }
}
