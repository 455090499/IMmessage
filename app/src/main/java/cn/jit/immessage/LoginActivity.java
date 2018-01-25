package cn.jit.immessage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import qiu.niorgai.StatusBarCompat;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class LoginActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn3;
    private Button btn2;
    private EditText et2;
    private EditText et1;
    private CheckBox cbox;
    private CheckBox cbox2;
    public  boolean isRem=false;
    private boolean isphonerem=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //透明状态栏
        StatusBarCompat.translucentStatusBar(LoginActivity.this);
        //SDK >= 21时, 取消状态栏的阴影
        //StatusBarCompat.translucentStatusBar(LoginActivity.this,true);

        Bmob.initialize(this, "a2994aaba430f692b3d442a44b73a089");
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        btn1=(Button)findViewById(R.id.login_btn1);
        btn2=(Button)findViewById(R.id.login_btn2);
        btn3=(Button)findViewById(R.id.login_btn3);
        et1=(EditText)findViewById(R.id.login_et1);
        et2=(EditText)findViewById(R.id.login_et2);
        cbox=(CheckBox)findViewById(R.id.login_cbox);
        cbox2=(CheckBox)findViewById(R.id.login_cbox2);

        //设置passwd的可见与不可见
        cbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //注释的地方是第二种方法
                if (isChecked) {
                    et2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    //  passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    //   passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //记住密码
        cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor1 = getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                isphonerem=isChecked;
                editor1.putString("isprem",isphonerem?"1":"0");
                editor1.commit();
            }
        });

        SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
        String content = pre.getString("sms_content", "");
        String content1 = pre.getString("sms_content1", "");
        cbox.setChecked(pre.getString("isprem","0").equals("1"));
        if(isRem) {
            et2.setText("" + content1);
        }
        if(isphonerem)
            et1.setText("" + content);


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);

            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // p2.insertpp("13260905105","chenhao");


                BmobQuery<pp> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("phone",et1.getText());
                bmobQuery.addWhereEqualTo("passwd",et2.getText());
                bmobQuery.findObjects(new FindListener<pp>() {
                    @Override
                    public void done(List<pp> object, BmobException e) {
                        if(e==null){
                            if(object.size()==0)
                                Toast.makeText(LoginActivity.this,"账号或密码错误！", Toast.LENGTH_SHORT).show();
                            for (pp p1 : object) {
                                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                editor.putString("sms_content", et1.getText().toString());
                                editor.putString("sms_content1", et2.getText().toString());
                                Body1Activity.islogin=true;
                                editor.putString("islogin","1");
                                editor.commit();
                                Intent intent=new Intent(LoginActivity.this,Body1Activity.class);
                                startActivity(intent);
                                finish();

                            }
                        }else{
                            Toast.makeText(LoginActivity.this,"登陆失败，检查网络！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //设置Editview的图标
        Drawable leftDrawable = et1.getCompoundDrawables()[0];
        if(leftDrawable!=null){
            leftDrawable.setBounds(0, 0, 60, 60);
            et1.setCompoundDrawables(leftDrawable, et1.getCompoundDrawables()[1], et1.getCompoundDrawables()[2], et1.getCompoundDrawables()[3]);
        }
        Drawable leftDrawable1 = et2.getCompoundDrawables()[0];
        if(leftDrawable1!=null){
            leftDrawable1.setBounds(0, 0, 60, 60);
            et2.setCompoundDrawables(leftDrawable1, et2.getCompoundDrawables()[1], et2.getCompoundDrawables()[2], et2.getCompoundDrawables()[3]);
        }



    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }
}
