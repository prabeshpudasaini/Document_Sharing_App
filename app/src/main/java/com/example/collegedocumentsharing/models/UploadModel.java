package com.example.collegedocumentsharing.models;

public class UploadModel {

    private String fileName,information,uploaded_by,username;
    private String fileUrl;
    private boolean isOwner;
    private String uploaded_date;

    public UploadModel(){}

    public UploadModel(String fileName, String information, String fileUrl, String uploaded_by,
                       String username, String uploaded_date, boolean isOwner){
        
//        if(information.trim().equals("")){
//            information = "";
//        }
        this.fileName = fileName;
        this.information = information;
        this.fileUrl = fileUrl;
        this.uploaded_by = uploaded_by;
        this.username = username;
        this.uploaded_date = uploaded_date;
        this.isOwner = isOwner;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(String uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public String getUploaded_date() {
        return uploaded_date;
    }

    public void setUploaded_date(String uploaded_date) {
        this.uploaded_date = uploaded_date;
    }
}
