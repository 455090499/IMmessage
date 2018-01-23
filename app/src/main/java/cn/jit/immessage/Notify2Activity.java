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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;
import qiu.niorgai.StatusBarCompat;

public class Notify2Activity extends AppCompatActivity {
    private TextView tv1;
    private TextView tv2;
    private ImageView im;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private pfriend pf1=new pfriend();
    private gphone gp1=new gphone();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify2);

        //透明状态栏
        StatusBarCompat.translucentStatusBar(Notify2Activity.this);
        //SDK >= 21时, 取消状态栏的阴影
        StatusBarCompat.translucentStatusBar(Notify2Activity.this,true);

        tv1=(TextView) findViewById(R.id.notify2_et1);
        tv2=(TextView) findViewById(R.id.notify2_et2);
        im=(ImageView)findViewById(R.id.notify2_im);
        btn1=(Button)findViewById(R.id.notify2_btn1);
        btn2=(Button)findViewById(R.id.notify2_btn2);
        btn3=(Button)findViewById(R.id.notify2_btn3);


        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String desc = intent.getStringExtra("desc");
        final String head=intent.getStringExtra("head");
        tv1.setText(name+"");
        tv2.setText(desc.equals("")?"请求添加你为好友":"请求加入群"+desc);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //通过图片Url返回Bitmap
                    Bitmap bitmap = getBitmap(head);
                    im.setImageBitmap(bitmap);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(desc.equals("")){
                    pf1.insertpfriend(Body1Activity.p1.getPhone(),name);
                }else{
                    gp1.insertgphone(desc,name);
                }
                String bq2 = "select * from inform where phone='" +Body1Activity.p1.getPhone()  + "'and sphone='" + name + "'and content='"+desc+"'";
                BmobQuery<inform> query = new BmobQuery<>();
                //设置查询的SQL语句
                query.setSQL(bq2);
                query.doSQLQuery(new SQLQueryListener<inform>() {

                    @Override
                    public void done(BmobQueryResult<inform> result, BmobException e) {
                        if (e == null) {
                            List<inform> list = (List<inform>) result.getResults();
                            for (inform if1 : list) {
                                if1.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {

                                        } else {
                                            Toast.makeText(Notify2Activity.this, "删除记录失败", Toast.LENGTH_SHORT).show();
                                        }
                                        Intent intent1=new Intent(Notify2Activity.this,Body1Activity.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                });
                            }


                        }


                    }
                });

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(desc.equals("")){
                    String bql = "select * from pfriend where phone='" + name + "'and fphone='" + Body1Activity.p1.getPhone() + "'";
                    BmobQuery<pfriend> query = new BmobQuery<>();
                    //设置查询的SQL语句
                    query.setSQL(bql);
                    query.doSQLQuery(new SQLQueryListener<pfriend>() {

                        @Override
                        public void done(BmobQueryResult<pfriend> result, BmobException e) {
                            if (e == null) {
                                List<pfriend> list = (List<pfriend>) result.getResults();
                                for (pfriend pf1 : list) {
                                    pf1.delete(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {

                                            } else {
                                                Toast.makeText(Notify2Activity.this, "删除成员失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                String bq2 = "select * from inform where phone='" +Body1Activity.p1.getPhone()  + "'and sphone='" + name + "'and content='"+desc+"'";
                                BmobQuery<inform> query = new BmobQuery<>();
                                //设置查询的SQL语句
                                query.setSQL(bq2);
                                query.doSQLQuery(new SQLQueryListener<inform>() {

                                    @Override
                                    public void done(BmobQueryResult<inform> result, BmobException e) {
                                        if (e == null) {
                                            List<inform> list = (List<inform>) result.getResults();
                                            for (inform if1 : list) {
                                                if1.delete(new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null) {

                                                        } else {
                                                            Toast.makeText(Notify2Activity.this, "删除记录失败", Toast.LENGTH_SHORT).show();
                                                        }
                                                        Intent intent1=new Intent(Notify2Activity.this,Body1Activity.class);
                                                        startActivity(intent1);
                                                        finish();
                                                    }
                                                });
                                            }


                                        }


                                    }
                                });



                            }


                        }
                    });

                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
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
