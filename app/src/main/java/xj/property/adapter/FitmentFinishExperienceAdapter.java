package xj.property.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xj.property.R;
import xj.property.activity.fitmentfinish.FitMentFinishDetailsActivity;
import xj.property.beans.FitmentFinishCompanyData;
import xj.property.utils.CommonUtils;
import xj.property.utils.other.Config;

/**
 * Created by asia on 15/11/20.
 */
public class FitmentFinishExperienceAdapter extends BaseAdapter {

    private Context mContext;

    public List<FitmentFinishCompanyData> mlist;

    private DisplayImageOptions options;

    private View headerView;


    private WebView bangzhu_privilege_introduction_wv;
    private View ll_errorpage, ll_neterror;
    private WebSettings mWebSettings;
    private String loadingurl= Config.NET_BASE+"/jsp/app/fitment/fitment_plan.jsp";


    public FitmentFinishExperienceAdapter(Context mContext, List<FitmentFinishCompanyData> mlist){
        this.mContext = mContext;
        this.mlist = mlist;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_picture)
                .showImageForEmptyUri(R.drawable.default_picture)
                .showImageOnFail(R.drawable.default_picture)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    public void ChangeRefresh(List<FitmentFinishCompanyData> list){
        mlist.clear();
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    public void LoadMoreRefresh(List<FitmentFinishCompanyData> list){
        mlist.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null && position!=0) {
            holder = new ViewHolder();
            convertView = ((Activity) (mContext)).getLayoutInflater()
                    .inflate(R.layout.item_fitment_scheme, parent, false);
            holder.mIv_company = (ImageView) convertView.findViewById(R.id.iv_company);
            convertView.setTag(holder);
        } else if(convertView == null && position == 0){
            headerView = ((Activity) (mContext)).getLayoutInflater()
                    .inflate(R.layout.item_fitment_scheme_header, parent, false);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final FitmentFinishCompanyData fitmentFinish = mlist.get(position);

        if(position!=0){
            ImageLoader.getInstance().displayImage(fitmentFinish.getLogo(), holder.mIv_company, options);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FitMentFinishDetailsActivity.class);
                    intent.putExtra("decorationId",fitmentFinish.getDecorationId());
                    mContext.startActivity(intent);
                }
            });
        }else if(position == 0){
            bangzhu_privilege_introduction_wv = (WebView) headerView.findViewById(R.id.bangzhu_privilege_introduction_wv);
            ll_errorpage = headerView.findViewById(R.id.ll_errorpage);
            ll_neterror = headerView.findViewById(R.id.ll_neterror);
            headerView.findViewById(R.id.tv_getagain).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initData();
                }
            });
            initData();
            initWebView(bangzhu_privilege_introduction_wv);
        }

        if(position!=0){
            return convertView;
        }else if(position == 0){
            return headerView;
        }else {
            return convertView;
        }
    }

    private void initData() {
        if (TextUtils.isEmpty(loadingurl)) {
            if (bangzhu_privilege_introduction_wv != null) {
                bangzhu_privilege_introduction_wv.setVisibility(View.GONE);
            }
        } else {
            if (CommonUtils.isNetWorkConnected(mContext)) {
                if (bangzhu_privilege_introduction_wv != null) {
                    bangzhu_privilege_introduction_wv.loadUrl(loadingurl);
                }
            } else {
                setNetworkErrorView();
            }
        }
    }

    /**
     * 初始化WebView
     *
     * @param webView
     */
    public void initWebView(WebView webView) {
        // 设置支持JavaScript等
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        mWebSettings.setLightTouchEnabled(true);
        webView.setHapticFeedbackEnabled(false); //
        webView.setInitialScale(0); // 改变这个值可以设定初始大小 //重要,用于与页面交互!
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (ll_errorpage != null) {
                    ll_errorpage.setVisibility(View.GONE);
                }
                bangzhu_privilege_introduction_wv.setVisibility(View.VISIBLE);
                headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                ll_errorpage.setVisibility(View.VISIBLE);
                ll_neterror.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setNetworkErrorView() {
        if (ll_errorpage != null && ll_neterror != null) {
            ll_neterror.setVisibility(View.VISIBLE);
            ll_errorpage.setVisibility(View.VISIBLE);
        }
    }

    private static final class ViewHolder {
        private ImageView mIv_company;
    }

}
