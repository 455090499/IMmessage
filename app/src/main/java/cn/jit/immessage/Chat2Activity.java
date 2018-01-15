package cn.jit.immessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import qiu.niorgai.StatusBarCompat;

public class Chat2Activity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private TextView tv1;
    private String desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        //透明状态栏
        StatusBarCompat.translucentStatusBar(Chat2Activity.this);
        //SDK >= 21时, 取消状态栏的阴影
        StatusBarCompat.translucentStatusBar(Chat2Activity.this,true);

        btn1=(Button)findViewById(R.id.chat2_btn1);
        btn2=(Button)findViewById(R.id.chat2_btn2);
        tv1=(TextView) findViewById(R.id.chat2_tv1);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");
        Toast.makeText(Chat2Activity.this,name,Toast.LENGTH_SHORT).show();
        tv1.setText(name);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat2Activity.this,Info5Activity.class);
                intent.putExtra("desc",desc);
                startActivity(intent);
            }
        });

    }
}



