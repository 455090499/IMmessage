package cn.jit.immessage;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btn1=(Button)findViewById(R.id.add_btn1);
        btn2=(ImageButton)findViewById(R.id.add_imbtn);
        btn3=(Button)findViewById(R.id.add_btn2);
        et1=(EditText)findViewById(R.id.add_et1);
        lv1 = (ListView) findViewById(R.id.add_lv1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BmobQuery<uinfo> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("phone", et1.getText().toString());
                bmobQuery.findObjects(new FindListener<uinfo>() {
                    @Override
                    public void done(List<uinfo> list, BmobException e) {
                        for (uinfo u1 : list) {
                           /* BmobFile bmobfile = u1.getPhoto();

                            if(bmobfile!=null)
                                try {

                                    String url = bmobfile.getFileUrl();
                                    Log.e("1", url);
                                    Bitmap bitmap = getBitmap(url);
                                    im.setImageBitmap(bitmap);
                                } catch (IOException e2) {
                                    // TODO Auto-generated catch block
                                    e2.printStackTrace();
                                }*/
                            String[] name = {u1.getNiconame()};
                            String[] desc = {u1.getPhone()};
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
}
