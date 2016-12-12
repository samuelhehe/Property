package xj.property.cache;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import xj.property.beans.XJ;

/**
 * Created by che on 2015/6/25.
 */
@Table(name = "search_history_cache")
public class SearchHistoryCache  extends Model implements XJ {
    @Column(name = "emobid")
    public   String emobid;
    @Column(name = "searchtype")
    public  int searchtype;
    @Column(name = "searchcontent")
    public  String searchcontent;

    public SearchHistoryCache() {
    }

    public SearchHistoryCache(String emobid, int searchtype, String searchcontent) {
        this.emobid = emobid;
        this.searchtype = searchtype;
        this.searchcontent = searchcontent;
    }
}
