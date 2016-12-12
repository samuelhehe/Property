package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/9/29.
 */
public class InvitedNeighborBean {


    /**
     * status : yes
     * info : [{"registNickname":"不知不觉","registEmobId":"2","registAvatar":"http://...","phone":"15874859658"},{"registNickname":"天天向上","registEmobId":"3","registAvatar":"http://...","phone":"15874851151"},{"phone":"15874852222"},{"phone":"15874852223"}]
     */

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    private List<InfoEntity> info;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(List<InfoEntity> info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public List<InfoEntity> getInfo() {
        return info;
    }

    public static class InfoEntity {
        /**
         * registNickname : 不知不觉
         * registEmobId : 2
         * registAvatar : http://...
         * phone : 15874859658
         */

        private String registNickname;
        private String registEmobId;
        private String registAvatar;
        private String phone;

        public void setRegistNickname(String registNickname) {
            this.registNickname = registNickname;
        }

        public void setRegistEmobId(String registEmobId) {
            this.registEmobId = registEmobId;
        }

        public void setRegistAvatar(String registAvatar) {
            this.registAvatar = registAvatar;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRegistNickname() {
            return registNickname;
        }

        public String getRegistEmobId() {
            return registEmobId;
        }

        public String getRegistAvatar() {
            return registAvatar;
        }

        public String getPhone() {
            return phone;
        }
    }
}
