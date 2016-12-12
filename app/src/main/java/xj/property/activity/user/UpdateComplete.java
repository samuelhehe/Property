package xj.property.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import xj.property.R;

/**
 * Created by Administrator on 2015/5/22.
 */
public class UpdateComplete extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=View.inflate(this, R.layout.activity_updatecomplete,null);
        setContentView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
