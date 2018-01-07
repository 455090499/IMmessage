package cn.jit.immessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Info2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info2);
        TextView textView=(TextView)findViewById(R.id.toptitle_tv);
        Button btn1=(Button)findViewById(R.id.toptitle_btn1);
        Button btn2=(Button)findViewById(R.id.info2_btn2);
        textView.setText("个人信息");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Info2Activity.this,Info3Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
