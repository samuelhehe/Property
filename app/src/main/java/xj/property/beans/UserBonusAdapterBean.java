package xj.property.beans;

import java.util.ArrayList;

/**
 * Created by n on 2015/4/9.
 */
public class UserBonusAdapterBean implements XJ {
    private String bonusStatusStr;
    private ArrayList<UserBonusBean> childList;

    public String getBonusStatusStr() {
        return bonusStatusStr;
    }

    public void setBonusStatusStr(String bonusStatusStr) {
        this.bonusStatusStr = bonusStatusStr;
    }

    public ArrayList<UserBonusBean> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<UserBonusBean> childList) {
        this.childList = childList;
    }

    @Override
    public String toString() {
        return "UserBonusAdapterBean{" +
                "bonusStatusStr='" + bonusStatusStr + '\'' +
                ", childList=" + childList +
                '}';
    }
}
