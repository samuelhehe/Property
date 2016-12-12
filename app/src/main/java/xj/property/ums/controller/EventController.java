
package xj.property.ums.controller;

import android.content.Context;
import android.os.Handler;


import org.json.JSONObject;

import xj.property.beans.MyMessage;
import xj.property.beans.PostObjEvent;
import xj.property.ums.common.AssembJSONObj;
import xj.property.ums.common.CommonUtil;
import xj.property.ums.common.NetworkUitlity;
import xj.property.ums.common.UmsConstants;
import xj.property.ums.dao.JSONParser;

public class EventController {
    static final String EVENTURL = UmsConstants.preUrl + UmsConstants.eventUrl;
    public static boolean postEventInfo(Handler handler, Context context, PostObjEvent event) {
        try {
            if (!event.verification())
            {
                CommonUtil.printLog("UMSAgent", "Illegal value of acc in postEventInfo");
                return false;
            }

            JSONObject localJSONObject = AssembJSONObj.getEventJOSNobj(event);

            if (1 == CommonUtil.getReportPolicyMode(context)
                    && CommonUtil.isNetworkAvailable(context)) {
                try {
                    String reslut = NetworkUitlity.Post(EVENTURL, localJSONObject.toString());
                    MyMessage info = JSONParser.parse(reslut);
                    if (!info.isFlag()) {
                        CommonUtil.saveInfoToFile(handler, "eventInfo", localJSONObject, context);
                        return false;
                    }
                } catch (Exception e) {
                    CommonUtil.printLog("UmsAgent", "fail to post eventContent");
                }
            } else {

                CommonUtil.saveInfoToFile(handler, "eventInfo", localJSONObject, context);
                return false;
            }
        } catch (Exception e) {
            CommonUtil.printLog("UMSAgent", "Exception occurred in postEventInfo()");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
