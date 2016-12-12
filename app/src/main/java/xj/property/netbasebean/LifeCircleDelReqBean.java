package xj.property.netbasebean;

import xj.property.netbase.BaseBean;

/**
 * Created by Administrator on 2016/3/4.
 * v3
 */
public class  LifeCircleDelReqBean extends  BaseBean {
    /**
     * lifeCircleId : 1
     * emobId : d463b16dfc014466a1e441dd685ba505
     */

    /**
     *
     * {
     "lifeCircleId": {生活圈ID},
     "emobId": "{生活圈创建者环信ID}"
     }
     */

    private int lifeCircleId;
    private String emobId;

    public void setLifeCircleId(int lifeCircleId) {
        this.lifeCircleId = lifeCircleId;
    }

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public int getLifeCircleId() {
        return lifeCircleId;
    }

    public String getEmobId() {
        return emobId;
    }






}
