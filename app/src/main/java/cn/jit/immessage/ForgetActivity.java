package cn.jit.immessage;

import android.content.Intent;
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
import cn.bmob.v3.listener.UpdateListener;

public class ForgetActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private static pp p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        BmobSMS.initialize(this, "52c7b2c9b75c6bea231965db8248157a");
        et1=(EditText)findViewById(R.id.forget_et1);
        et2=(EditText)findViewById(R.id.forget_et2);
        et3=(EditText)findViewById(R.id.forget_et3);
        et4=(EditText)findViewById(R.id.forget_et4);
        button1=(Button)findViewById(R.id.forget_btn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobSMS.requestSMSCode(ForgetActivity.this,et1.getText().toString(),"IM聊天注册", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer smsId, BmobException ex) {
                        if(ex==null)
                        {
                            Log.e("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                            button1.setClickable(false);
//        mbtn.setBackgroundColor(Color.GRAY);
                            Toast.makeText(ForgetActivity.this, "验证码发送成功，请尽快使用", Toast.LENGTH_SHORT).show();
                            new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
//                Message_btn.setBackgroundResource(R.drawable.button_shape02);
                                    button1.setText(millisUntilFinished / 1000 + "秒");
                                }

                                @Override
                                public void onFinish() {
                                    button1.setClickable(true);
//                Message_btn.setBackgroundResource(R.drawable.button_shape);
                                    button1.setText("重新发送");
                                }
                            }.start();
                        }


                    }
                });
            }
        });
        button2=(Button)findViewById(R.id.forget_btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(RegisterActivity.this,Info1Activity.class);
//                startActivity(intent);
                BmobSMS.verifySmsCode(ForgetActivity.this,et1.getText().toString(), et4.getText().toString(), new VerifySMSCodeListener() {

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
                                            if (object.size() == 0)
                                                Toast.makeText(ForgetActivity.this, "该手机号已注册！", Toast.LENGTH_SHORT).show();
                                            else{
                                                //可以注册
                                                BmobQuery<pp> bmobQuery2 = new BmobQuery<>();
                                                bmobQuery2.addWhereEqualTo("phone", et1.getText());
                                                bmobQuery2.findObjects(new FindListener<pp>(){

                                                    @Override
                                                    public void done(List<pp> list, cn.bmob.v3.exception.BmobException e) {
                                                        for (pp p7 : list) {
                                                            p1=new pp();
                                                            p1.setPasswd(et2.getText().toString());
                                                            p1.update(p7.getObjectId(), new UpdateListener() {
                                                                @Override
                                                                public void done(cn.bmob.v3.exception.BmobException e) {
                                                                    if (e == null){
                                                                        Toast.makeText(ForgetActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();

                                                                        //页面的下一步跳转
                                                                        Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }else
                                                                        Toast.makeText(ForgetActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();

                                                                }
                                                            });
                                                        }
                                                    }
                                                });

                                            }
                                        } else {
                                            Log.i("bmob", "系统异常：" + e.getMessage() + "," + e.getErrorCode());
                                        }
                                    }
                                });
                            }else
                                Toast.makeText(ForgetActivity.this, "两次密码不相同！", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                            Toast.makeText(ForgetActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }
}
