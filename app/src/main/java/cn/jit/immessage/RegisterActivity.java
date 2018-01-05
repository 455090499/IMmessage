package cn.jit.immessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class RegisterActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private EditText et1;
    private EditText et4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        BmobSMS.initialize(this, "52c7b2c9b75c6bea231965db8248157a");
        et1=(EditText)findViewById(R.id.register_et1);
        et4=(EditText)findViewById(R.id.register_et4);
        button1=(Button)findViewById(R.id.register_btn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.requestSMSCode(RegisterActivity.this,et1.getText().toString(),"IM聊天注册", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        if(ex==null)
                            Log.e("bmob", "短信id：" + smsId);//用于查询本次短信发送详情

                    }
                });
            }
        });
        button2=(Button)findViewById(R.id.register_btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,Info1Activity.class);
                startActivity(intent);
                BmobSMS.verifySmsCode(RegisterActivity.this,et1.getText().toString(), et4.getText().toString(), new VerifySMSCodeListener() {

                    @Override
                    public void done(BmobException ex) {
                        // TODO Auto-generated method stub
                        if(ex==null){//短信验证码已验证成功
                            Log.i("bmob", "验证通过");
                            //页面的下一步跳转
                           // Intent intent=new Intent(RegisterActivity.this,Info1Activity.class);
                            //startActivity(intent);
                        }else{
                            Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                            Toast.makeText(RegisterActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
