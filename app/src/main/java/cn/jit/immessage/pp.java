package cn.jit.immessage;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by User on 2018/1/4.
 */

public class pp extends BmobObject {

        private String phone;
        private String passwd;

        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
        public String getPasswd() {
         return passwd;
        }
        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }
        public void insertpp(String phone,String passwd) {

            this.setPhone(phone);
            this.setPasswd(passwd);
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
