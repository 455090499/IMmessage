package cn.jit.immessage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

public class CreatActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private EditText et1;
    private EditText et2;
    public ginfo ginfo1=new ginfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat);
        button1=(Button)findViewById(R.id.create_btn1);
        button2=(Button)findViewById(R.id.create_btn2);
        et1=(EditText)findViewById(R.id.create_et1);
        et2=(EditText)findViewById(R.id.create_et2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,2);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ginfo1.getPhoto()!=null)
                    ginfo1.getPhoto().uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                            ginfo1.insertginfo(pre.getString("sms_content", ""),et1.getText().toString(),et2.getText().toString());

                        }
                    });
                else {
                    SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                    ginfo1.insertginfo(pre.getString("sms_content", ""), et1.getText().toString(),et2.getText().toString());
                }
                Intent intent=new Intent(CreatActivity.this,Body1Activity.class);
                startActivity(intent);
                finish();
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
