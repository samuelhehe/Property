package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/25.
 */
public class VoteIndexDiscussInfoRespBean {


    /**
     * status : yes
     * info : {"characterValues":0,"characterPercent":0,"tips":[{"nickname":"我是6号","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FobiXm7f18ZvXGbWvoDrmUbR0qof","type":"reply","sourceId":"1"}]}
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
         * characterValues : 0
         * characterPercent : 0
         * tips : [{"nickname":"我是6号","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FobiXm7f18ZvXGbWvoDrmUbR0qof","type":"reply","sourceId":"1"}]
         */

        private int characterValues;
        private float characterPercent;
        private List<VoteIndexDiscussInfoNotify> tips;

        public void setCharacterValues(int characterValues) {
            this.characterValues = characterValues;
        }

        public int getCharacterValues() {
            return characterValues;
        }


        public List<VoteIndexDiscussInfoNotify> getTips() {
            return tips;
        }

        public void setTips(List<VoteIndexDiscussInfoNotify> tips) {
            this.tips = tips;
        }

        public float getCharacterPercent() {
            return characterPercent;
        }

        public void setCharacterPercent(float characterPercent) {
            this.characterPercent = characterPercent;
        }
    }
}
