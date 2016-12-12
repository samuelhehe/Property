package xj.property.beans;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/16.
 */
public class MspShare2LifeCircleBean {


    /**
     * status : yes
     * info : {"photo":"${会员卡图片地址}","shopLogo":"${周边店家Logo地址}"}
     */

    private String status;
    private String message;
    private InfoEntity info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class InfoEntity  implements Serializable{
        /**
         * photo : ${会员卡图片地址}
         * shopLogo : ${周边店家Logo地址}
         */

        private String photo;
        private String shopLogo;

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setShopLogo(String shopLogo) {
            this.shopLogo = shopLogo;
        }

        public String getPhoto() {
            return photo;
        }

        public String getShopLogo() {
            return shopLogo;
        }
    }
}
