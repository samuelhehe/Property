
package xj.property.wxapi;

import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;


/**
 * 该页面继承,包名禁止修改删除
 * 该页面用于微信分享回调Activity.
 */
public class WXEntryActivity extends WXCallbackActivity {
    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
    }

    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
        }
    }
}
