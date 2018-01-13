package cn.jit.immessage;

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

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.body_message, container, false);
        int type = getArguments().getInt("type", 0);
        listView = (ListView) view.findViewById(R.id.body_message_lv);

        //textView.setText(type == 0 ? "1st Fragment" : type == 1 ? "2nd Fragment" : type == 2 ? "3rd Fragment" : "4th Fragment");
        if (type == 0)
        {

            Log.e(TAG, "onCreateView: xiaoxi" );
            Log.d("0000","xiaoxi");
            String[] name = { "李四", "张三"};
            String[] desc = { "怎么不回我信息", "在哪呢"};
            //int[] imageids ={};
            List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < name.length; i++) {
                Map<String, Object> listem = new HashMap<String, Object>();
              //  listem.put("head", imageids[i]);
                listem.put("name", name[i]);
                listem.put("desc", desc[i]);
                listems.add(listem);
            }
           SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
                   R.layout.xiaoxi, new String[] { "name", "head", "desc" },
                   new int[] {R.id.name,R.id.head,R.id.desc});
            listView.setAdapter(simplead);

            simplead.notifyDataSetChanged();
        }
        if (type == 1)
        {
            Log.d("1111","haoyou");
            String bql ="select * from uinfo where phone in (select fphone from pfriend where phone = '"+Body1Activity.p1.getPhone()+"')";
            BmobQuery<uinfo> query=new BmobQuery<uinfo>();
//设置查询的SQL语句
            query.setSQL(bql);
            query.doSQLQuery(new SQLQueryListener<uinfo>(){

                @Override
                public void done(BmobQueryResult<uinfo> result, BmobException e) {
                    if(e ==null){
                        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
                        List<uinfo> list = (List<uinfo>) result.getResults();

                            for (uinfo uf1 : list) {
                                BmobFile bmobFile=uf1.getPhoto();
                                Map<String, Object> listem = new HashMap<String, Object>();
                                String name =uf1.getNiconame();
                                String desc =uf1.getPhone();
                                String url = bmobFile.getFileUrl();
                                Log.e(TAG, "done:"+name);
                                listem.put("name", name);
                                listem.put("desc", desc);
                                listem.put("head",url);
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
                            simplead.notifyDataSetChanged();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HashMap<String,String> map=(HashMap<String,String>)listView.getItemAtPosition(position);
                                    String name=map.get("name");
                                    Intent intent = new Intent(getActivity(),ChatActivity.class);
                                    intent.putExtra("name",name);
                                    startActivity(intent);
                                }
                            });

                    }else{
                        Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    }
                }
            });
        }
        if (type == 2)
        {
            Log.d("2222","qunzu");
            String bql ="select * from  ginfo where gid in (select gid from gphone where phone = '"+Body1Activity.p1.getPhone()+"')";
            BmobQuery<ginfo> query=new BmobQuery<ginfo>();
            //设置查询的SQL语句
            query.setSQL(bql);
            query.doSQLQuery(new SQLQueryListener<ginfo>(){

                @Override
                public void done(BmobQueryResult<ginfo> result, BmobException e) {
                    if(e ==null){
                        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
                        List<ginfo> list = (List<ginfo>) result.getResults();

                        for (ginfo uf1 : list) {
                            BmobFile bmobFile=uf1.getPhoto();
                            Map<String, Object> listem = new HashMap<String, Object>();
                            String name =uf1.getGname();
                            String desc =uf1.getGid();
                            String url = bmobFile.getFileUrl();
                            Log.e(TAG, "done:"+name);
                            listem.put("name", name);
                            listem.put("desc", desc);
                            listem.put("head",url);
                            listems.add(listem);
                        }

                        SimpleAdapter simplead = new SimpleAdapter(getActivity(), listems,
                                R.layout.qunzu, new String[] { "name", "head", "desc" },
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
                        simplead.notifyDataSetChanged();


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

                    }else{
                        Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    }
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