package cn.jit.immessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import qiu.niorgai.StatusBarCompat;

public class ShowAdviceActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_advice);
        //透明状态栏
        StatusBarCompat.translucentStatusBar(ShowAdviceActivity.this);
        //SDK >= 21时, 取消状态栏的阴影
        StatusBarCompat.translucentStatusBar(ShowAdviceActivity.this,true);
        btn1=(Button)findViewById(R.id.showadvice_btn1);
        btn2=(Button)findViewById(R.id.showadvice_btn2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowAdviceActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ShowAdviceActivity.this,ShowAdvice2Activity.class);
                startActivity(intent);

            }
        });
    }
}
