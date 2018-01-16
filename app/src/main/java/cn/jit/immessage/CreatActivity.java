package cn.jit.immessage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import qiu.niorgai.StatusBarCompat;

public class CreatActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private EditText et1;
    private EditText et2;
    private ImageView img;
    public ginfo ginfo1=new ginfo();
    private gphone gphone=new gphone();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat);

        //透明状态栏
        StatusBarCompat.translucentStatusBar(CreatActivity.this);
        //SDK >= 21时, 取消状态栏的阴影
        StatusBarCompat.translucentStatusBar(CreatActivity.this,true);

        img=(ImageView)findViewById(R.id.create_im);
        button1=(Button)findViewById(R.id.create_btn1);
        button2=(Button)findViewById(R.id.create_btn2);
        et1=(EditText)findViewById(R.id.create_et1);
        et2=(EditText)findViewById(R.id.create_et2);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,2);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et2.getText().toString().equals("") && et1.getText().toString().length() == 8) {
                    if (ginfo1.getPhoto() != null)
                        ginfo1.getPhoto().uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                                ginfo1.insertginfo(pre.getString("sms_content", ""), et1.getText().toString(), et2.getText().toString());
                                gphone.insertgphone(et1.getText().toString(), pre.getString("sms_content", ""));
                                Intent intent = new Intent(CreatActivity.this, AddgroupActivity.class);
                                String name = et1.getText().toString();
                                intent.putExtra("name", name);
                                startActivity(intent);
                                finish();

                            }
                        });
                    else {
                        Toast.makeText(CreatActivity.this, "请添加头像", Toast.LENGTH_SHORT).show();
//                    SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
//                    ginfo1.insertginfo(pre.getString("sms_content", ""), et1.getText().toString(),et2.getText().toString());
//                    gphone.insertgphone(et1.getText().toString(),pre.getString("sms_content", ""));

                    }

                }else{
                    Toast.makeText(CreatActivity.this, "请按要求填写信息", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            final BmobFile icon = new BmobFile(new File(cursor.getString(columnIndex)));
            ginfo1.setPhoto(icon);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.create_im);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
}
