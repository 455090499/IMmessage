package cn.jit.immessage;

import android.app.ActionBar;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 2018/1/3.
 */

public class DemoFragment extends Fragment {
   private ListView listView;
    String[] toolbar=new String[]{"消息","好友","群组"};


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.body_message, container, false);
        int type = getArguments().getInt("type");
        listView = (ListView) view.findViewById(R.id.body_message_lv);



        if (type == 0)
        {
//            Log.e(TAG, "onCreateView: xiaoxi" );
//            Log.d("0000","xiaoxi");
//            String[] name = { "李四", "张三"};
//            String[] desc = { "怎么不回我信息", "在哪呢"};
//            List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
//            for (int i = 0; i < name.length; i++) {
//                Map<String, Object> listem = new HashMap<String, Object>();
//                listem.put("name", name[i]);
//                listem.put("desc", desc[i]);
//                listems.add(listem);
//            }
//           SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
//                   R.layout.xiaoxi, new String[] { "name", "head", "desc" },
//                   new int[] {R.id.name,R.id.head,R.id.desc});
//            listView.setAdapter(simplead);
//            simplead.notifyDataSetChanged();
            List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
            Log.e(TAG, "done:112");
            for(int i=0;i<getArguments().getInt("count");i++) {
                Map<String, Object> listem = new HashMap<String, Object>();
                listem.put("name", getArguments().getStringArray("name")[i]);
                System.out.println(getArguments().getStringArray("name")[i]);
                listem.put("desc", getArguments().getStringArray("desc")[i].equals("")?"请求添加你为好友":"请求加入群"+getArguments().getStringArray("desc")[i]);
                listem.put("head", getArguments().getStringArray("head")[i]);
                listems.add(listem);
            }


            SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
                    R.layout.haoyou, new String[] { "name", "head", "desc" },
                    new int[] {R.id.name,R.id.head,R.id.desc}){
                @Override
                public void setViewImage(final ImageView v, final  String value) {
                    // TODO Auto-generated method stub
                    if(v.getId()==R.id.head)
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    //通过图片Url返回Bitmap
                                    Bitmap bitmap = getBitmap(value);
                                    Log.d("12333","done:"+value);
                                    v.setImageBitmap(bitmap);
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else{super.setViewImage(v, value);}
                }

            };

            listView.setAdapter(simplead);
            //Body1Activity.tv.setText(toolbar[1]);
            simplead.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(position);
                    String name=map.get("name");
                    String desc=map.get("desc");
                    String head=map.get("head");
                    Intent intent = new Intent(getActivity(),Notify2Activity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("desc",getArguments().getStringArray("desc")[position]);
                    intent.putExtra("head",head);
                    startActivity(intent);
                }
            });

        }
        else if (type == 1)
        {



            List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();

                Log.e(TAG, "done:112");
                for(int i=0;i<getArguments().getInt("count");i++) {
                    Map<String, Object> listem = new HashMap<String, Object>();
                    listem.put("name", getArguments().getStringArray("name")[i]);
                    System.out.println(getArguments().getStringArray("name")[i]);
                    listem.put("desc", getArguments().getStringArray("desc")[i]);
                    listem.put("head", getArguments().getStringArray("head")[i]);
                    listems.add(listem);
                }


            SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
                    R.layout.haoyou, new String[] { "name", "head", "desc" },
                    new int[] {R.id.name,R.id.head,R.id.desc}){
                @Override
                public void setViewImage(final ImageView v, final  String value) {
                    // TODO Auto-generated method stub
                    if(v.getId()==R.id.head)
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    //通过图片Url返回Bitmap
                                    Bitmap bitmap = getBitmap(value);
                                    Log.d("12333","done:"+value);
                                    v.setImageBitmap(bitmap);
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else{super.setViewImage(v, value);}
                }

            };

            listView.setAdapter(simplead);
            //Body1Activity.tv.setText(toolbar[1]);
            simplead.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(position);
                    String name=map.get("name");
                    String desc=map.get("desc");
                    Intent intent = new Intent(getActivity(),ChatActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("desc",desc);
                    startActivity(intent);
                }
            });





        } else if (type == 2)
        {

            System.out.println("够好跪下!");



            List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
            for(int i=0;i<getArguments().getInt("count");i++) {
                Map<String, Object> listem = new HashMap<String, Object>();
                listem.put("name", getArguments().getStringArray("name")[i]);
                System.out.println(getArguments().getStringArray("name")[i]);
                listem.put("desc", getArguments().getStringArray("desc")[i]);
                listem.put("head", getArguments().getStringArray("head")[i]);
                listems.add(listem);
            }

            SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
                    R.layout.haoyou, new String[] { "name", "head", "desc" },
                    new int[] {R.id.name,R.id.head,R.id.desc}){
                @Override
                public void setViewImage(final ImageView v, final  String value) {
                    // TODO Auto-generated method stub
                    if(v.getId()==R.id.head)
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    //通过图片Url返回Bitmap
                                    Bitmap bitmap = getBitmap(value);
                                    Log.d("12333","done:"+value);
                                    v.setImageBitmap(bitmap);
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    else{super.setViewImage(v, value);}
                }

            };
            simplead.notifyDataSetChanged();
            listView.setAdapter(simplead);
            //Body1Activity.tv.setText(toolbar[2]);



            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(position);
                    String name=map.get("name");
                    String desc=map.get("desc");
                    Intent intent = new Intent(getActivity(),Chat2Activity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("desc",desc);
                    startActivity(intent);



                }
            });


        }

        return view;
    }
    public Bitmap getBitmap(String path) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}