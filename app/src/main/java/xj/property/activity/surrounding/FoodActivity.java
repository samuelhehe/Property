package xj.property.activity.surrounding;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import xj.property.R;

/**
 * @author 张欣欣
 *
 * @date 2014-12-9 下午3:06:26
 */
public class FoodActivity extends Activity {
    private ListView lv_food;
    private String []names=new String[25];
    private int []imgs={R.drawable.widget01,R.drawable.widget02,R.drawable.widget04,R.drawable.widget03,R.drawable.widget06,R.drawable.widget07,R.drawable.widget08};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        lv_food=(ListView) findViewById(R.id.lv_food);
        
        for (int i = 0; i<names.length; i++) {
            if(i%2==0)
                names[i]="蛋炒饭"+i;
            else
                names[i]="饭炒蛋"+i;
        }
        
        lv_food.setAdapter(new MyAdapter(this));
        lv_food.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Toast.makeText(FoodActivity.this, "posi"+arg2, 0).show();
                Intent intent=new Intent(FoodActivity.this,FoodDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
    class MyAdapter extends BaseAdapter{
        private Context context;
        
        public MyAdapter(Context context) {
            super();
            this.context = context;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder; 
            if(convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(context, R.layout.item_listview_food, null);
                holder.img=(ImageView) convertView.findViewById(R.id.imageView1);
                holder.text=(TextView) convertView.findViewById(R.id.textView1);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder) convertView.getTag();
            }
            int i=position;
            if(position>=imgs.length){
                i=position%imgs.length;
            }
            holder.img.setImageResource(imgs[i]);
            holder.text.setText(names[position]);
            System.out.println("convertView"+convertView+"\t"+position);
            return convertView;
        }
        class ViewHolder {
            private ImageView img;
            private TextView text;
        }
    }
}
