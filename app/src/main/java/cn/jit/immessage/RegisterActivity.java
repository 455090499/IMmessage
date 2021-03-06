package cn.jit.immessage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import qiu.niorgai.StatusBarCompat;

import static cn.jit.immessage.Body1Activity.p1;

public class RegisterActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //透明状态栏
        StatusBarCompat.translucentStatusBar(RegisterActivity.this);
        //SDK >= 21时, 取消状态栏的阴影
        //StatusBarCompat.translucentStatusBar(LoginActivity.this,true);

        BmobSMS.initialize(this, "52c7b2c9b75c6bea231965db8248157a");
        et1=(EditText)findViewById(R.id.register_et1);
        et2=(EditText)findViewById(R.id.register_et2);
        et3=(EditText)findViewById(R.id.register_et3);
        et4=(EditText)findViewById(R.id.register_et4);
        button1=(Button)findViewById(R.id.register_btn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.requestSMSCode(RegisterActivity.this,et1.getText().toString(),"IM聊天注册", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        if(ex==null)
                        {
                            Log.e("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                            //button 60s 倒计时
                            button1.setClickable(false);
                            Toast.makeText(RegisterActivity.this, "验证码发送成功，请尽快使用", Toast.LENGTH_SHORT).show();
                            new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    button1.setText(millisUntilFinished / 1000 + "秒");
                                }

                                @Override
                                public void onFinish() {
                                    button1.setClickable(true);
                                    button1.setText("重新发送");
                                }
                            }.start();
                        }


                    }
                });
            }
        });
        button2=(Button)findViewById(R.id.register_btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.verifySmsCode(RegisterActivity.this,et1.getText().toString(), et4.getText().toString(), new VerifySMSCodeListener() {

                    @Override
                    public void done(BmobException ex) {
                        // TODO Auto-generated method stub
                        if(ex==null){//短信验证码已验证成功
                            Log.i("bmob", "验证通过");
                            if(et2.getText().toString().equals(et3.getText().toString())) {
                                BmobQuery<pp> bmobQuery = new BmobQuery<>();
                                bmobQuery.addWhereEqualTo("phone", et1.getText());
                                bmobQuery.findObjects(new FindListener<pp>() {
                                    @Override
                                    public void done(List<pp> object, cn.bmob.v3.exception.BmobException e) {
                                        if (e == null) {
                                            if (object.size() == 0){
                                                //可以注册
                                                p1=new pp();
                                                p1.insertpp(et1.getText().toString(),et2.getText().toString());
                                                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                                editor.putString("sms_content", et1.getText().toString());
                                                editor.commit();
                                                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                                //页面的下一步跳转
                                                Intent intent = new Intent(RegisterActivity.this, Info1Activity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                                Toast.makeText(RegisterActivity.this, "该手机号已注册！", Toast.LENGTH_SHORT).show();
                                        } else {

                                        }
                                    }
                                });
                            }else
                                Toast.makeText(RegisterActivity.this, "两次密码不相同！", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }
}
