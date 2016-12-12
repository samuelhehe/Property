package xj.property.beans;

/**
 * Created by Administrator on 2015/4/24.
 */
public class TouristResult {
    public String status;
    public TouristBean info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TouristBean getInfo() {
        return info;
    }

    public void setInfo(TouristBean info) {
        this.info = info;
    }

    public static class TouristBean{
        public String emobId;
        public String role;
        public int userId;

        public String getEmobId() {
            return emobId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "TouristBean{" +
                    "emobId='" + emobId + '\'' +
                    ", role='" + role + '\'' +
                    ", userId=" + userId +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TouristResult{" +
                "status='" + status + '\'' +
                ", info=" + info +
                '}';
    }
}
