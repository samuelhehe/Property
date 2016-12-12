package xj.property.beans;

/**
 * Created by Administrator on 2015/9/28.
 */
public class NeedInviteNums {


    /**
     * status : yes
     * info : {"require4Bangzhu":3,"require4FuBangzhu":1,"require4Zhanglao":0,"unInvited4Bangzhu":1,"unInvited4FuBangzhu":0,"unInvited4Zhanglao":0,"registCount":2}
     */

    private String status;
    private InfoEntity info;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

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

    public static class InfoEntity {
        /**
         * require4Bangzhu : 3
         * require4FuBangzhu : 1
         * require4Zhanglao : 0
         * unInvited4Bangzhu : 1
         * unInvited4FuBangzhu : 0
         * unInvited4Zhanglao : 0
         * registCount : 2
         */

        private int require4Bangzhu;
        private int require4FuBangzhu;
        private int require4Zhanglao;
        private int unInvited4Bangzhu;
        private int unInvited4FuBangzhu;
        private int unInvited4Zhanglao;
        private int registCount;

        public void setRequire4Bangzhu(int require4Bangzhu) {
            this.require4Bangzhu = require4Bangzhu;
        }

        public void setRequire4FuBangzhu(int require4FuBangzhu) {
            this.require4FuBangzhu = require4FuBangzhu;
        }

        public void setRequire4Zhanglao(int require4Zhanglao) {
            this.require4Zhanglao = require4Zhanglao;
        }

        public void setUnInvited4Bangzhu(int unInvited4Bangzhu) {
            this.unInvited4Bangzhu = unInvited4Bangzhu;
        }

        public void setUnInvited4FuBangzhu(int unInvited4FuBangzhu) {
            this.unInvited4FuBangzhu = unInvited4FuBangzhu;
        }

        public void setUnInvited4Zhanglao(int unInvited4Zhanglao) {
            this.unInvited4Zhanglao = unInvited4Zhanglao;
        }

        public void setRegistCount(int registCount) {
            this.registCount = registCount;
        }

        public int getRequire4Bangzhu() {
            return require4Bangzhu;
        }

        public int getRequire4FuBangzhu() {
            return require4FuBangzhu;
        }

        public int getRequire4Zhanglao() {
            return require4Zhanglao;
        }

        public int getUnInvited4Bangzhu() {
            return unInvited4Bangzhu;
        }

        public int getUnInvited4FuBangzhu() {
            return unInvited4FuBangzhu;
        }

        public int getUnInvited4Zhanglao() {
            return unInvited4Zhanglao;
        }

        public int getRegistCount() {
            return registCount;
        }
    }
}
