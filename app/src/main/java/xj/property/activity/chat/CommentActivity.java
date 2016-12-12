package xj.property.activity.chat;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.app.Activity;

import xj.property.R;

public class CommentActivity extends Activity {
    private ImageButton confirm;
    private ImageButton cancle;
    private RatingBar myRatingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);
        confirm=(ImageButton) findViewById(R.id.confirm);
        cancle=(ImageButton) findViewById(R.id.cancle);
        myRatingBar=(RatingBar) findViewById(R.id.myRatingBar);
        
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                //提交评价
                System.out.println("getRating--"+myRatingBar.getRating());
                
                finish();
            }
        });
        cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
