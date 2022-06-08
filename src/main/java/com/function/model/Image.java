package com.function.model;


import java.io.Serializable;

public class Image implements Serializable {
    private String name;

    private byte[] file;

    public Image() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file2) {
        this.file = file2;
    }

    public Image(String postRowId) {
        name = "image_rowKey-" + postRowId + ".jpg";
    }
}
