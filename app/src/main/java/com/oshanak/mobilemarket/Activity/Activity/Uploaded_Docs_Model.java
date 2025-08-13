package com.oshanak.mobilemarket.Activity.Activity;

import java.io.Serializable;

public class Uploaded_Docs_Model implements Serializable {
    int Uploaded_Doc_Id;
    String Uploaded_Doc_Title;
    String DocumentTypeTitle;
    int DocumentTypeCode;
    String Uploaded_Doc_Desc;
    String Uploaded_Doc_Date;
    int Uploaded_Doc_img1_ID;
    int Uploaded_Doc_img2_ID;
    int Uploaded_Doc_img3_ID;
    int Uploaded_Doc_img4_ID;
    int Uploaded_Doc_img5_ID;
    String Uploaded_Doc_img1;
    String Uploaded_Doc_img2;
    String Uploaded_Doc_img3;
    String Uploaded_Doc_img4;
    String Uploaded_Doc_img5;

    public Uploaded_Docs_Model(int uploaded_Doc_Id, String uploaded_Doc_Title, String documentTypeTitle, int documentTypeCode, String uploaded_Doc_Desc, String uploaded_Doc_Date, int uploaded_Doc_img1_ID, int uploaded_Doc_img2_ID, int uploaded_Doc_img3_ID, int uploaded_Doc_img4_ID, int uploaded_Doc_img5_ID, String uploaded_Doc_img1, String uploaded_Doc_img2, String uploaded_Doc_img3, String uploaded_Doc_img4, String uploaded_Doc_img5) {
        Uploaded_Doc_Id = uploaded_Doc_Id;
        Uploaded_Doc_Title = uploaded_Doc_Title;
        DocumentTypeTitle = documentTypeTitle;
        DocumentTypeCode = documentTypeCode;
        Uploaded_Doc_Desc = uploaded_Doc_Desc;
        Uploaded_Doc_Date = uploaded_Doc_Date;
        Uploaded_Doc_img1_ID = uploaded_Doc_img1_ID;
        Uploaded_Doc_img2_ID = uploaded_Doc_img2_ID;
        Uploaded_Doc_img3_ID = uploaded_Doc_img3_ID;
        Uploaded_Doc_img4_ID = uploaded_Doc_img4_ID;
        Uploaded_Doc_img5_ID = uploaded_Doc_img5_ID;
        Uploaded_Doc_img1 = uploaded_Doc_img1;
        Uploaded_Doc_img2 = uploaded_Doc_img2;
        Uploaded_Doc_img3 = uploaded_Doc_img3;
        Uploaded_Doc_img4 = uploaded_Doc_img4;
        Uploaded_Doc_img5 = uploaded_Doc_img5;
    }

    public Uploaded_Docs_Model(int uploaded_Doc_Id, String uploaded_Doc_Title, String documentTypeTitle, int documentTypeCode, String uploaded_Doc_Desc, String uploaded_Doc_Date) {
        Uploaded_Doc_Id = uploaded_Doc_Id;
        Uploaded_Doc_Title = uploaded_Doc_Title;
        DocumentTypeTitle = documentTypeTitle;
        DocumentTypeCode = documentTypeCode;
        Uploaded_Doc_Desc = uploaded_Doc_Desc;
        Uploaded_Doc_Date = uploaded_Doc_Date;
    }

    public int getUploaded_Doc_Id() {
        return Uploaded_Doc_Id;
    }

    public void setUploaded_Doc_Id(int uploaded_Doc_Id) {
        Uploaded_Doc_Id = uploaded_Doc_Id;
    }

    public String getUploaded_Doc_Title() {
        return Uploaded_Doc_Title;
    }

    public void setUploaded_Doc_Title(String uploaded_Doc_Title) {
        Uploaded_Doc_Title = uploaded_Doc_Title;
    }

    public String getDocumentTypeTitle() {
        return DocumentTypeTitle;
    }

    public void setDocumentTypeTitle(String documentTypeTitle) {
        DocumentTypeTitle = documentTypeTitle;
    }

    public int getDocumentTypeCode() {
        return DocumentTypeCode;
    }

    public void setDocumentTypeCode(int documentTypeCode) {
        DocumentTypeCode = documentTypeCode;
    }

    public String getUploaded_Doc_Desc() {
        return Uploaded_Doc_Desc;
    }

    public void setUploaded_Doc_Desc(String uploaded_Doc_Desc) {
        Uploaded_Doc_Desc = uploaded_Doc_Desc;
    }

    public String getUploaded_Doc_Date() {
        return Uploaded_Doc_Date;
    }

    public void setUploaded_Doc_Date(String uploaded_Doc_Date) {
        Uploaded_Doc_Date = uploaded_Doc_Date;
    }

    public int getUploaded_Doc_img1_ID() {
        return Uploaded_Doc_img1_ID;
    }

    public void setUploaded_Doc_img1_ID(int uploaded_Doc_img1_ID) {
        Uploaded_Doc_img1_ID = uploaded_Doc_img1_ID;
    }

    public int getUploaded_Doc_img2_ID() {
        return Uploaded_Doc_img2_ID;
    }

    public void setUploaded_Doc_img2_ID(int uploaded_Doc_img2_ID) {
        Uploaded_Doc_img2_ID = uploaded_Doc_img2_ID;
    }

    public int getUploaded_Doc_img3_ID() {
        return Uploaded_Doc_img3_ID;
    }

    public void setUploaded_Doc_img3_ID(int uploaded_Doc_img3_ID) {
        Uploaded_Doc_img3_ID = uploaded_Doc_img3_ID;
    }

    public int getUploaded_Doc_img4_ID() {
        return Uploaded_Doc_img4_ID;
    }

    public void setUploaded_Doc_img4_ID(int uploaded_Doc_img4_ID) {
        Uploaded_Doc_img4_ID = uploaded_Doc_img4_ID;
    }

    public int getUploaded_Doc_img5_ID() {
        return Uploaded_Doc_img5_ID;
    }

    public void setUploaded_Doc_img5_ID(int uploaded_Doc_img5_ID) {
        Uploaded_Doc_img5_ID = uploaded_Doc_img5_ID;
    }

    public String getUploaded_Doc_img1() {
        return Uploaded_Doc_img1;
    }

    public void setUploaded_Doc_img1(String uploaded_Doc_img1) {
        Uploaded_Doc_img1 = uploaded_Doc_img1;
    }

    public String getUploaded_Doc_img2() {
        return Uploaded_Doc_img2;
    }

    public void setUploaded_Doc_img2(String uploaded_Doc_img2) {
        Uploaded_Doc_img2 = uploaded_Doc_img2;
    }

    public String getUploaded_Doc_img3() {
        return Uploaded_Doc_img3;
    }

    public void setUploaded_Doc_img3(String uploaded_Doc_img3) {
        Uploaded_Doc_img3 = uploaded_Doc_img3;
    }

    public String getUploaded_Doc_img4() {
        return Uploaded_Doc_img4;
    }

    public void setUploaded_Doc_img4(String uploaded_Doc_img4) {
        Uploaded_Doc_img4 = uploaded_Doc_img4;
    }

    public String getUploaded_Doc_img5() {
        return Uploaded_Doc_img5;
    }

    public void setUploaded_Doc_img5(String uploaded_Doc_img5) {
        Uploaded_Doc_img5 = uploaded_Doc_img5;
    }
}
