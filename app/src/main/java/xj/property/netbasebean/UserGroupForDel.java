package xj.property.netbasebean;

import java.util.List;

import xj.property.beans.UserGroupBeanForDel;

/**
 * Created by <a> samuelnotes <a href="http://www.samuelnotes.cn">.
 * packageName: xj.property.netbasebean
 * date: 2016/4/5
 * company： xiaojian
 * action:
 */
public class UserGroupForDel {


    private int page;

    private int limit;

    private List<UserGroupBeanForDel> data;

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

    public List<UserGroupBeanForDel> getData() {
        return data;
    }

    public void setData(List<UserGroupBeanForDel> data) {
        this.data = data;
    }
}
