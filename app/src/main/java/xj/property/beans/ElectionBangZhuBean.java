package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/9/25.
 * v3 2016/03/15
 */
public class ElectionBangZhuBean {

    /**
     * fubangzhu : [{"userId":4500,"emobId":"793b3d0e968d2dfccac0bdbb7162564b","nickname":"hdhd","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt","characterValues":0,"grade":"fubangzhu"},{"userId":4509,"emobId":"8bd058d340c54007f0c60bd0759ec122","nickname":"大头","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt","characterValues":0,"grade":"fubangzhu"}]
     * bangzhu : [{"userId":1773,"emobId":"dfe46855383090ecb1f317faaad8c4fc","nickname":"wodemin","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FvQDu4kJd9_-pIY0FrByeM4oyipl","characterValues":15,"grade":"bangzhu"}]
     * daren : [{"userId":486,"emobId":"cea259be0e82d227ff7720619d08043a","nickname":"嘻嘻哈哈","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fm2Z2fOMuqvxy_4KXJRnb_ZJAH2y","characterValues":25,"grade":"zhanglao"},{"userId":1548,"emobId":"386d646e16927622d71bfe77f3e5b5e2","nickname":"我是914","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/FjrZ2DxU-tbAKIC4V3atnFaoraeP","characterValues":16,"grade":"zhanglao"},{"userId":4512,"emobId":"c37ce94085bbea46562a3ffaa17dc5a4","nickname":"吧唧吧唧","avatar":"http://7d9lcl.com2.z0.glb.qiniucdn.com/Fjk1cqP2gezuV6GS2Wd-AIrbvFVt","characterValues":1000,"grade":"zhanglao"}]
     */

    private List<FubangzhuEntity> fubangzhu;
    private List<BangzhuEntity> bangzhu;
    private List<DarenEntity> daren;

    public void setFubangzhu(List<FubangzhuEntity> fubangzhu) {
        this.fubangzhu = fubangzhu;
    }

    public void setBangzhu(List<BangzhuEntity> bangzhu) {
        this.bangzhu = bangzhu;
    }


    public List<FubangzhuEntity> getFubangzhu() {
        return fubangzhu;
    }

    public List<BangzhuEntity> getBangzhu() {
        return bangzhu;
    }

    public List<DarenEntity> getDaren() {
        return daren;
    }

    public void setDaren(List<DarenEntity> daren) {
        this.daren = daren;
    }

    public static class FubangzhuEntity {

        /**
         *
         *
         * "userId": {用户数据ID},
         "emobId": "{用户环信ID}",
         "nickname": "{用户昵称}",
         "avatar": "{用户头像}",
         "characterValues": "{用户人品值}",
         "grade": "{用户帮主身份}",
         "identity": "{用户牛人身份}"
         *
         */
        private int userId;
        private String emobId;
        private String nickname;
        private String avatar;
        private int characterValues;
        private String grade;
        private String identity;

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setCharacterValues(int characterValues) {
            this.characterValues = characterValues;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public int getUserId() {
            return userId;
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

        public int getCharacterValues() {
            return characterValues;
        }

        public String getGrade() {
            return grade;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }
    }

    public static class BangzhuEntity {
        /**
         * userId : 1773
         * emobId : dfe46855383090ecb1f317faaad8c4fc
         * nickname : wodemin
         * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/FvQDu4kJd9_-pIY0FrByeM4oyipl
         * characterValues : 15
         * grade : bangzhu
         */

        private int userId;
        private String emobId;
        private String nickname;
        private String avatar;
        private int characterValues;
        private String grade;
        private String identity;

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setCharacterValues(int characterValues) {
            this.characterValues = characterValues;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public int getUserId() {
            return userId;
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

        public int getCharacterValues() {
            return characterValues;
        }

        public String getGrade() {
            return grade;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }
    }

    public static class DarenEntity {
        /**
         * userId : 486
         * emobId : cea259be0e82d227ff7720619d08043a
         * nickname : 嘻嘻哈哈
         * avatar : http://7d9lcl.com2.z0.glb.qiniucdn.com/Fm2Z2fOMuqvxy_4KXJRnb_ZJAH2y
         * characterValues : 25
         * grade : zhanglao
         */

        private int userId;
        private String emobId;
        private String nickname;
        private String avatar;
        private int characterValues;
        private String grade;
        private String identity;

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setEmobId(String emobId) {
            this.emobId = emobId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setCharacterValues(int characterValues) {
            this.characterValues = characterValues;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public int getUserId() {
            return userId;
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

        public int getCharacterValues() {
            return characterValues;
        }

        public String getGrade() {
            return grade;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }
    }

}
