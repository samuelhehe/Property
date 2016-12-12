package xj.property.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.ArrayList;
import java.util.List;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.NotifyListAdapter;
import xj.property.cache.XJNotify;
import xj.property.utils.CommonUtils;
import xj.property.utils.image.utils.Config;
import xj.property.utils.other.PreferencesUtil;

/**
 * Created by Administrator on 2015/4/1.
 */
public class NotifyListActivity extends HXBaseActivity {
    ListView lvNotifyList;
    List<XJNotify> notifyList;
    NotifyListAdapter adapter;
    private View view;
    private ImageView iv_footer;

    private LinearLayout ll_errorpage;
    private LinearLayout ll_nomessage;
    private LinearLayout ll_neterror;
    private TextView tv_getagain;
    private boolean isallRead;
    private String emobId;
    List<XJNotify> notifyListtemmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_list);
        initTitle(null, "通知中心", "全部已读");
        //  PushManager.getInstance().getClientid(this);

//        emobId= PreferencesUtil.getLoginInfo(NotifyListActivity.this).getEmobId();
//        if (emobId==null) emobId="";

        notifyList = new Select().from(XJNotify.class).where("emobid = ?", PreferencesUtil.getLogin(this) ? PreferencesUtil.getLoginInfo(this).getEmobId() : "-1").orderBy("timestamp DESC").execute();

//        notifyList = getNotifyList();
        lvNotifyList = (ListView) findViewById(R.id.lv_notifylist);
        view = View.inflate(this, R.layout.item_notice_footer, null);
        iv_footer = (ImageView) findViewById(R.id.iv_footer);
        ll_neterror = (LinearLayout) findViewById(R.id.ll_neterror);
        tv_getagain = (TextView) findViewById(R.id.tv_getagain);
        ll_nomessage = (LinearLayout) findViewById(R.id.ll_nomessage);
        ll_errorpage = (LinearLayout) findViewById(R.id.ll_errorpage);
        if (!CommonUtils.isNetWorkConnected(this)) {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.VISIBLE);
            ll_nomessage.setVisibility(View.GONE);
            tv_getagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CommonUtils.isNetWorkConnected(NotifyListActivity.this)) {
                        return;
                    } else {
                        ll_errorpage.setVisibility(View.GONE);
                        initListView();
                    }
                }
            });
        } else {
            ll_errorpage.setVisibility(View.GONE);
            initListView();
        }


    }
/*------------------------------------------------------------------------------------------------------------------------------*/

    String[] title = new String[]{
            "啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦",
            "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊",
            "嗯嗯嗯嗯嗯嗯嗯嗯嗯嗯嗯嗯嗯嗯嗯嗯嗯嗯嗯",
            "哈哈哈哈哈哈哈哈哈",
            "咦咦咦咦咦咦咦咦咦咦咦咦咦咦咦",
            "哦哦哦哦哦哦哦哦哦哦哦哦哦哦",
            "噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢噢",
            "喔喔喔喔喔喔喔喔喔喔喔喔喔喔喔喔喔喔",
            "哎哎哎哎哎哎哎哎哎哎哎哎哎哎哎哎哎哎哎",
            "唉唉唉唉唉唉唉唉唉唉唉唉唉唉唉唉唉唉唉唉唉"
    };

    // 零时数据
    private List<XJNotify> getNotifyList() {
        List<XJNotify> notifyList = new ArrayList<>();

        int timeStamp = (int) (System.currentTimeMillis() / 1000L);
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                XJNotify xjNotify = new XJNotify(1045 + i, "qazwsxedc" + i, 100, title[i],
                        "本文版权归作者和博客园共有，欢迎转载，但未经作者同意必须保留此段声明，且在文章页面明显位置给出原文连接，否则保留追究法律责任的权利。" + i,
                        timeStamp + i, true, "yes");
                notifyList.add(xjNotify);

            } else {
                XJNotify xjNotify = new XJNotify(1445 + i, "qazwsxedc" + i, 100, title[i],
                        "本文版权归作者和博客园共有，欢迎转载，但未经作者同意必须保留此段声明，且在文章页面明显位置给出原文连接，否则保留追究法律责任的权利。" + i,
                        timeStamp + i, false, "no");
                notifyList.add(xjNotify);
            }

        }
        return notifyList;

    }

/*------------------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onResume() {
        super.onResume();
        initListView();
    }

    private void initListView() {

        notifyListtemmp = new Select().from(XJNotify.class).where("read_status = ?", "no").execute();
        if (notifyListtemmp != null && notifyListtemmp.size() != 0) {
            tvRight.setText("一键全读");
            tvRight.setVisibility(View.VISIBLE);
            isallRead = false;
        } else {
            tvRight.setText("全部已读");
            isallRead = true;
        }

        if (notifyList.size() == 0) {
            // lvNotifyList.removeFooterView(view);
            iv_footer.setVisibility(View.GONE);
        } else {
            // lvNotifyList.addFooterView(view);
            iv_footer.setVisibility(View.VISIBLE);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new NotifyListAdapter(notifyList, this);
            lvNotifyList.setAdapter(adapter);
        }
        if (notifyList.size() != 0) {
            ll_errorpage.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } else {
            ll_errorpage.setVisibility(View.VISIBLE);
            ll_neterror.setVisibility(View.GONE);
            ll_nomessage.setVisibility(View.VISIBLE);
        }

        lvNotifyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XJNotify notify = notifyList.get(position);
                notify.setRead_status("yes");
                // notify.setHasReaded("yes");
                notify.save();
//                if (notify.setHasReaded("yes")) {
//                    notify.isReaded = true;
//                    notify.save();
//                }
                Intent it = new Intent(NotifyListActivity.this, NotifyContentActivity.class);
                it.putExtra(Config.INTENT_PARMAS1, notify);
                startActivity(it);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_text:
                if (lvNotifyList != null && isallRead == false) {
                    new Update(XJNotify.class).set("read_status = ?", "yes").execute();
//                    for (int i=0;i<notifyList.size();i++){
//                        notifyList.get(i).isReaded=true;
//                        notifyList.get(i).save();
//                    }
                    tvRight.setText("全部已读");
                    notifyList = new Select().from(XJNotify.class).orderBy("timestamp DESC").execute();
                    adapter.notifyDataSetChanged();
                }
                break;
        }

    }
}
