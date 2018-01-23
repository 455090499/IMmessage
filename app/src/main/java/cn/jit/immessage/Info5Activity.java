package cn.jit.immessage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;
import qiu.niorgai.StatusBarCompat;

import static android.content.ContentValues.TAG;

public class Info5Activity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private ImageView img;
    // private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private ImageButton imbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info5);

        //透明状态栏
        StatusBarCompat.translucentStatusBar(Info5Activity.this);
        //SDK >= 21时, 取消状态栏的阴影
        StatusBarCompat.translucentStatusBar(Info5Activity.this,true);


        btn1 = (Button) findViewById(R.id.info5_btn1);
        btn2 = (Button) findViewById(R.id.info5_btn2);
        imbtn = (ImageButton) findViewById(R.id.info5_imbtn);
        img = (ImageView) findViewById(R.id.info5_im);
        //   tv1=(TextView)findViewById(R.id.info5_tv1);
        tv2 = (TextView) findViewById(R.id.info5_tv2);
        tv3 = (TextView) findViewById(R.id.info5_tv3);
        tv4 = (TextView) findViewById(R.id.info5_tv4);
        Intent intent = getIntent();
        String desc = intent.getStringExtra("desc");
        BmobQuery<ginfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("gid", desc);
        bmobQuery.findObjects(new FindListener<ginfo>() {
            @Override
            public void done(List<ginfo> list, BmobException e) {
                for (ginfo u1 : list) {
                    BmobFile bmobfile = u1.getPhoto();

                    if (bmobfile != null)
                        try {
                            String url = bmobfile.getFileUrl();
                            Bitmap bitmap = getBitmap(url);
                            img.setImageBitmap(bitmap);
                        } catch (IOException e2) {
                            // TODO Auto-generated catch block
                            e2.printStackTrace();
                        }
                    tv2.setText(u1.getPhone());
                    tv3.setText(u1.getGname());
                    tv4.setText(u1.getGid());
                }
            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new CommomDialog(Info5Activity.this, R.style.dialog, "您确定退出该群？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog,boolean confirm) {
                        if(confirm){
                            SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                            String content = pre.getString("sms_content", "");
                            if (content.equals(tv2.getText().toString())) {
                                BmobQuery<gphone> bmobQuery = new BmobQuery<>();
                                bmobQuery.addWhereEqualTo("gid", tv4.getText().toString());
                                bmobQuery.findObjects(new FindListener<gphone>() {
                                    @Override
                                    public void done(List<gphone> list, BmobException e) {
                                        for (gphone u1 : list) {
                                            u1.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        //Toast.makeText(Info5Activity.this, "删除gphone成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(Info5Activity.this, "删除gphone失败", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                BmobQuery<ginfo> bmobQuery1 = new BmobQuery<>();
                                bmobQuery1.addWhereEqualTo("phone", content);
                                bmobQuery1.addWhereEqualTo("gid",tv4.getText().toString());
                                bmobQuery1.findObjects(new FindListener<ginfo>() {
                                    @Override
                                    public void done(List<ginfo> list, BmobException e) {
                                        for (ginfo u1 : list) {
                                            u1.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        //Toast.makeText(Info5Activity.this, "删除ginfo成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(Info5Activity.this, "删除ginfo失败", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                Intent intent1=new Intent(Info5Activity.this,Body1Activity.class);
                                startActivity(intent1);

                            } else {

                                String bql = "select * from gphone where gid='" + tv4.getText().toString() + "'and phone='" + Body1Activity.p1.getPhone() + "'";
                                BmobQuery<gphone> query = new BmobQuery<gphone>();
                                //设置查询的SQL语句
                                query.setSQL(bql);
                                query.doSQLQuery(new SQLQueryListener<gphone>() {

                                    @Override
                                    public void done(BmobQueryResult<gphone> result, BmobException e) {
                                        if (e == null) {
                                            List<gphone> list = (List<gphone>) result.getResults();
                                            for (gphone uf1 : list) {
                                                uf1.delete(new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null) {
                                                            //Toast.makeText(Info5Activity.this, "删除成员成功", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Info5Activity.this, "删除成员失败", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }

                                        }

                                    }
                                });
                                Intent intent1=new Intent(Info5Activity.this,Body1Activity.class);
                                startActivity(intent1);
                            }


                        }else{
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();
            }
        });

        imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Info5Activity.this, Showgroupers.class);
                intent1.putExtra("gid", tv4.getText().toString());
                startActivity(intent1);

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