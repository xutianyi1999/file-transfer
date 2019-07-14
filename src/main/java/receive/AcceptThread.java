package receive;

import com.alibaba.fastjson.JSON;
import common.Commons;
import entity.MessageHead;

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

    private SocketChannel socketChannel;

    public AcceptThread(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        ByteBuffer messageHeadBuffer = ByteBuffer.allocateDirect(MESSAGE_HEAD_LENGTH);

        try {
            while (socketChannel.read(messageHeadBuffer) != -1) {
                if (messageHeadBuffer.remaining() == 0) {
                    break;
                }
            }
        } catch (Exception e) {
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

        File file = new File(FILE_PATH, messageHead.getFileName());

        try (
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                FileChannel fileChannel = randomAccessFile.getChannel()
        ) {
            fileChannel.transferFrom(socketChannel, 0, messageHead.getFileSize());
            String md5 = Commons.getMD5(file);

            String message = null;
            if (md5 != null && md5.equals(messageHead.getMD5())) {
                message = "success";
            } else {
                message = "error";
            }

            messageHeadBuffer.flip().clear().put(message.getBytes(StandardCharsets.UTF_8)).flip();
            socketChannel.write(messageHeadBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
