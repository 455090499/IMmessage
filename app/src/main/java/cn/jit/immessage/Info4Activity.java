package cn.jit.immessage;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;
import qiu.niorgai.StatusBarCompat;

public class Info4Activity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private ImageView img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info4);


        //透明状态栏
        StatusBarCompat.translucentStatusBar(Info4Activity.this);
        //SDK >= 21时, 取消状态栏的阴影
        StatusBarCompat.translucentStatusBar(Info4Activity.this,true);

        tv1 = (TextView) findViewById(R.id.info4_tv1);
        tv2 = (TextView) findViewById(R.id.info4_tv2);
        tv3 = (TextView) findViewById(R.id.info4_tv3);
        tv4 = (TextView) findViewById(R.id.info4_tv4);
        tv5 = (TextView) findViewById(R.id.info4_tv5);
        tv6 = (TextView) findViewById(R.id.info4_tv6);
        img = (ImageView) findViewById(R.id.info4_im);
        btn1 = (Button) findViewById(R.id.info4_btn1);
        btn2 = (Button) findViewById(R.id.info4_btn2);


        Intent intent = getIntent();
        final String desc = intent.getStringExtra("desc");

        BmobQuery<uinfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("phone", desc);
        bmobQuery.findObjects(new FindListener<uinfo>() {
            @Override
            public void done(List<uinfo> list, BmobException e) {
                for (uinfo u1 : list) {
                    BmobFile bmobfile = u1.getPhoto();

                    if(bmobfile!=null)
                        try {

                            String url = bmobfile.getFileUrl();
                            Bitmap bitmap = getBitmap(url);
                            img.setImageBitmap(bitmap);
                        } catch (IOException e2) {
                            // TODO Auto-generated catch block
                            e2.printStackTrace();
                        }
                    tv1.setText(u1.getNiconame());
                    tv2.setText(u1.getBirth());
                    tv3.setText(u1.getSex());
                    tv4.setText(u1.getEmail());
                    tv5.setText(u1.getPs());
                    tv6.setText(u1.getPhone());

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
                  new CommomDialog(Info4Activity.this, R.style.dialog, "您确定删除该好友？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                            String content = pre.getString("sms_content", "");

                            String bql = "select * from pfriend where (phone='" + content + "'and fphone='" + desc + "')or(phone='"+desc+"'and fphone='"+content+"')";
                            BmobQuery<pfriend> query = new BmobQuery<pfriend>();
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
                                                        //Toast.makeText(Info4Activity.this, "删除好友成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(Info4Activity.this, "删除好友失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }else{
                                        Toast.makeText(Info4Activity.this, "查找失败", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            Intent intent1=new Intent(Info4Activity.this,Body1Activity.class);
                            startActivity(intent1);

                        }else{
                            dialog.dismiss();
                        }

                    }
                  }).setTitle("提示").show();
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
