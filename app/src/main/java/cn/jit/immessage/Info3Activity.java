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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static cn.jit.immessage.Body1Activity.rff;

public class Info3Activity extends AppCompatActivity {
    private  TextView text;
    private EditText edt1;
    private EditText edt2;
    private EditText edt3;
    private String isex ="";
    private RadioButton rb1;
    private RadioButton rb2;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private uinfo ufo2=new uinfo();
    private boolean fphoto=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info3);
        btn1=(Button)findViewById(R.id.info3_btn1);
        btn2=(Button)findViewById(R.id.info3_btn2);
        btn3=(Button)findViewById(R.id.info3_btn3);
        btn4=(Button)findViewById(R.id.info3_btn4);
        edt1=(EditText)findViewById(R.id.info3_et1);
        edt2=(EditText)findViewById(R.id.info3_et2);
        edt3=(EditText)findViewById(R.id.info3_et3);
        text=(TextView)findViewById(R.id.info3_tv1);
        rb1=(RadioButton)findViewById(R.id.info3_rb1);
        rb2=(RadioButton)findViewById(R.id.info3_rb2);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.info3_sex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                isex=radioButton.getText().toString();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fphoto=true;
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,2);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(Info3Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        text.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pre = getSharedPreferences("user", MODE_PRIVATE);
                String content = pre.getString("sms_content", "");

                BmobQuery<uinfo> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("phone", content);
                bmobQuery.findObjects(new FindListener<uinfo>() {
                    @Override
                    public void done(List<uinfo> list, BmobException e) {
                        for (final uinfo u1 : list) {
                            if(fphoto) {

                                BmobFile bmobfile = u1.getPhoto();
                                String url = bmobfile.getFileUrl();
                                bmobfile.setUrl(url);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
                                bmobfile.delete(new UpdateListener() {

                                    @Override
                                    public void done(BmobException e) {

                                            if(ufo2.getPhoto()!=null) {
                                                ufo2.getPhoto().uploadblock(new UploadFileListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        u1.setValue("photo", ufo2.getPhoto());
                                                        Toast.makeText(Info3Activity.this, "done" + u1.getObjectId(), Toast.LENGTH_SHORT).show();

                                                        Toast.makeText(Info3Activity.this, "done" + isex, Toast.LENGTH_SHORT).show();
                                                        if(!edt1.getText().toString().equals(""))
                                                            u1.setValue("niconame", edt1.getText().toString());
                                                        if(!isex.equals(""))
                                                            u1.setValue("sex", isex);
                                                        if(!text.getText().toString().equals(""))
                                                            u1.setValue("birth", text.getText().toString());
                                                        if(!edt2.getText().toString().equals(""))
                                                            u1.setValue("email", edt2.getText().toString());
                                                        if(!edt3.getText().toString().equals(""))
                                                            u1.setValue("ps", edt3.getText().toString());

                                                        u1.update(u1.getObjectId(), new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                                if (e == null) {
                                                                    Log.i("bmob", "更新成功");
                                                                    try {
                                                                        fphoto=false;
                                                                        Toast.makeText(Info3Activity.this, "正在保存" + u1.getObjectId(), Toast.LENGTH_LONG).show();
                                                                        Thread.sleep(1000L);

                                                                        finish();
                                                                    } catch (InterruptedException e1) {
                                                                        e1.printStackTrace();
                                                                    }

                                                                } else {
                                                                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }else
                                            {
                                                Toast.makeText(Info3Activity.this,"done"+u1.getObjectId(),Toast.LENGTH_SHORT).show();


                                            }

                                    }
                                });
                            }else{

                                Toast.makeText(Info3Activity.this, "done" + isex, Toast.LENGTH_SHORT).show();
                                if(!edt1.getText().toString().equals(""))
                                    u1.setValue("niconame", edt1.getText().toString());
                                if(!isex.equals(""))
                                    u1.setValue("sex", isex);
                                if(!text.getText().toString().equals(""))
                                    u1.setValue("birth", text.getText().toString());
                                if(!edt2.getText().toString().equals(""))
                                    u1.setValue("email", edt2.getText().toString());
                                if(!edt3.getText().toString().equals(""))
                                    u1.setValue("ps", edt3.getText().toString());

                                u1.update(u1.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Log.i("bmob", "更新成功");
                                            try {
                                                Toast.makeText(Info3Activity.this, "运行成功" + u1.getObjectId(), Toast.LENGTH_LONG).show();

                                                Thread.sleep(1000L);

                                                finish();
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }

                                        } else {
                                            Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                        }
                                    }
                                });
                            }

                            Log.d("d","done:"+u1.getObjectId() );


                        }
                    }
                });

            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Info3Activity.this,Info2Activity.class);
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
            final  BmobFile icon = new BmobFile(new File(cursor.getString(columnIndex)));
            ufo2.setPhoto(icon);

            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.info3_im);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

}
