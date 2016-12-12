package xj.property.beans;

/**
 * Created by Administrator on 2015/3/24.
 */
public class ZanRequest extends BaseBean {
    private String emobIdTo;
    private String lifeCircleId;

    public String getEmobIdTo() {
        return emobIdTo;
    }

    public void setEmobIdTo(String emobIdTo) {
        this.emobIdTo = emobIdTo;
    }

    public String getLifeCircleId() {
        return lifeCircleId;
    }

    public void setLifeCircleId(String lifeCircleId) {
        this.lifeCircleId = lifeCircleId;

    }

    @Override
    public String toString() {
        return "ZanRequest{" +
                "emobIdTo='" + emobIdTo + '\'' +
                ", lifeCircleId='" + lifeCircleId + '\'' +
                '}';
    }
}
