package xj.property.beans;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/24.
 */
public class DoorPasteIndexBean  implements Serializable{
    /**
     * doorId : 5
     * address : 1号楼4单元909
     * times : 2
     */
    private boolean isexample; /// 是不是例子
    private int doorId;
    private String address;
    private int times;
    private boolean isAddBlock; /// 是不是添加模块

    public void setDoorId(int doorId) {
        this.doorId = doorId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getDoorId() {
        return doorId;
    }

    public String getAddress() {
        return address;
    }

    public int getTimes() {
        return times;
    }

    public boolean isIsexample() {
        return isexample;
    }

    public void setIsexample(boolean isexample) {
        this.isexample = isexample;
    }

    public boolean isAddBlock() {
        return isAddBlock;
    }

    public void setAddBlock(boolean isAddBlock) {
        this.isAddBlock = isAddBlock;
    }
}
