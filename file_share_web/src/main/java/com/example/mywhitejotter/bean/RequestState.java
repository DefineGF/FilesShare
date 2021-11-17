package com.example.mywhitejotter.bean;

public class RequestState {
    private boolean reqFile;
    private String targetId;
    private String reqFileName;
    private long   reqFileSize;

    public RequestState() {
        reqFile = false;
    }

    public RequestState(boolean reqFile, String targetId, String reqFileName, long reqFileSize) {
        this.reqFile = reqFile;
        this.targetId = targetId;
        this.reqFileName = reqFileName;
        this.reqFileSize = reqFileSize;
    }

    public boolean isReqFile() {
        return reqFile;
    }

    public void setReqFile(boolean reqFile) {
        this.reqFile = reqFile;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getReqFileName() {
        return reqFileName;
    }

    public void setReqFileName(String reqFileName) {
        this.reqFileName = reqFileName;
    }

    public long getReqFileSize() {
        return reqFileSize;
    }

    public void setReqFileSize(long reqFileSize) {
        this.reqFileSize = reqFileSize;
    }
}
