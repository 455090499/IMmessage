package cn.jit.immessage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

public class AddActivity extends AppCompatActivity {
    private ListView lv1;
    private Button btn1;
    private ImageButton btn2;
    private Button btn3;
    private EditText et1;
    private ImageView im;
    private boolean isfound=false;
    pfriend pfriend=new pfriend();
    gphone gphone=new gphone();

    String sendphone;
    String recvphone;
    String groupphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btn1=(Button)findViewById(R.id.add_btn1);
        btn2=(ImageButton)findViewById(R.id.add_imbtn);
//        btn3=(Button)findViewById(R.id.add_item_btn);
        et1=(EditText)findViewById(R.id.add_et1);
        lv1 = (ListView) findViewById(R.id.add_lv1);
        im=(ImageView)findViewById(R.id.head);

        SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
        String content1 = pre.getString("sms_content", "");
        sendphone = content1;

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }



        //图片设置
        StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isfound) {
                    Intent in = new Intent(AddActivity.this,Body1Activity.class);
                    startActivity(in);
                }
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et1.getText().toString().length() == 11){
                    BmobQuery<uinfo> bmobQuery5 = new BmobQuery<>();
                    bmobQuery5.addWhereEqualTo("phone", et1.getText().toString());
                    bmobQuery5.findObjects(new FindListener<uinfo>() {
                        @Override
                        public void done(List<uinfo> list, BmobException e) {
                            if(list.size()==0)
                                Toast.makeText(AddActivity.this,"该用户名不存在", Toast.LENGTH_SHORT).show();
                            else
                                isfound=true;
                            for (uinfo u1 : list) {

                                recvphone = u1.getPhone();
                                BmobFile bmobFile=u1.getPhoto();
                                String url = bmobFile.getFileUrl();
                                String s1="(";
                                String s2=")";
                                String[] name = {u1.getNiconame()};
                                String[] desc = {(s1.concat(u1.getPhone())).concat(s2)};
                                String[] head={url};
                                List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
                                for (int i = 0; i < name.length; i++) {
                                    Map<String, Object> listem = new HashMap<String, Object>();
                                    //listem.put("head", imageids[i]);

                                    listem.put("name", name[i]);
                                    listem.put("desc", desc[i]);
                                     listem.put("head",head[i]);
                                    listems.add(listem);
                                }
                                SimpleAdapter simplead = new SimpleAdapter(AddActivity.this, listems,
                                        R.layout.additem, new String[] { "name", "head", "desc"},
                                        new int[] {R.id.add_item_name,R.id.add_item_imag,R.id.add_item_desc}){


//                                    @Override
//                                    public View getView(int position, View convertView, ViewGroup parent) {
//
//                                        notifyDataSetChanged();
//                                        btn3=(Button) convertView.findViewById(R.id.add_item_btn);
//                                        final View view=super.getView(position, convertView, parent);
//                                        btn3.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
//                                                if(isfound){
//                                                    pfriend.insertpfriend(pre.getString("sms_content", ""),et1.getText().toString());
//                                                    Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_LONG).show() ;
//                                                }
//                                            }
//                                        });
//                                        return view;
//                                    }
                                    @Override
                                    public void setViewImage(final ImageView v, final  String value) {
                                        // TODO Auto-generated method stub
                                        if(v.getId()==R.id.add_item_imag)
                                        {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try{
                                                        //通过图片Url返回Bitmap
                                                        Bitmap bitmap = getBitmap(value);
                                                        Log.d("12333","done:"+value);
                                                        v.setImageBitmap(bitmap);
                                                        ((Button) findViewById(R.id.add_item_btn)).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                                                                SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                                                                if(isfound) {
                                                                    Message msg = new Message();
                                                                    msg.what = 1;
                                                                    msg.obj = new Mes(sendphone, "0", recvphone);
                                                                    Body1Activity.bodyThread.revHandler.sendMessage(msg);
                                                                    pfriend.insertpfriend(pre.getString("sms_content", ""), et1.getText().toString());
                                                                }
                                                            }
                                                        });
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
                                lv1.setAdapter(simplead);


                            }
                        }
                    });
                }else{
                    BmobQuery<ginfo> bmobQuery = new BmobQuery<>();
                    bmobQuery.addWhereEqualTo("gid", et1.getText().toString());
                    bmobQuery.findObjects(new FindListener<ginfo>() {
                        @Override
                        public void done(List<ginfo> list, BmobException e) {
                            if (list.size() == 0)
                                Toast.makeText(AddActivity.this, "该群组不存在", Toast.LENGTH_SHORT).show();
                            else
                                isfound = true;
                            for (ginfo gu1 : list) {

                                BmobFile bmobFile=gu1.getPhoto();
                                String url = bmobFile.getFileUrl();

                                recvphone = gu1.getPhone();
                                groupphone = gu1.getGid();
                                String s1 = "(";
                                String s2 = ")";
                                String[] name = {gu1.getGname()};
                                String[] desc = {(s1.concat(gu1.getGid())).concat(s2)};
                                String[] head={url};
                                List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
                                for (int i = 0; i < name.length; i++) {
                                    Map<String, Object> listem = new HashMap<String, Object>();
                                    //listem.put("head", imageids[i]);

                                    listem.put("name", name[i]);
                                    listem.put("desc", desc[i]);
                                    listem.put("head",head[i]);
                                    listems.add(listem);
                                }
                                SimpleAdapter simplead = new SimpleAdapter(AddActivity.this, listems,
                                        R.layout.additem, new String[] { "name", "head", "desc" },
                                        new int[] {R.id.add_item_name,R.id.add_item_imag,R.id.add_item_desc}){
                                    @Override
                                    public void setViewImage(final ImageView v, final  String value) {
                                        // TODO Auto-generated method stub
                                        if(v.getId()==R.id.add_item_imag)
                                        {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try{
                                                        //通过图片Url返回Bitmap
                                                        Bitmap bitmap = getBitmap(value);
                                                        Log.d("12333","done:"+value);
                                                        v.setImageBitmap(bitmap);
                                                        ((Button) findViewById(R.id.add_item_btn)).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                                                                SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                                                                if(isfound) {
                                                                    Message msg = new Message();
                                                                    msg.what = 1;
                                                                    msg.obj = new Mes(sendphone, "0", groupphone + "/" + recvphone);
                                                                    Body1Activity.bodyThread.revHandler.sendMessage(msg);
                                                                    gphone.insertgphone(et1.getText().toString(),pre.getString("sms_content", "") );
                                                                }
                                                            }
                                                        });
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
                                lv1.setAdapter(simplead);

                            }
                        }
                    });
                }


            }
        });
//        btn3=(Button) findViewById(R.id.add_item_btn);
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
//                if(isfound){
//                    pfriend.insertpfriend(pre.getString("sms_content", ""),et1.getText().toString());
//                }
//
//
//
//            }
//        });
        et1.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isfound=false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });



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
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
