package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/20.
 */
public class SrroundingInfoBean {


    /**
     * status : yes
     * info : [{"facilitiesClassId":217,"facilitiesClassName":"美食","weight":1,"picName":"nearby_food"},{"facilitiesClassId":218,"facilitiesClassName":"娱乐","weight":1,"picName":"nearby_ktv"},{"facilitiesClassId":219,"facilitiesClassName":"洗车","weight":1,"picName":"nearby_carwash"},{"facilitiesClassId":220,"facilitiesClassName":"健身","weight":1,"picName":"nearby_tearoom"},{"facilitiesClassId":221,"facilitiesClassName":"商场","weight":1,"picName":"nearby_dime_store"}]
     * longitude : 116.4307
     * latitude : 39.89178
     */

    private String status;
    private String message;
    private double longitude;
    private double latitude;
    private List<InfoEntity> info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setInfo(List<InfoEntity> info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public List<InfoEntity> getInfo() {
        return info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class InfoEntity {
        /**
         * name : {分类名称}
         * photo : {分类图片}
         */

        private String name;
        private String photo;

        public void setName(String name) {
            this.name = name;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getName() {
            return name;
        }

        public String getPhoto() {
            return photo;
        }


    }
}
