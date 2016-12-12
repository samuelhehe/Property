package xj.property.beans;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/5/21.
 */
public class AppLoginInfoBean {
   public String status;
   public Info info;

    @Override
    public String toString() {
        return "AppLoginInfoBean{" +
                "status='" + status + '\'' +
                ", info=" + info +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public static class  Info implements Serializable{
       public  String key;
        public int loginTime;
        public String emobId;
       public String equipment;
        public int communityId;

        @Override
        public String toString() {
            return "Info{" +
                    "key='" + key + '\'' +
                    ", loginTime=" + loginTime +
                    ", emobId='" + emobId + '\'' +
                    ", equipment='" + equipment + '\'' +
                    ", communityId=" + communityId +
                    '}';
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(int loginTime) {
            this.loginTime = loginTime;
        }

        public String getEmobId() {
            return emobId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public String getEquipment() {
            return equipment;
        }

        public void setEquipment(String equipment) {
            this.equipment = equipment;
        }

        public int getCommunityId() {
            return communityId;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }
    }
}
