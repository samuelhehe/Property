package xj.property.activity.surrounding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import xj.property.R;
import xj.property.activity.HXBaseActivity.HXBaseActivity;
import xj.property.beans.PanicBuyingRequestBean;
import xj.property.beans.PanicBuyingResponseBean;
import xj.property.utils.QRCodeUtil;
import xj.property.utils.image.utils.StrUtils;
import xj.property.utils.message.XJContactHelper;
import xj.property.utils.message.XJMessageHelper;
import xj.property.utils.other.Config;
import xj.property.utils.other.PreferencesUtil;
import xj.property.widget.LoadingDialog;

public class PanicBuyingDetailActivity extends HXBaseActivity {

    private LoadingDialog loadingDialog;
    private LinearLayout ll_buy_failure, ll_buy_success;
    private int crazySalesId, count;
    private String emobId, qrCode;
    private ImageView iv_back, iv_back_to;
    private TextView tv_title, tv_description,fail_text;
    private ListView lv_list_view;
    private Button btn_confirm;
    private List<String> codeList;
    private String logo;

    private LinearLayout headerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_buying_detail);
        loadingDialog = new LoadingDialog(PanicBuyingDetailActivity.this);
        codeList = new ArrayList<>();
        crazySalesId = getIntent().getIntExtra("crazySalesId", -1);
        count = getIntent().getIntExtra("count", -1);
        emobId = getIntent().getStringExtra("emobId");
        logo = getIntent().getStringExtra("logo");
        initView();
        panicBuying();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back_to = (ImageView) findViewById(R.id.iv_back_to);
        tv_description = (TextView) findViewById(R.id.tv_description);
        fail_text = (TextView) findViewById(R.id.fail_text);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        ll_buy_failure = (LinearLayout) findViewById(R.id.ll_buy_failure);
        ll_buy_success = (LinearLayout) findViewById(R.id.ll_buy_success);
        lv_list_view = (ListView) findViewById(R.id.lv_list_view);
        headerView = (LinearLayout) View.inflate(this, R.layout.panic_buying_success_headerview, null);
        tv_title = (TextView) headerView.findViewById(R.id.tv_title);
        lv_list_view.addHeaderView(headerView);
        TextPaint tp = tv_title.getPaint();
        tp.setFakeBoldText(true);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    interface PanicBuyingService {
        @POST("/api/v1/crazysales/{communityId}/user")
        void getPanicBuyingInfo(@Header("signature") String signature, @Body PanicBuyingRequestBean requestBean, @Path("communityId") int communityId, Callback<PanicBuyingResponseBean> callback);
    }

    private void panicBuying() {
        loadingDialog.show();
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.NET_BASE).build();
        PanicBuyingService service = restAdapter.create(PanicBuyingService.class);
        PanicBuyingRequestBean requestBean = new PanicBuyingRequestBean();
        requestBean.setCount(count);
        requestBean.setCrazySalesId(crazySalesId);
        requestBean.setUserEmobId(emobId);
        Callback<PanicBuyingResponseBean> callback = new Callback<PanicBuyingResponseBean>() {
            @Override
            public void success(final PanicBuyingResponseBean panicBuyingResponseBean, Response response) {
                loadingDialog.dismiss();
                Log.i("debbug", "抢购成功=");
                if (panicBuyingResponseBean != null && panicBuyingResponseBean.getInfo()!=null && "yes".equals(panicBuyingResponseBean.getStatus())) {

                    //抢购成功
                    Log.i("chenggong", "成功");
                    ll_buy_success.setVisibility(View.VISIBLE);
                    qrCode = panicBuyingResponseBean.getInfo().getCrazySalesUser().get(0).getCode();
                    tv_title.setText("" + panicBuyingResponseBean.getInfo().getTitle());
                    for (int i = 0; i < panicBuyingResponseBean.getInfo().getCrazySalesUser().size(); i++) {
                        codeList.add(panicBuyingResponseBean.getInfo().getCrazySalesUser().get(i).getCode());
                    }
                    QRCodeAdapter qrCodeAdapter = new QRCodeAdapter();
                    lv_list_view.setAdapter(qrCodeAdapter);
                    //抢购成功，发送到周边消息

//                    //保存联系人信息
//                    XJContactHelper.saveContact(panicBuyingResponseBean.getInfo().getEmobId(),"周边店家","drawable://"+R.drawable.shop_face,"100");

                    //创建消息
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(1);
                            for (int i = 0; i < panicBuyingResponseBean.getInfo().getCrazySalesUser().size(); i++) {
                                Log.i("debbug", "iii=" + i);
                                final EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                                TextMessageBody txtBody = new TextMessageBody("周边消息");
                                message.addBody(txtBody);
                                message.setFrom(panicBuyingResponseBean.getInfo().getEmobId());
                                message.setTo(PreferencesUtil.getLoginInfo(PanicBuyingDetailActivity.this).getEmobId());
                                message.setAttribute("avatar", logo);
                                message.setAttribute("nickname", panicBuyingResponseBean.getInfo().getShopName());
                                message.setAttribute("CMD_CODE", 500);
                                message.setAttribute("clickable", 1);
                                message.setAttribute("isShowAvatar", 1);
                                message.setAttribute("title", panicBuyingResponseBean.getInfo().getTitle());
                                message.setAttribute("code", "" + panicBuyingResponseBean.getInfo().getCrazySalesUser().get(i).getCode());
                                message.setAttribute(Config.EXPKey_SORT, "13");
//                        EMChatManager.getInstance().saveMessage(message,true);
                                EMChatManager.getInstance().importMessage(message, true);
                                XJContactHelper.saveContact(message);
                                XJMessageHelper.saveMessage2DB(message.getMsgId(), message.getStringAttribute("code", ""), 500);
                            }
                            handler.sendEmptyMessage(2);
                        }
                    }).start();

//                    finish();

                } else if ("no".equals(panicBuyingResponseBean.getStatus())) {
//                    Log.i("failure", "失败");
//                    ll_buy_failure.setVisibility(View.VISIBLE);
//                    if ("sellOutError".equals(panicBuyingResponseBean.getErrorCode())) {
//                        //剩余数量为0
//                        Log.i("911911", "抢购商品已被抢光了，看看别的吧！");
//                        fail_text.setText("您慢了一步！");
//                        tv_description.setText("抢购商品已被抢光了，看看别的吧！");
//                        btn_confirm.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                finish();
//                            }
//                        });
//                    } else if ("stockError".equals(panicBuyingResponseBean.getErrorCode())) {
//                        //剩余数量小于请求抢购数量
//                        Log.i("911911", "请返回修改抢购数量！");
//                        fail_text.setText("您慢了一步！");
//                        tv_description.setText("抢购商品只剩" + panicBuyingResponseBean.getInfo().getRemain() + "个了，请返回修改抢购数量！");
//                        btn_confirm.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                finish();
//                            }
//                        });
//                    }else if("onlyOneError".equals(panicBuyingResponseBean.getErrorCode())){
//                        tv_description.setText("您只能抢购一次！");
//                    }
                    ll_buy_failure.setVisibility(View.VISIBLE);
                    tv_description.setText(""+panicBuyingResponseBean.getMessage());
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }

            }

            @Override
            public void failure(RetrofitError error) {
                loadingDialog.dismiss();
                showNetErrorToast();
                finish();
            }
        };
        service.getPanicBuyingInfo(StrUtils.string2md5(Config.BANGBANG_TAG + StrUtils.dataHeader(requestBean)), requestBean, PreferencesUtil.getCommityId(PanicBuyingDetailActivity.this), callback);
    }


    @Override
    public void onClick(View v) {

    }

    public class QRCodeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return codeList.size();
        }

        @Override
        public Object getItem(int position) {
            return codeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(PanicBuyingDetailActivity.this, R.layout.qrcode_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_code.setText("抢购码：" + codeList.get(position));
            viewHolder.iv_qrcode.setImageBitmap(QRCodeUtil.generateCode(Long.parseLong(codeList.get(position))));
            return convertView;
        }
    }

    class ViewHolder {
        private TextView tv_code;
        private ImageView iv_qrcode;

        ViewHolder(View view) {
            tv_code = (TextView) view.findViewById(R.id.tv_code);
            iv_qrcode = (ImageView) view.findViewById(R.id.iv_qrcode);
            view.setTag(this);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mLdDialog.show();
                    break;
                case 2:
                    mLdDialog.dismiss();
                    break;
            }
        }
    };
}