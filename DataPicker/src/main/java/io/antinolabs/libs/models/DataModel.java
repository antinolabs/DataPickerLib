package io.antinolabs.libs.models;

public class DataModel {
  String path;
  int fileType;
  boolean isSelected = false;

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


  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
  }
}
