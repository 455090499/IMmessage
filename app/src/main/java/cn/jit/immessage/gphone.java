package cn.jit.immessage;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;
/**
 * Created by longkang on 2018/1/12.
 */

public class gphone extends BmobObject{

    private String gid;
    private String phone;

    public String getGid() {
        return gid;
    }
    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getPhone(){return phone;}
    public void setPhone(String phone){this.phone=phone;}

    public void insertgphone(String gid,String phone) {
        this.setGid(gid);
        this.setPhone(phone);
        this.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),"群组数据添加成功，返回objectId为："+objectId,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"群组数据添加失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
