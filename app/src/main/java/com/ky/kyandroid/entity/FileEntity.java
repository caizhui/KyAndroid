package com.ky.kyandroid.entity;

import android.graphics.Bitmap;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Caizhui on 2017/6/27.
 */
@Table(name = "t_file")
public class FileEntity implements Serializable{

    @Column(name = "uuid",isId = true,autoGen = true)
    private int uuid;


    @Column(name = "sjId")
    private String  sjId;

    /**
     * 图片
     */
    public Bitmap bitmap;


    /**
     * 从后台哪来的文件url链接
     */
    @Column(name = "fileUrl")
    public String fileUrl;

    /**
     * 文件名字
     */
    @Column(name = "fileName")
    public String fileName;

    /**
     * 文件描述
     */
    @Column(name = "fileMs")
    public  String fileMs;

    /**
     * 是否显示描述
     */
    public boolean isHaveMs = true;

    public FileEntity(){}

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getFileMs() {
        return fileMs;
    }

    public void setFileMs(String fileMs) {
        this.fileMs = fileMs;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public boolean isHaveMs() {
        return isHaveMs;
    }

    public void setHaveMs(boolean haveMs) {
        isHaveMs = haveMs;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSjId() {
        return sjId;
    }

    public void setSjId(String sjId) {
        this.sjId = sjId;
    }
}
