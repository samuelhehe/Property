package xj.property.utils.other;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by n on 2015/4/28.
 */
public class MaxLengthWatcher implements TextWatcher {
    Context context;
    private int maxLen = 0;
    private EditText editText = null;
    private String msg;

    public MaxLengthWatcher(Context context,int maxLen, EditText editText,String msg) {
        this.context=context;
        this.maxLen = maxLen;
        this.editText = editText;
        this.msg=msg;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Editable editable = editText.getText();
        int len = editable.length();
        if(len > maxLen)
        {
            int selEndIndex = Selection.getSelectionEnd(editable);
            String str = editable.toString().trim();
            //截取新字符串
            String newStr = str.substring(0,maxLen);
            editText.setText(newStr);
            editable = editText.getText();

            //新字符串的长度
            int newLen = editable.length();
            //旧光标位置超过字符串长度
            if(selEndIndex > newLen)
            {
                selEndIndex = editable.length();
            }
            //设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
            Toast toast=Toast.makeText(context,msg,Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
