package cn.jit.immessage;

import android.widget.Toast;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by User on 2018/1/5.
 */

public class uinfo extends BmobObject {
    private String phone;
    private String niconame;
    private String sex;
    private String birth;
    private String email;
    private String ps;
    private Object photo;

        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }

    public String getNiconame() {
        return niconame;
    }
    public void setNiconame(String niconame) {
        this.niconame = niconame;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPs() {
        return ps;
    }
    public void setPs(String ps) {
        this.ps = ps;
    }

    public Object getPhoto() {
        return photo;
    }
    public void setPhoto(Object photo) {
        this.photo = photo;
    }

        public void insertuinfo(String phone,String niconame,String sex, String birth, String email,String ps,Object photo) {

            this.setPhone(phone);
            this.setNiconame(niconame);
            this.setSex(sex);
            this.setBirth(birth);
            this.setEmail(email);
            this.setPs(ps);
            this.setPhoto(photo);
            this.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if(e==null){
                        Toast.makeText(getApplicationContext(),"添加数据成功，返回objectId为："+objectId,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

            });




    }
}
