package xj.property.beans;

/**
 * Created by Administrator on 2015/10/16.
 */
public class BangBiCountBean {


    /**
     * status : yes
     * info : {"exchange":1,"bonuscoin":100,"bonuscoinCount":400}
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

    public static class InfoEntity {
        /**
         * exchange : 1
         * bonuscoin : 100
         * bonuscoinCount : 400
         * showBonuscoin: no
         */

        private int exchange;
        private int bonuscoin;
        private int bonuscoinCount;

        private int count;
        private float ratio;

        private String showBonuscoin;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public float getRatio() {
            return ratio;
        }

        public void setRatio(float ratio) {
            this.ratio = ratio;
        }

        public void setExchange(int exchange) {
            this.exchange = exchange;
        }

        public void setBonuscoin(int bonuscoin) {
            this.bonuscoin = bonuscoin;
        }

        public void setBonuscoinCount(int bonuscoinCount) {
            this.bonuscoinCount = bonuscoinCount;
        }

        public int getExchange() {
            return exchange;
        }

        public int getBonuscoin() {
            return bonuscoin;
        }

        public int getBonuscoinCount() {
            return bonuscoinCount;
        }

        public String getShowBonuscoin() {
            return showBonuscoin;
        }

        public void setShowBonuscoin(String showBonuscoin) {
            this.showBonuscoin = showBonuscoin;
        }

    }
}
