package xj.property.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;

import xj.property.utils.TimeUtils;
import xj.property.utils.image.utils.StrUtils;

/**
 * Created by maxwell on 15/2/9.
 */

@SuppressLint("ValidFragment")
public class DataPickerFragmentSingleButtonDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private OnDateSetCallBack listener;
    private DatePickerDialog dataPickerDialog;
    private int resultYear;
    private int resultMonth;
    private int resultDay;
    private boolean hasSet = false;


    /**
     * constructor
     */
    public DataPickerFragmentSingleButtonDialog(OnDateSetCallBack listener)
    {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        if (!hasSet)
        {
            dataPickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            // dataPickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
            // "选定日期",
            // new DialogInterface.OnClickListener() {
            // public void onClick(DialogInterface dialog, int which) {
            // if (which == DialogInterface.BUTTON_NEGATIVE) {
            // logger.info("BUTTON_NEGATIVE click");
            // if((getTag().equals("search"))||(getTag().equals("customize"))){
            // listener.onDateSetCallBack(year,month,day,getTag());
            // }else
            // if((getTag().equals("PublishBasicInfo"))||(getTag().equals("PubSliderFragment"))){
            // listener.onDateSetCallBack(year,month,day,getTag());
            // }
            // dataPickerDialog.dismiss();
            // }
            // }
            // });
            // return new DatePickerDialog(getActivity(), this, year, month,
            // day);
            return dataPickerDialog;
        } else
        {
            dataPickerDialog = new DatePickerDialog(getActivity(), this, resultYear, resultMonth, resultDay);
            return dataPickerDialog;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        resultDay = day;
        resultMonth = month;
        resultYear = year;
        String formatDate = year + "-" + (month+1) + "-" + day + " " + "23:59:00";
       int  activityTime = TimeUtils.fromDateStringToInt(formatDate);
        long currentTime=new Date().getTime()/1000;
if(currentTime<activityTime)
        hasSet = true;
        else {
    Toast.makeText(dataPickerDialog.getContext(),"您不能穿越,请重新设置",Toast.LENGTH_SHORT).show();
    return;
}

        // Do something with the date chosen by the user
        listener.onDateSetCallBack(year, month, day, getTag());
    }


    public interface OnDateSetCallBack {
        public void onDateSetCallBack(int year, int month, int day, String tag);
    }
}
