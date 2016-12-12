package xj.property.beans;

/**
 * Created by Administrator on 2016/1/15.
 */
public class CurrentAppVersionReqBean   extends  BaseBean{

    private String equipment;

    public String getEquipmentVersion() {
        return equipmentVersion;
    }

    public void setEquipmentVersion(String equipmentVersion) {
        this.equipmentVersion = equipmentVersion;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    private String equipmentVersion;



}
