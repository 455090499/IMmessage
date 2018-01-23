package cn.jit.immessage;

import android.widget.Toast;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by fyf on 2018/1/18.
 */

public class inform extends BmobObject {
    private String phone;
    private String sphone;
    private String content;
    private String photo;


    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo){
        this.photo=photo;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getSphone() { return sphone;  }
    public void setSphone(String sphone) {
        this.sphone = sphone;
    }
    public String getContent() { return content; }
    public void setContent(String content){ this.content=content; }

    public void insertinform(String phone,String sphone,String content,String photo) {

        this.setPhone(phone);
        this.setSphone(sphone);
        this.setContent(content);
        this.setPhoto(photo);

        this.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
//                    Toast.makeText(getApplicationContext(),"添加消息数据成功，返回objectId为："+objectId,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"创建inform失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}
