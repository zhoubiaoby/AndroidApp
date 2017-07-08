package com.example.zhoubiao.cxcourses.dataobject;

/**
 * Created by zhoubiao on 2015/10/10.
 */
public class LocalCourse {
    private String name;
    private String localPath;
    private int isFinished;

    public LocalCourse(String name, String localPath, int isFinished) {
        this.name = name;
        this.localPath = localPath;
        this.isFinished = isFinished;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }

    public String getName() {

        return name;
    }

    public String getLocalPath() {
        return localPath;
    }

    public int getIsFinished() {
        return isFinished;
    }


}
