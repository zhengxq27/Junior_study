package com.study.zhengxq27.healthfoodlist;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class foodDetail extends AppCompatActivity {

    private String[] operations = { "分享信息","不敢兴趣","查看更多信息","出错反馈",""};
    private String Star_situation = "empty";
    private item Item;
    private boolean is_putInto_collect = false;
    private int ADD_CODE = 2;
    private int NOTADD_CODE = 3;
    private Bundle bundle;
    public String circle_name;
    public String food_name;
    public String food_kind;
    public String element;
    public String title_color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        ListView listView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview_item , operations);
        listView.setAdapter(arrayAdapter);

        Intent intent = this.getIntent();
         bundle = intent.getExtras();

         circle_name = bundle.getString("circle_name");
         food_name = bundle.getString("food_name");
         food_kind = bundle.getString("food_kind");
         element = bundle.getString("element");
        String Element = "富含 "+ element;
         title_color = bundle.getString("title_color");
        Item = new item(circle_name,food_name,food_kind,element,title_color);

        RelativeLayout title = (RelativeLayout)findViewById(R.id.title);
        title.setBackgroundColor(Color.parseColor(title_color));
        ImageButton back_button = (ImageButton)findViewById(R.id.back_button);
        back_button.setBackgroundColor(Color.parseColor(title_color));

        TextView food_name1 = (TextView)findViewById(R.id.food_name);
        food_name1.setText(food_name);

        TextView food_kind1 = (TextView)findViewById(R.id.food_kind);
        food_kind1.setText(food_kind);

        TextView element1 = (TextView)findViewById(R.id.element);
        element1.setText(Element);

        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction("dynamic_notification");    //添加动态广播的Action
        DynamicReceiver dynamicReceiver = new DynamicReceiver();
        registerReceiver(dynamicReceiver, dynamic_filter);    //注册自定义动态广播消息


    }
    public void click_star(View view)
    {
        ImageView Star_icon = (ImageView) findViewById(R.id.Star);
        if( Star_situation.equals("empty") )
        {
            Star_icon.setBackgroundResource(R.drawable.full_star);
           Star_situation = "full";
        }
        else if( Star_situation.equals("full") )
        {
            Star_icon.setBackgroundResource(R.drawable.empty_star);
            Star_situation = "empty";
        }
    }
    public void shoppingChart_click(View view)
    {
        Toast.makeText(getApplicationContext(),"已收藏",
            Toast.LENGTH_SHORT).show();
        is_putInto_collect = true;

        Intent intentBroadcast = new Intent(); //定义Intent
        intentBroadcast.setAction("dynamic_notification");
        intentBroadcast.putExtras(bundle);
        sendBroadcast(intentBroadcast);

        EventBus.getDefault().post(
                new MessageBus(circle_name,food_name,food_kind,element,title_color));

        Intent Widget_broadcast_back = new Intent("com.com.study.zhengxq27.healthfoodlist.Widget.BroadCast");
        sendBroadcast(Widget_broadcast_back);
    }

    public void backButton_click(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
       if( is_putInto_collect == true )
       {
           Bundle bundle1 = new Bundle();
           bundle1.putString("circle_name",Item.textview1_content);
           bundle1.putString("food_name",Item.textview2_content);
           bundle1.putString("food_kind",Item.kind);
           bundle1.putString("element",Item.element);
           bundle1.putString("title_color",Item.title_color);
           intent.putExtras(bundle);
           setResult(ADD_CODE,intent);
       }
       else
       {
           setResult(NOTADD_CODE,intent);
       }
        finish();
    }

}
