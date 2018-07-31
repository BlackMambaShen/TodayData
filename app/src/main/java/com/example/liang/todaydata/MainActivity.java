package com.example.liang.todaydata;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private String path="http://api.juheapi.com/japi/toh?key=f39bf8fe1fb0a831119bba1e647bed17&v=1.0";
    private RecyclerView recycle_view;
    private ArrayList<NewsInfo> newsInfos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        String TodayPath = getTodayData();
        getTodayJsonData(TodayPath);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycle_view.setLayoutManager(linearLayoutManager);

    }

    private void initView() {
         recycle_view = (RecyclerView)findViewById(R.id.recycle_view);
    }


    class Myadpter extends RecyclerView.Adapter<Myadpter.ViewHolder>{
        private ArrayList<NewsInfo>mLists;
        private LayoutInflater mInflater;
        private Context mContext;
//        public interface OnItemClickListener{
//            void OnItemClick(View view,int position);
//            void OnItemLongClick(View view,int position);
//        }
//
//        private OnItemClickListener onItemClickListener;
//
//        public void setOnItemClickListener(OnItemClickListener listener){
//                    onItemClickListener=listener;
//         }

        public Myadpter(ArrayList<NewsInfo>lists,Context context){
            mLists=lists;
            this.mContext=context;
            mInflater=LayoutInflater.from(context);
        }
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=mInflater.inflate(R.layout.info_item,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final NewsInfo info = mLists.get(position);
            String SrcPath = info.pic;
            Glide.with(getApplicationContext()).load(SrcPath).into(holder.pic);
            holder.des.setText(info.des);
            holder.title.setText(info.title);
            final String time="时间"+info.year+"年"+info.month+"月"+info.day+"日";
            holder.time.setText(time);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,ItemActivity.class);
                    intent.putExtra("des",info.des);
                    intent.putExtra("title",info.title);
                    intent.putExtra("pic",info.pic);
                    intent.putExtra("time",time);
                    startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return mLists.size();
        }

         class ViewHolder extends RecyclerView.ViewHolder{
            TextView id;
            TextView title;
            ImageView pic;
            TextView time;
            TextView lunar;
             TextView des;
            public ViewHolder(View view) {
                super(view);
                des= (TextView) view.findViewById(R.id.tv_des);
                time=(TextView) view.findViewById(R.id.tv_time);
                title=(TextView) view.findViewById(R.id.tv_title);
                pic=(ImageView) view.findViewById(R.id.iv_pic);
            }
        }
    }

    private ArrayList<NewsInfo> parseJsonData(String todayJsonData) {
        try {
            JSONObject jo=new JSONObject(todayJsonData);
            JSONArray ja = jo.getJSONArray("result");
            ArrayList<NewsInfo> newsInfoList=new ArrayList<NewsInfo>();
            for (int i = 0; i <ja.length() ; i++) {
                JSONObject jo1 = ja.getJSONObject(i);
                NewsInfo info=new NewsInfo();
                info.id=jo1.getString("_id");
                info.title=jo1.getString("title");
                info.pic=jo1.getString("pic");
                info.des=jo1.getString("des");
                info.lunar=jo1.getString("lunar");
                info.year=jo1.getInt("year");
                info.month=jo1.getInt("month");
                info.day=jo1.getInt("day");
                newsInfoList.add(info);
            }
            return newsInfoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getTodayJsonData(String todayPath) {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(todayPath).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                System.out.println("结果："+responseData);
                //解析json数据
                newsInfos = parseJsonData(responseData);
//                System.out.println("个数："+newsInfos.size());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Myadpter myadpter=new Myadpter(newsInfos,MainActivity.this);
                        recycle_view.setAdapter(myadpter);
                    }
                });
            }
        });
    }

    private String getTodayData() {
        //获取手机今日日期
        Calendar calendar=Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        path=path+"&month="+month+"&day="+day;
//        Log.d("MainActivity",path);
        return path;
    }
}
