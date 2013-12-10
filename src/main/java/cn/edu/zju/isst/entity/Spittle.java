package cn.edu.zju.isst.entity;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Spittle {
    private int id;
    
    private int year;

    private int user_id;

    @NotNull(message = "Content must not be null")
    @Size(min = 15, max = 200, message = "Content must be between 15 and 200 characters long.")
    private String content;

    private long post_time;

    private int likes;

    private int dislikes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
    }

    public long getPostTime() {
        return post_time;
    }

    public void setPostTime(long postTime) {
        this.post_time = postTime;
    }
    
    public void setPostTime(Timestamp postTime) {
        this.post_time = postTime.getTime();
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}