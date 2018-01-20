package cn.jit.immessage;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by fyf on 2018/1/19.
 */

public class fileurl extends BmobObject {
    private BmobFile bfile;
    private String filesize;

    public BmobFile getBfile() {
        return bfile;
    }
    public String getFilesize(){
        return  filesize;
    }
    public void setBfile(BmobFile bfile) {
        this.bfile = bfile;
    }
    public void setFilesize(String filesize) {
        this.filesize =filesize;
    }

}
