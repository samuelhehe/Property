package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/7/3.
 */
public class TopListPhotoRespone {
  public String status;

    public List<info> info;

    public List<TopListPhotoRespone.info> getInfo() {
        return info;
    }

    public void setInfo(List<TopListPhotoRespone.info> info) {
        this.info = info;
    }

    public static class info{

     public    String imgUrl;
      public int   serviceId;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
