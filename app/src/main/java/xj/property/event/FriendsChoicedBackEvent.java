package xj.property.event;

import java.util.List;
import java.util.Map;

import xj.property.beans.UserGroupBean;
import xj.property.beans.UserGroupBeanForDel;

public class FriendsChoicedBackEvent {

    public static final int FLAG_MEMBERS_ADD = 1;

    public static final int FLAG_MEMBERS_DELETE = 2;



    public FriendsChoicedBackEvent() {
    }

    public FriendsChoicedBackEvent(int flag, List<String> emobIds) {
        this.flag = flag;
        this.emobIds = emobIds;
    }

    public FriendsChoicedBackEvent(int flag, Map<String, UserGroupBeanForDel> mapLists) {
        this.flag = flag;
        this.mapLists = mapLists;
    }

    private int flag ;

    private Map<String, UserGroupBeanForDel> mapLists;

    private List<String> emobIds;


    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Map<String, UserGroupBeanForDel> getMapLists() {
        return mapLists;
    }

    public void setMapLists(Map<String, UserGroupBeanForDel> mapLists) {
        this.mapLists = mapLists;
    }

    public List<String> getEmobIds() {
        return emobIds;
    }

    public void setEmobIds(List<String> emobIds) {
        this.emobIds = emobIds;
    }
}
