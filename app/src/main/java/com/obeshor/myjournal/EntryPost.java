package com.obeshor.myjournal;

import java.util.Date;

public class EntryPost extends EntryPostId {
    public String user_id, title, desc;
    public Date timestamp;

    public EntryPost() {}

    public EntryPost(String user_id, String title,  String desc) {
        this.user_id = user_id;
        this.title = title;
        this.desc = desc;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
