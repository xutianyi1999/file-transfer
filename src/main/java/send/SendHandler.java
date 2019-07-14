package send;

import com.alibaba.fastjson.JSON;
import common.Commons;
import entity.MessageHead;

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

    public void send(String path) {
        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            return;
        }

        String md5 = Commons.getMD5(file);

        if (md5 == null) {
            return;
        }

        try (
                SocketChannel socketChannel = SocketChannel.open();
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                FileChannel fileChannel = randomAccessFile.getChannel();
        ) {
            if (!socketChannel.connect(new InetSocketAddress("127.0.0.1", 1999))) {
                return;
            }

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(MESSAGE_HEAD_LENGTH);
            byte[] bytes = Arrays.copyOf(JSON.toJSONBytes(new MessageHead(file.getName(), fileChannel.size(), md5)), MESSAGE_HEAD_LENGTH);
            socketChannel.write(byteBuffer.put(bytes).flip());
            fileChannel.transferTo(0, fileChannel.size(), socketChannel);

            byteBuffer.flip().clear();

            while (socketChannel.read(byteBuffer) != -1) {
                if (byteBuffer.remaining() == 0) {
                    break;
                }
            }

            byte[] receiveMessage = new byte[byteBuffer.remaining()];
            byteBuffer.get(receiveMessage);
            System.out.println(new String(receiveMessage, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SendHandler().send("C:\\Users\\xutia\\Pictures\\v2-18701cc218618f5e315550190ee61dff_hd.jpg");
    }
}
