
package com.purpletealabs.sephora.dtos;

import com.google.gson.annotations.SerializedName;

public class Book {

    @SerializedName("id")
    private String id;

    @SerializedName("volumeInfo")
    private VolumeInfo volumeInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}
