package entity;

public class MessageHead {

    private String fileName;
    private long fileSize;
    private String MD5;

    public MessageHead(String fileName, long fileSize, String MD5) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.MD5 = MD5;
    }

    public MessageHead() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }
}
