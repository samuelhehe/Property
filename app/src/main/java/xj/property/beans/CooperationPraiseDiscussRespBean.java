package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public class CooperationPraiseDiscussRespBean {


    /**
     * status : yes
     * info : {"characterValues":1050,"characterPercent":99.88512349224582,"tips":[{"nickname":"葫芦侠","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fls9XjF87nmvcsAs8ilkkMyafrc7","type":"praise","sourceId":"5b72624e68cee8047f5cce9acba426ee"},{"avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl-0dVrsWOBP8F1W_t8Mh0y6sZY2","type":"content","sourceId":"1"},{"nickname":"maxwell","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl-0dVrsWOBP8F1W_t8Mh0y6sZY2","type":"praise","sourceId":"d11504bf81a4949e8a348e9f783f68f7"}]}
     */

    private String status;
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

    public static class InfoEntity {
        /**
         * characterValues : 1050
         * characterPercent : 99.88512349224582
         * tips : [{"nickname":"葫芦侠","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fls9XjF87nmvcsAs8ilkkMyafrc7","type":"praise","sourceId":"5b72624e68cee8047f5cce9acba426ee"},{"avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl-0dVrsWOBP8F1W_t8Mh0y6sZY2","type":"content","sourceId":"1"},{"nickname":"maxwell","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fl-0dVrsWOBP8F1W_t8Mh0y6sZY2","type":"praise","sourceId":"d11504bf81a4949e8a348e9f783f68f7"}]
         */

        private int characterValues;
        private double characterPercent;
        private List<CooperationPraiseDiscussNotify> tips;

        public void setCharacterValues(int characterValues) {
            this.characterValues = characterValues;
        }

        public void setCharacterPercent(double characterPercent) {
            this.characterPercent = characterPercent;
        }

        public void setTips(List<CooperationPraiseDiscussNotify> tips) {
            this.tips = tips;
        }

        public int getCharacterValues() {
            return characterValues;
        }

        public double getCharacterPercent() {
            return characterPercent;
        }

        public List<CooperationPraiseDiscussNotify> getTips() {
            return tips;
        }


    }
}
