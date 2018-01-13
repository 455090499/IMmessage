package cn.jit.immessage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Info5Activity extends AppCompatActivity {
    private Button btn1;
    private ImageView img;
   // private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private ImageButton imgbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info5);

        btn1=(Button)findViewById(R.id.info5_btn1);
        imgbtn=(ImageButton)findViewById(R.id.info5_imbtn);
        img=(ImageView)findViewById(R.id.info5_im);
     //   tv1=(TextView)findViewById(R.id.info5_tv1);
        tv2=(TextView)findViewById(R.id.info5_tv2);
        tv3=(TextView)findViewById(R.id.info5_tv3);
        tv4=(TextView)findViewById(R.id.info5_tv4);
        Intent intent = getIntent();
        String desc = intent.getStringExtra("desc");
        BmobQuery<ginfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("gid", desc);
        bmobQuery.findObjects(new FindListener<ginfo>() {
            @Override
            public void done(List<ginfo> list, BmobException e) {
                for (ginfo u1 : list) {
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
       //             tv1.setText(u1.get());
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
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Info5Activity.this,Showgroupers.class);
                intent1.putExtra("gid",tv4.getText().toString());
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
