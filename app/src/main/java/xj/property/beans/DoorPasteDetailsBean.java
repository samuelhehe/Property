package xj.property.beans;

/**
 * Created by Administrator on 2016/3/24.
 */
public class DoorPasteDetailsBean {

    /**
     * doorId : 1
     * doorStickerId : 1
     * content : 老把车停单元楼门口，进出都不方面
     * times : 1
     * createTime : 1458735796
     */
    private int doorId;
    private int doorStickerId;
    private String content;
    private int times;
    private int createTime;

    public void setDoorId(int doorId) {
        this.doorId = doorId;
    }

    public void setDoorStickerId(int doorStickerId) {
        this.doorStickerId = doorStickerId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getDoorId() {
        return doorId;
    }

    public int getDoorStickerId() {
        return doorStickerId;
    }

    public String getContent() {
        return content;
    }

    public int getTimes() {
        return times;
    }

    public int getCreateTime() {
        return createTime;
    }
}
