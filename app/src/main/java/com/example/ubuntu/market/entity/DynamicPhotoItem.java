package com.example.ubuntu.market.entity;

/**
 * 作者： wangyue on 17-4-10 16:30
 * 邮箱：973459080@qq.com
 */
public class DynamicPhotoItem {
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isPick() {
        return isPick;
    }

    public void setPick(boolean pick) {
        isPick = pick;
    }

    private String filePath;
    private boolean isPick;
    public DynamicPhotoItem() {
    }

    public DynamicPhotoItem(String filePath, boolean isPick) {
        this.filePath = filePath;
        this.isPick = isPick;
    }
}
