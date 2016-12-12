package xj.property.beans;

import java.util.List;

/**
 * 作者：asia on 2015/12/30 18:55
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * v3 2016/03/18
 */
public class ChatSameInfoBean {
    /**
     *  "page": {页码},
     "limit": {页面大小},
     "data": [{
     "emobId": "{邻居的环信ID}",
     "nickname": "{邻居的昵称}",
     "avatar": "{邻居的头像}",
     "grade": "{邻居的帮主身份}",
     "identity": "{邻居的牛人身份}",
     "labels": [{
     "labelContent": "{标签}",
     "count": {邻居被贴此标签的次数},
     "match": "{是否和当前用户的标签匹配：yes->匹配，no->不匹配}"
     }]
     }]
     *
     *
     *
     */


    private int page;
    private int limit;
    private List<ChatSameDataBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<ChatSameDataBean> getData() {
        return data;
    }

    public void setData(List<ChatSameDataBean> data) {
        this.data = data;
    }
}
