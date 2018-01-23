package cn.jit.immessage;

import android.widget.Toast;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by fyf on 2018/1/10.
 */

public class pfriend extends BmobObject {
    private String phone;
    private String fphone;

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getFphone() {
        return fphone;
    }
    public void setFphone(String fphone) {this.fphone = fphone;}

    public void insertpfriend(String phone,String fphone) {

        this.setPhone(phone);
        this.setFphone(fphone);

        this.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){

                }else{
                    Toast.makeText(getApplicationContext(),"好友申请发送失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

}
