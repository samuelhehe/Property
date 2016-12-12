package xj.property.beans;

import java.util.List;

/**
 * 作者：asia on 2015/12/11 18:04
 * ********************************************
 * 公司：北京小间科技发展有限公司
 * ********************************************
 * 界面功能：
 */
public class GeniusInfoBean {


    private int limit;
    private int page;

    private List<GeniusBean> data;

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

    public List<GeniusBean> getData() {
        return data;
    }

    public void setData(List<GeniusBean> data) {
        this.data = data;
    }
}
