package common;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Commons {

    public static final int MESSAGE_HEAD_LENGTH = 1024;
    public static final String SUCCESS = "1";
    public static final String FAILED = "0";
    public static String FILE_PATH = "./";

    public static String getMD5(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return DigestUtils.md5Hex(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
