package entity;

import com.beust.jcommander.Parameter;

public class ConfigEntity {

    @Parameter(names = {"-s", "--server"})
    private boolean server = false;

    @Parameter(names = {"-c", "--client"})
    private boolean client = false;

    @Parameter(names = {"-p", "--port"})
    private Integer port;

    @Parameter(names = {"-cn", "--connect"})
    private String connect;

    @Parameter(names = {"-f", "--file"})
    private String filePath;

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
