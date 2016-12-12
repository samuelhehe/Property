package xj.property.beans;

import java.util.List;

/**
 * Created by maxwell on 15/1/8.
 * v3 2016/02/29
 */
public class ActivitiesBean {

    private int page;

    private int limit;

    private List<ActivityBean> data;

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

    public List<ActivityBean> getData() {
        return data;
    }

    public void setData(List<ActivityBean> data) {
        this.data = data;
    }
}
