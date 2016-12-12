package xj.property.netbasebean;

/**
 * Created by Administrator on 2016/2/26.
 */
public class BounsCoinInfoBean {

    /**
     * enable : 1
     * count : 100
     * ratio : 0.01
     */

    private int enable; ///// 1 开通，  0 默认未开通
    private int count;
    private float ratio;

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

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
}
