package cn.jit.immessage;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.StrictMode;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btn1=(Button)findViewById(R.id.add_btn1);
        btn2=(ImageButton)findViewById(R.id.add_imbtn);
        btn3=(Button)findViewById(R.id.add_btn2);
        et1=(EditText)findViewById(R.id.add_et1);
        lv1 = (ListView) findViewById(R.id.add_lv1);
        im=(ImageView)findViewById(R.id.head);

        //图片设置
        StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BmobQuery<uinfo> bmobQuery5 = new BmobQuery<>();
                bmobQuery5.addWhereEqualTo("phone", et1.getText().toString());
                bmobQuery5.findObjects(new FindListener<uinfo>() {
                    @Override
                    public void done(List<uinfo> list, BmobException e) {
                        for (uinfo u1 : list) {

//                            BmobFile bmobfile = u1.getPhoto();
//                            try {
//                                String url = bmobfile.getFileUrl();
//                                Log.e("1", url);
//                                Bitmap bitmap = getBitmap(url);
//                                im.setImageBitmap(bitmap);
//                                } catch (IOException e2) {
//                                    // TODO Auto-generated catch block
//                                    e2.printStackTrace();
//                                }

                            String s1="(";
                            String s2=")";
                            String[] name = {u1.getNiconame()};
                            String[] desc = {(s1.concat(u1.getPhone())).concat(s2)};

                            List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
                            for (int i = 0; i < name.length; i++) {
                                Map<String, Object> listem = new HashMap<String, Object>();
                                //listem.put("head", imageids[i]);
                                listem.put("name", name[i]);
                                listem.put("desc", desc[i]);
                                listems.add(listem);
                            }
                            SimpleAdapter simplead = new SimpleAdapter(AddActivity.this, listems,
                                    R.layout.additem, new String[]{"name", "head", "desc"},
                                    new int[]{R.id.name, R.id.head, R.id.desc});
                            lv1.setAdapter(simplead);
                        }
                    }
                });
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
