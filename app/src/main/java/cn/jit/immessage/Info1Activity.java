package cn.jit.immessage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import qiu.niorgai.StatusBarCompat;

public class Info1Activity extends AppCompatActivity {
    private Button button2;
    private Button button3;
//    private Button button1;
    private TextView text;
    private EditText et1;
    private RadioButton rb1;
    private RadioButton rb2;
    private ImageView im;
    private EditText et2;
    private EditText et3;
    private String isex = "";
    public uinfo ufo1;
    public String img_url=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info1);

        //透明状态栏
        StatusBarCompat.translucentStatusBar(Info1Activity.this);
        //SDK >= 21时, 取消状态栏的阴影
        //StatusBarCompat.translucentStatusBar(LoginActivity.this,true);

        ufo1 = new uinfo();
        im=(ImageView)findViewById(R.id.info1_im);
        button2=(Button)findViewById(R.id.info1_btn2);
//        button1=(Button)findViewById(R.id.info1_btn1);
        button3=(Button)findViewById(R.id.info1_btn3);
        text=(TextView)findViewById(R.id.info1_tv1);
        et1=(EditText) findViewById(R.id.info1_et1);
        et2=(EditText) findViewById(R.id.info1_et2);
        et3=(EditText) findViewById(R.id.info1_et3);
        rb1=(RadioButton)findViewById(R.id.info1_rb1);
        rb2=(RadioButton)findViewById(R.id.info1_rb2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(Info1Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        text.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,2);
                img_url=i.getStringExtra("123");
                Toast.makeText(Info1Activity.this, img_url, Toast.LENGTH_SHORT).show();
            }
        });
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.info1_sex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                isex=radioButton.getText().toString();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ufo1.getPhoto()!=null)
                    ufo1.getPhoto().uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        Log.d("1","1");
                        isex="".equals("")?"男":isex;
                        SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                        ufo1.insertuinfo(pre.getString("sms_content", ""),et1.getText().toString(),isex,text.getText().toString(),et2.getText().toString(),et3.getText().toString());

                    }
                });
                else {
                    Log.d("2","2");
                    isex = "".equals("") ? "男" : isex;
                    SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                    ufo1.insertuinfo(pre.getString("sms_content", ""), et1.getText().toString(), isex, text.getText().toString(), et2.getText().toString(), et3.getText().toString());
                }
//                    Intent intent=new Intent(Info1Activity.this,LoginActivity.class);
//                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(Info1Activity.this,"请把信息填写完成",Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            final BmobFile icon = new BmobFile(new File(cursor.getString(columnIndex)));
            ufo1.setPhoto(icon);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.info1_im);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));


        }else
        {
            ufo1.setPhoto(null);
        }
    }

}
