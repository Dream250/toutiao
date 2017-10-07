package com.hjj.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/7.
 */
public class News {
  private int id;
  private String title;
    private String content;
  private String image;
  private int likeCount;
  private int commentCount;
  private Date createdDate;
  private int userId;
    private int status;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
  public String getImage() {
    return image;
  }
  public void setImage(String image) {
    this.image = image;
  }
  public int getLikeCount() {
    return likeCount;
  }
  public void setLikeCount(int likeCount) {
    this.likeCount = likeCount;
  }
  public int getCommentCount() {
    return commentCount;
  }
  public void setCommentCount(int commentCount) {
    this.commentCount = commentCount;
  }
  public Date getCreatedDate() {
    return createdDate;
  }
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
}
