package xj.property.beans;

/**
 * Created by Administrator on 2015/5/21.
 */
public class AppLoginRequest extends BaseBean{
public String key;
public String emobId;
public String equipment;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

}
