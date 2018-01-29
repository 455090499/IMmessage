package cn.jit.immessage;

/**
 * Created by liweiwei on 2018/1/5.
 */
public class Msg {
    public static final int TYPE_RECEIVCED = 0;
    public static final int TYPE_SENT = 1;
    public static final int FILE_SENT = 2;
    public static final int FILE_RECV = 3;
    public static final int IMG_SENT = 4;
    public static final int IMG_RECV = 5;
    public static final int VOICE_SENT = 6;
    public static final int VOICE_RECV = 7;



    private String content;
    private int type;
    private String url;
    private String filename;
    private String filesize;
    private String filestate;
    private String fileobject;
    private String img;
    private int pic;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }
    public Msg(String content, int type,String url) {
        this.content = content;
        this.type = type;
        this.url=url;
    }
    public Msg(String content, int type,String url,String img,int pic) {
        this.content = content;
        this.type = type;
        this.url=url;
        this.img=img;
        this.pic=pic;
    }
    public Msg(String content, int type,String url,String fileobject) {
        this.content = content;
        this.type = type;
        this.url=url;
        this.fileobject=fileobject;
    }
    public Msg(String content, int type,String url,String filename,String filesize,String filestate) {
        this.content = content;
        this.type = type;
        this.url=url;
        this.filename=filename;
        this.filesize=filesize;
        this.filestate=filestate;
    }
    public Msg(String content, int type,String url,String filename,String filesize,String filestate,String fileobject) {
        this.content = content;
        this.type = type;
        this.url=url;
        this.filename=filename;
        this.filesize=filesize;
        this.filestate=filestate;
        this.fileobject=fileobject;

    }
    public String getContent() {
        return content;
    }
    public String getUrl() {
        return url;
    }
    public int getType() {
        return  type;
    }
    public String getFilename() { return  filename;}
    public String getFilesize() { return  filesize;}
    public String getFilestate() { return  filestate;}
    public String getFileobject() { return  fileobject;}
    public String getImg() {return img;}

}
