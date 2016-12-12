package xj.property.beans;

import java.util.List;

/**
 * Created by Administrator on 2015/6/10.
 * v3 2016/03/04
 */
public class CircleListRespone {

    public int page;
    public int limit;

    public List<LifeCircleBean> data;
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

    public List<LifeCircleBean> getData() {
        return data;
    }

    public void setData(List<LifeCircleBean> data) {
        this.data = data;
    }



}
