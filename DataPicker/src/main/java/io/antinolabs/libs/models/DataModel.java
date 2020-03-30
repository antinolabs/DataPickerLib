package io.antinolabs.libs.models;

public class DataModel {
    String path;
    int fileType;

    public DataModel(String path, int fileType) {
        this.path = path;
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}
