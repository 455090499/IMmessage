package cn.jit.immessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class LoginActivity extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private EditText et2;
    private EditText et1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Bmob.initialize(this, "a2994aaba430f692b3d442a44b73a089");
        btn1=(Button)findViewById(R.id.login_btn1);
        btn2=(Button)findViewById(R.id.login_btn2);
        et1=(EditText)findViewById(R.id.login_et1);
        et2=(EditText)findViewById(R.id.login_et2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
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
                                Toast.makeText(LoginActivity.this,"密码错误！", Toast.LENGTH_SHORT).show();
                            for (pp p1 : object) {
                                    Intent intent = new Intent(LoginActivity.this, Body1Activity.class);
                                    startActivity(intent);
                                Toast.makeText(LoginActivity.this,p1.getObjectId()+","+et2.getText(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Log.i("bmob","登录失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });


    }
}
