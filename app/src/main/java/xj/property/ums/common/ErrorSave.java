package xj.property.ums.common;

import android.content.Context;
import android.os.Environment;

import org.json.JSONObject;

import java.io.File;

import xj.property.XjApplication;
import xj.property.beans.ErrorBean;
import xj.property.utils.image.utils.FileHelper;
import xj.property.utils.image.utils.StrUtils;

/**
 * Created by Administrator on 2015/5/25.
 */
public class ErrorSave extends Thread {

    ErrorBean errorBean;
    String errorInfo;
    public ErrorSave(ErrorBean errorBean) {
        super();
        this.errorBean = errorBean;
    }
    public ErrorSave(String errorInfo) {
        super();
        this.errorInfo = errorInfo;
    }
    @Override
    public void run() {
        super.run();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Error"+ XjApplication.getInstance().getPackageName());
        if(errorBean!=null)
        FileHelper.writeFile(file.getAbsolutePath(),errorBean.toString(),true);
        if(errorInfo!=null)
            FileHelper.writeFile(file.getAbsolutePath(),errorInfo,true);
    }
}
