package send;

import com.alibaba.fastjson.JSON;
import common.Commons;
import entity.MessageHead;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static common.Commons.MESSAGE_HEAD_LENGTH;

public class SendHandler {

    private static final Logger LOGGER = LogManager.getLogger(SendHandler.class);

    public void send(String host, int port, File file) {
        LOGGER.info("Get File MD5");
        String md5 = Commons.getMD5(file);

        if (md5 == null) {
            LOGGER.error("Get File Md5 Failed");
            return;
        }

        try (
                SocketChannel socketChannel = SocketChannel.open();
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                FileChannel fileChannel = randomAccessFile.getChannel()
        ) {
            LOGGER.info("Connect To Server");

            if (!socketChannel.connect(new InetSocketAddress(host, port))) {
                LOGGER.error("Connection Fail");
                return;
            }

            LOGGER.info("File Upload");
            long fileSize = fileChannel.size();
            ByteBuffer byteBuffer = ByteBuffer.allocate(MESSAGE_HEAD_LENGTH);
            byte[] bytes = Arrays.copyOf(JSON.toJSONBytes(new MessageHead(file.getName(), fileSize, md5)), MESSAGE_HEAD_LENGTH);
            socketChannel.write(byteBuffer.put(bytes).flip());

            long position = 0;

            while (position < fileSize) {
                position += fileChannel.transferTo(position, fileSize, socketChannel);
            }

            byteBuffer.flip().clear();

            while (socketChannel.read(byteBuffer) != -1) {
                if (byteBuffer.remaining() == 0) {
                    break;
                }
            }

            byte[] receiveMessage = new byte[byteBuffer.flip().remaining()];
            byteBuffer.get(receiveMessage);

            if (new String(receiveMessage, StandardCharsets.UTF_8).equals(Commons.SUCCESS)) {
                LOGGER.info("Complete");
            } else {
                LOGGER.error("Failed");
            }
        } catch (IOException e) {
            LOGGER.error("Error");
            e.printStackTrace();
        }
    }
}
