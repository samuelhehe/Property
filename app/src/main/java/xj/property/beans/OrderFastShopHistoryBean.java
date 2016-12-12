package xj.property.beans;

import java.util.List;


/**
 * Created by Administrator on 2015/3/16.
 *
 * v3 2016/03/17
 *
 */
public class OrderFastShopHistoryBean implements XJ {

    private int page;
    private int limit;
    private List<HistoryFastShopBean> data;

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

    public List<HistoryFastShopBean> getData() {
        return data;
    }

    public void setData(List<HistoryFastShopBean> data) {
        this.data = data;
    }

}
