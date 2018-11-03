package com.study.zhengxq27.healthfoodlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    public static List<item> data;
    private List<item>collect_data;
    private RecyclerView recyclerView;
    private ListView collect_listview;
    private FloatingActionButton button_change;
    private String which_onshow = "rec";
    private int REQUEST_CODE = 1;
    public String circle_name1;
    public String food_name1;
    public String food_kind1;
    public String element1;
    public String title_color1;
    private myListViewAdapter collect_listviewadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

      //  Intent pi = new Intent("com.com.study.zhengxq27.healthfoodlist.Widget.BroadCast");
        //sendBroadcast(pi);


        //设置启动Activivty时弹出Notification，并且点击notifica时进入详情界面
        Intent notification_intent = new Intent("OpenAppNotification");
        Random random = new Random();
        int n = random.nextInt(data.size());
        Bundle info_bundle = new Bundle();
        item notify_item = data.get(n);
        info_bundle.putString("circle_name",notify_item.textview1_content);
        info_bundle.putString("food_name",notify_item.textview2_content);
        info_bundle.putString("food_kind",notify_item.kind);
        info_bundle.putString("element",notify_item.element);
        info_bundle.putString("title_color",notify_item.title_color);
        notification_intent.putExtras(info_bundle);
        sendBroadcast(notification_intent);

        EventBus.getDefault().register(this);


        collect_listviewadapter = new myListViewAdapter(collect_data,MainActivity.this);
        //创建一个收藏夹
        collect_listview = (ListView)findViewById(R.id.collect_listview);
        collect_listview.setAdapter(collect_listviewadapter);
        collect_listviewadapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                item chosen_item = data.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("circle_name",chosen_item.textview1_content);
                bundle.putString("food_name",chosen_item.textview2_content);
                bundle.putString("food_kind",chosen_item.kind);
                bundle.putString("element",chosen_item.element);
                bundle.putString("title_color",chosen_item.title_color);
                Intent intent = new Intent(MainActivity.this,foodDetail.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(final int position) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("删除").setMessage("确定删除" + collect_data.get(position).textview2_content + "?").setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                collect_data.remove(position);
                                collect_listviewadapter.refresh(collect_data,MainActivity.this);
                                collect_listviewadapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
                alertDialog.show();
            }
        });
        //设置收藏夹的listview初始不可见
        collect_listview.setVisibility(View.INVISIBLE);


        recyclerView = findViewById(R.id.recyclerView);
        button_change = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        button_change.setImageResource(R.drawable.collect);
        final MyRecyclerViewAdapter myAdapter = new MyRecyclerViewAdapter<item>(MainActivity.this, R.layout.item ,data) {
            @Override
            public void convert(MyViewHolder holder, item s) {
                // item是自定义的一个类，封装了数据信息，也可以直接将数据做成一个Map，那么这里就是Map<String, Object>
                TextView textView1 = holder.getView(R.id.textview1);
                textView1.setText( s.textview1_content);
                TextView textView2 = holder.getView(R.id.textview2);
                textView2.setText(s.textview2_content);
            }
        };

        myAdapter.setData(data);
        myAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                item chosen_item = data.get(position);
               Bundle bundle = new Bundle();
               bundle.putString("circle_name",chosen_item.textview1_content);
               bundle.putString("food_name",chosen_item.textview2_content);
               bundle.putString("food_kind",chosen_item.kind);
               bundle.putString("element",chosen_item.element);
               bundle.putString("title_color",chosen_item.title_color);
               Intent intent = new Intent(MainActivity.this,foodDetail.class);
               intent.putExtras(bundle);
               startActivityForResult(intent,REQUEST_CODE);
            }

            @Override
            public void onLongClick(int position) {
              data.remove(position);
              myAdapter.notifyItemRangeRemoved(position,1);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this) );
        //recyclerView.setAdapter(myAdapter);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myAdapter);
        scaleInAnimationAdapter.setDuration(1000);
        recyclerView.setAdapter((scaleInAnimationAdapter));
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());

    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        Intent pi = new Intent("com.com.study.zhengxq27.healthfoodlist.Widget.BroadCast");
        sendBroadcast(pi);
    }

    protected void initData()
    {
        data = new ArrayList<item>();
        item item1 = new item("粮","大豆","粮食","蛋白质","#BB4C3B");
        item item2 = new item("蔬","十字花科蔬菜","蔬菜","维生素C","#C48D30");
        item item3 = new item("饮","牛奶","饮品","钙","#4469B0");
        item item4 = new item("肉","海鱼","肉食","蛋白质","#20A17B");
        item item5 = new item("蔬","菌菇类","蔬菜","微量元素","#BB4C3B");
        item item6 = new item("蔬","番茄","蔬菜","番茄红素","#4469B0");
        item item7 = new item("蔬","胡萝卜","蔬菜","胡萝卜素","#20A17B");
        item item8 = new item("粮","荞麦","粮食","膳食纤维","#BB4C3B");
        item item9 = new item("杂","鸡蛋","杂","几乎所有营养物质","#C48D30");

        data.add(item1);
        data.add(item2);
        data.add(item3);
        data.add(item4);
        data.add(item5);
        data.add(item6);
        data.add(item7);
        data.add(item8);
        data.add(item9);

        collect_data = new ArrayList<item>();
        item collect_item1 = new item("*","收藏夹","","","");
        collect_data.add(collect_item1);
    }

    public void FloatingActionButton_click(View view)
    {
        if( which_onshow.equals("rec") )
        {
            collect_listview.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            button_change.setImageResource(R.drawable.mainpage);
            which_onshow = "lis";
        }
        else if( which_onshow.equals("lis") )
        {
            recyclerView.setVisibility(View.VISIBLE);
            collect_listview.setVisibility(View.INVISIBLE);
            button_change.setImageResource(R.drawable.collect);
            which_onshow = "rec";
        }

    }
    public void onActivityResult(int requestcode,int resultcode,Intent intent_back)
    {
        super.onActivityResult(requestcode,requestcode,intent_back);
        intent_back.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if( requestcode == REQUEST_CODE)
        {
            if( resultcode == 2 )
            {
                Bundle bundle_back = intent_back.getExtras();
                circle_name1 = bundle_back.getString("circle_name");
                food_name1 = bundle_back.getString("food_name");
                food_kind1 = bundle_back.getString("food_kind");
                element1 = bundle_back.getString("element");
                title_color1 = bundle_back.getString("title_color");
                item add_item = new item(circle_name1,food_name1,food_kind1,element1,title_color1);
                collect_data.add(add_item);
                collect_listviewadapter.refresh(collect_data,MainActivity.this);
                collect_listviewadapter.notifyDataSetChanged();
            }
        }
    }
    class myListViewAdapter extends BaseAdapter
    {
        private List<item> list;
        private Context context;
        private View.OnClickListener onClickListener;
        private View.OnLongClickListener onLongClickListener;
        private OnItemClickListener onItemClickListener;

        myListViewAdapter(List<item> list, Context context) {
            this.list = list;
            this.context = context;
        }
        public void refresh(List<item> list, Context context)
        {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            if (list == null) {
                return null;
            }
            return list.get(i);
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = LayoutInflater.from(context).inflate(R.layout.item , null);
            TextView bookName = (TextView) view.findViewById(R.id.textview1);
            TextView bookPrice = (TextView) view.findViewById(R.id.textview2);
            // 从数据列表中取出对应的对象，然后赋值给他们
            bookName.setText(list.get(i).textview1_content);
            bookPrice.setText(list.get(i).textview2_content);
            // 将这个处理好的view返回
            if( onItemClickListener != null && i != 0)
            {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onClick(i);
                    }
                });
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onItemClickListener.onLongClick(i);
                        return false;
                    }
                });
            }
            return view;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageBus event)
    {
        item add_collect = new item(event.circle_name,event.food_name,event.food_kind,event.element,event.title_color);
        collect_data.add(add_collect);
        collect_listviewadapter.refresh(collect_data,MainActivity.this);
        collect_listviewadapter.notifyDataSetChanged();

        collect_listview.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        button_change.setImageResource(R.drawable.mainpage);
        which_onshow = "lis";

    }
}
