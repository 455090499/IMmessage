package cn.jit.immessage;

import android.content.Intent;
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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import qiu.niorgai.StatusBarCompat;

public class Info4Activity extends AppCompatActivity {
    private Button btn;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
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
        img = (ImageView) findViewById(R.id.info4_im);
        Button btn1 = (Button) findViewById(R.id.info4_btn1);

        Intent intent = getIntent();
        String desc = intent.getStringExtra("desc");

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
                            Log.e("1", url);
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
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
