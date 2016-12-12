package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 */
public class SearchUserResultRespBean {

    private String emobId;
    private String nickname;
    private String avatar;
    private String grade;
    private String identity;//用户牛人身份
    public boolean isChecked = false;

    /*
    {
     "emobId": "{用户环信ID}",
     "nickname": "{用户昵称}",
     "avatar": "{用户头像}",
     "grade": "{用户帮主身份}",
     "identity": "{用户牛人身份}"
     }
     */

    private List<LabelsEntity> labels;

    public void setEmobId(String emobId) {
        this.emobId = emobId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setLabels(List<LabelsEntity> labels) {
        this.labels = labels;
    }

    public String getEmobId() {
        return emobId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getGrade() {
        return grade;
    }

    public List<LabelsEntity> getLabels() {
        return labels;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public static class LabelsEntity {


        /**
         *
         *  "labels": [{
         "labelContent": "{标签}",
         "count": {邻居被贴此标签的次数},
         "match": "{是否和当前用户的标签匹配：yes->匹配，no->不匹配}"
         }]
         */

        /**
         * labelContent : 万能大妈
         * count : 1
         */

        private String labelContent;
        private String count;
        private String match;

        public void setLabelContent(String labelContent) {
            this.labelContent = labelContent;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getLabelContent() {
            return labelContent;
        }

        public String getCount() {
            return count;
        }

        public String getMatch() {
            return match;
        }

        public void setMatch(String match) {
            this.match = match;
        }
    }
}
