package cn.jit.immessage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import cn.bmob.v3.listener.SQLQueryListener;
import qiu.niorgai.StatusBarCompat;

import static android.content.ContentValues.TAG;

public class Showgroupers extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private ListView lv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showgroupers);

        //透明状态栏
        StatusBarCompat.translucentStatusBar(Showgroupers.this);
        //SDK >= 21时, 取消状态栏的阴影
        StatusBarCompat.translucentStatusBar(Showgroupers.this,true);

        lv1=(ListView)findViewById(R.id.showgroupers_lv1);
        btn1=(Button)findViewById(R.id.showgroupers_btn1);
        btn2=(Button)findViewById(R.id.showgroupers_btn2);
        Intent intent = getIntent();
        final String  gid= intent.getStringExtra("gid");

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Showgroupers.this,AddgroupActivity.class);
                intent.putExtra("gid",gid);
                startActivity(intent);
                finish();
            }
        });




        String bql ="select * from  uinfo where phone in (select phone from gphone where gid = '"+gid+"')";
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

                    SimpleAdapter simplead = new SimpleAdapter(Showgroupers.this, listems,
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

                    lv1.setAdapter(simplead);
                    simplead.notifyDataSetChanged();




                }else{
                    Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
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
