package xj.property.beans;

import java.util.List;

/**
 * Created by che on 2015/6/9.
 */
public class TimeLineBeanDetail {
    List<String> images ;
    String content ;
    String photo_size;
    String comment_size;
    String praise_size;

    public TimeLineBeanDetail() {
    }

    public TimeLineBeanDetail(List<String> images, String content, String photo_size, String comment_size, String praise_size) {
        this.images = images;
        this.content = content;
        this.photo_size = photo_size;
        this.comment_size = comment_size;
        this.praise_size = praise_size;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto_size() {
        return photo_size;
    }

    public void setPhoto_size(String photo_size) {
        this.photo_size = photo_size;
    }

    public String getComment_size() {
        return comment_size;
    }

    public void setComment_size(String comment_size) {
        this.comment_size = comment_size;
    }

    public String getPraise_size() {
        return praise_size;
    }

    public void setPraise_size(String praise_size) {
        this.praise_size = praise_size;
    }
}
