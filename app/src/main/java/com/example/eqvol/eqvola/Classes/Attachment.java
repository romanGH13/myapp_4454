package com.example.eqvol.eqvola.Classes;

/**
 * Created by eqvol on 09.11.2017.
 */

public class Attachment {
    private int id;
    private int message_id;
    private String type;
    private String date;
    private byte[] data;
    private String attachment;

    public Attachment(String attachment, String type)
    {
        this.attachment = attachment;
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
