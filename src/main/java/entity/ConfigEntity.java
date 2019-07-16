package entity;

import com.beust.jcommander.Parameter;

public class ConfigEntity {

    @Parameter(names = {"-s", "--server"}, description = "Start As Server")
    private boolean server = false;

    @Parameter(names = {"-c", "--client"}, description = "Start As Client")
    private boolean client = false;

    @Parameter(names = {"-p", "--port"}, description = "Binding Port")
    private Integer port;

    @Parameter(names = {"-cn", "--connect"}, description = "Target Server")
    private String connect;

    @Parameter(names = {"-f", "--file"}, description = "File Path")
    private String filePath;

    @Parameter(names = {"-h", "--help"}, help = true)
    private boolean help;

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getConnect() {
        return connect;
    }

    public void setConnect(String connect) {
        this.connect = connect;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
