package common;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Commons {

    public static int MESSAGE_HEAD_LENGTH = 1024;
    public static String FILE_PATH = "D:/";

    public static String getMD5(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return DigestUtils.md5Hex(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
