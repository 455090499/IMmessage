package cn.jit.immessage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class AddgroupActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private Button button3;
    private EditText et1;
    private ImageButton im;
    private ListView lv1;
    private gphone gphone=new gphone();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgroup);
        lv1 = (ListView) findViewById(R.id.addgroup_lv1);
        et1=(EditText)findViewById(R.id.addgroup_et1);
        im=(ImageButton)findViewById(R.id.addgroup_imbtn);
        button1=(Button)findViewById(R.id.addgroup_btn1);
        button2=(Button)findViewById(R.id.addgroup_btn2);
        button3=(Button)findViewById(R.id.addgroup_btn3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String name = intent.getStringExtra("name");
                String gid=intent.getStringExtra("gid");
                if(name!=null){
                    gphone.insertgphone(name,et1.getText().toString());
                }else if(gid!=null){
                    gphone.insertgphone(gid,et1.getText().toString());
                }

            }
        });
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et1.getText().toString().length() == 11){
                    BmobQuery<uinfo> bmobQuery5 = new BmobQuery<>();
                    bmobQuery5.addWhereEqualTo("phone", et1.getText().toString());
                    bmobQuery5.findObjects(new FindListener<uinfo>() {
                        @Override
                        public void done(List<uinfo> list, BmobException e) {
                            if(list.size()==0)
                                Toast.makeText(AddgroupActivity.this,"该用户名不存在", Toast.LENGTH_SHORT).show();

                            for (uinfo u1 : list) {


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
                                SimpleAdapter simplead = new SimpleAdapter(AddgroupActivity.this, listems,
                                        R.layout.addhaoyou, new String[] { "name", "head", "desc" },
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
                                lv1.setAdapter(simplead);

                            }
                        }
                    });
                }else{
                    Toast.makeText(AddgroupActivity.this,"请输入正确的用户",Toast.LENGTH_SHORT).show();
                }
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
