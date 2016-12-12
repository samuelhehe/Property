package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/4/10.
 */
public class ResultEaGroupInfo implements XJ {
    public String status;
    public Info info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public static class Info {
        private String groupName;
        private String maxValue;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(String maxValue) {
            this.maxValue = maxValue;
        }
    }


}
