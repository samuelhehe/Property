package xj.property.beans;

import xj.property.utils.other.Config;

/**
 * Created by Administrator on 2015/6/2.
 */
public class TouristRequest extends xj.property.netbase.BaseBean {
    public String equipment;
    public String equipmentVersion;

    public TouristRequest() {
        super();
        if (Config.PHONETYPE.equals(android.os.Build.MANUFACTURER)) {
            equipment = "mi";
        } else {
            equipment = "android";
        }

    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getEquipmentVersion() {
        return equipmentVersion;
    }

    public void setEquipmentVersion(String equipmentVersion) {
        this.equipmentVersion = equipmentVersion;
    }
}
