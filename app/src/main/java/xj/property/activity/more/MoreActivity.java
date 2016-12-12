package xj.property.activity.more;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import java.util.ArrayList;

import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.adapter.GridViewAdapter;
import xj.property.beans.IndexBean;

public class MoreActivity extends HXBaseActivity implements AdapterView.OnItemClickListener {
    /**
     * logger
     */
    private GridView gv_more;
    private GridViewAdapter gridViewAdapter;
    private  String[] names = {"宽带","开锁","洗衣店","五金店"};
    private String[] pic={"more_home_internet","more_home_unlocking","more_home_metals","more_home_wash"};
    ArrayList<IndexBean> beans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        initTitle(null, "更多", "");
        beans = new ArrayList<IndexBean>();
        for (int i = 0; i < names.length; i++) {
            IndexBean bean = new IndexBean();
            bean.setServiceName(names[i]);
            bean.setImgName(pic[i]);
            beans.add(bean);
        }
        setUpViews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUpViews() {
        gv_more = (GridView) findViewById(R.id.gv_more);
        gridViewAdapter = new GridViewAdapter(this, beans);
        gv_more.setAdapter(gridViewAdapter);
        gv_more.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View v) {

    }
}

