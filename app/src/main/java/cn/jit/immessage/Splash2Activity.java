package cn.jit.immessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                    Intent intent = new Intent(Splash2Activity.this,LoginActivity.class);
                    Splash2Activity.this.startActivity(intent);
                    Splash2Activity.this.finish();
                   // overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                   // overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
