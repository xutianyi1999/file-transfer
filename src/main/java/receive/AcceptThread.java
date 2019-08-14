package receive;

import com.alibaba.fastjson.JSON;
import common.Commons;
import entity.MessageHead;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static common.Commons.FILE_PATH;
import static common.Commons.MESSAGE_HEAD_LENGTH;


public class AcceptThread extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(AcceptThread.class);
    private SocketChannel socketChannel;

    AcceptThread(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        ByteBuffer messageHeadBuffer = ByteBuffer.allocate(MESSAGE_HEAD_LENGTH);

        try {
            while (socketChannel.read(messageHeadBuffer) != -1) {
                if (messageHeadBuffer.remaining() == 0) {
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Analysis Message Head Failed");
            e.printStackTrace();

            try {
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }

        byte[] bytes = new byte[MESSAGE_HEAD_LENGTH];
        messageHeadBuffer.flip().get(bytes);
        MessageHead messageHead = JSON.parseObject(bytes, MessageHead.class);

        LOGGER.info("Downloading: " + messageHead.getFileName());
        File file = new File(FILE_PATH, messageHead.getFileName());

        try (
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                FileChannel fileChannel = randomAccessFile.getChannel()
        ) {
            fileChannel.transferFrom(socketChannel, 0, messageHead.getFileSize());

            LOGGER.info("Check File MD5");
            String md5 = Commons.getMD5(file);
            String message;

            if (md5 != null && md5.equals(messageHead.getMD5())) {
                LOGGER.info("Success");
                message = Commons.SUCCESS;
            } else {
                LOGGER.error("Failed");
                message = Commons.FAILED;
            }

            messageHeadBuffer.flip().clear().put(message.getBytes(StandardCharsets.UTF_8)).flip();
            socketChannel.write(messageHeadBuffer);
        } catch (Exception e) {
            LOGGER.error("Error");
            e.printStackTrace();
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                LOGGER.error("Error");
                e.printStackTrace();
            }
        }
    }
}
