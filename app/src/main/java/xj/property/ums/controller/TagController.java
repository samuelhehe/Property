package xj.property.ums.controller;

import android.content.Context;
import android.os.Handler;
import android.util.Log;


import org.json.JSONObject;

import xj.property.beans.MyMessage;
import xj.property.ums.common.AssembJSONObj;
import xj.property.ums.common.CommonUtil;
import xj.property.ums.common.NetworkUitlity;
import xj.property.ums.common.UmsConstants;
import xj.property.ums.dao.JSONParser;
import xj.property.ums.dao.Poster;

public class TagController {
    
    static final private String POSTURL = UmsConstants.preUrl + UmsConstants.tagUser;
    
    static public void PostTag(Context context,String tags,Handler handler) {
        JSONObject object = AssembJSONObj.getpostTagsJSONObj(Poster.GenerateTagObj(context, tags));
        
        if (1 == CommonUtil.getReportPolicyMode(context)
                && CommonUtil.isNetworkAvailable(context)) {

            String result = NetworkUitlity.Post(
                    POSTURL,
                    object.toString());
            
            MyMessage message = JSONParser.parse(result);
            
            if (!message.isFlag()) {
                CommonUtil.saveInfoToFile(handler,"tags", object, context);
            }
        } else {
            CommonUtil.saveInfoToFile(handler,"tags", object, context);
        }
    }

}
