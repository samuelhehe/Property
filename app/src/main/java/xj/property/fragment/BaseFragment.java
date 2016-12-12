package xj.property.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import xj.property.R;
import xj.property.utils.ToastUtils;
import xj.property.widget.LoadingDialog;

/**
 * Created by Administrator on 2015/3/13.
 */
public class BaseFragment extends Fragment {

    protected LoadingDialog mLdDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitDialog();
    }

    private void InitDialog() {
        mLdDialog = new LoadingDialog(getActivity());
        // mLdDialog.setMessage(this.getString(R.string.dialog_loading_msg));
        mLdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                dialog.cancel();
                return false;
            }
        });
    }


    protected void showToast(int strid) {
//        Toast.makeText(getActivity(), strid, Toast.LENGTH_SHORT).show();
        ToastUtils.showToast(getActivity(),strid);
    }

    protected void showToastLong(int strid) {
        Toast.makeText(getActivity(), strid, Toast.LENGTH_LONG).show();
    }

    protected void showToast(String str) {
//        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        ToastUtils.showToast(getActivity(),str);
    }

    protected void showNetErrorToast() {
        Toast.makeText(getActivity(), this.getResources().getString(R.string.netError), Toast.LENGTH_SHORT).show();
    }
}
