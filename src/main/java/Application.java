import com.beust.jcommander.JCommander;
import com.beust.jcommander.Strings;
import com.beust.jcommander.internal.Console;
import common.Commons;
import entity.ConfigEntity;
import receive.ReceiveHandler;
import send.SendHandler;

import java.io.File;

public class Application {

    public static void main(String[] args) {
        ConfigEntity configEntity = new ConfigEntity();
        JCommander jCommander = JCommander.newBuilder().addObject(configEntity).build();

        try {
            jCommander.parse(args);
        } catch (Exception e) {
            help(jCommander);
            return;
        }

        if (configEntity.isHelp()) {
            help(jCommander);
            return;
        }

        Console console = JCommander.getConsole();

        if (configEntity.isClient() && !configEntity.isServer()) {
            String connect = configEntity.getConnect();
            String filePath = configEntity.getFilePath();

            if (!Strings.isStringEmpty(connect) && !Strings.isStringEmpty(filePath)) {
                String[] split = connect.split(":");

                if (split.length != 2) {
                    console.println("Host Format Error");
                    return;
                }

                File file = new File(filePath);

                if (!file.isFile()) {
                    console.println("File Path Error");
                    return;
                }
                new SendHandler().send(split[0], Integer.parseInt(split[1]), file);
            } else {
                help(jCommander);
            }
        } else if (configEntity.isServer() && !configEntity.isClient()) {
            String filePath = configEntity.getFilePath();

            if (configEntity.getPort() == null) {
                help(jCommander);
            } else {
                if (!Strings.isStringEmpty(filePath)) {
                    if (new File(filePath).isDirectory()) {
                        Commons.FILE_PATH = filePath;
                    } else {
                        console.println("Directory Is Not Exist");
                        return;
                    }
                }
                new ReceiveHandler().bind(configEntity.getPort());
            }
        } else {
            help(jCommander);
        }
    }

    private static void help(JCommander jCommander) {
        jCommander.setProgramName("file-transfer");
        jCommander.usage();
    }
}
