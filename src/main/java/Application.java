import com.beust.jcommander.JCommander;
import com.beust.jcommander.Strings;
import common.Commons;
import entity.ConfigEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import receive.ReceiveHandler;
import send.SendHandler;

import java.io.File;

public class Application {

    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        ConfigEntity configEntity = new ConfigEntity();

        JCommander.newBuilder()
                .addObject(configEntity)
                .build()
                .parse(args);

        if (configEntity.isClient() && !configEntity.isServer()) {
            String connect = configEntity.getConnect();
            String filePath = configEntity.getFilePath();

            if (!Strings.isStringEmpty(connect) && !Strings.isStringEmpty(filePath)) {
                String[] split = connect.split(":", 2);

                if (split.length != 2) {
                    LOGGER.error("Host Format Error");
                    return;
                }

                File file = new File(filePath);

                if (!file.isFile()) {
                    LOGGER.error("File Path Error");
                    return;
                }
                new SendHandler().send(split[0], Integer.parseInt(split[1]), file);
            } else {
                LOGGER.error("Command Error");
            }
        } else if (configEntity.isServer() && !configEntity.isClient()) {
            String filePath = configEntity.getFilePath();

            if (configEntity.getPort() == null) {
                LOGGER.error("Command Error");
            } else {
                if (filePath != null) {
                    if (new File(filePath).isDirectory()) {
                        Commons.FILE_PATH = filePath;
                    } else {
                        LOGGER.error("Directory Is Not Exist");
                        return;
                    }
                }
                new ReceiveHandler().bind(configEntity.getPort());
            }
        } else {
            LOGGER.error("Command Error");
        }
    }
}
