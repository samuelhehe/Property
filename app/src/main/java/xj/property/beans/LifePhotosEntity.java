package xj.property.beans;

/**
 * Created by che on 2015/7/16.
 */
public class LifePhotosEntity {
    public LifePhotosEntity() {
    }

    public LifePhotosEntity(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private String photoUrl;


    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }
}
