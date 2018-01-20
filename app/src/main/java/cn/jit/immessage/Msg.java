package cn.jit.immessage;

/**
 * Created by liweiwei on 2018/1/5.
 */
public class Msg {
    public static final int TYPE_RECEIVCED = 0;
    public static final int TYPE_SENT = 1;
    public static final int FILE_SENT = 2;
    public static final int FILE_RECV = 3;

    private String content;
    private int type;
    private String url;
    private String filename;
    private String filesize;
    private String filestate;


    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }
    public Msg(String content, int type,String url) {
        this.content = content;
        this.type = type;
        this.url=url;
    }
    public Msg(String content, int type,String url,String filename,String filesize,String filestate) {
        this.content = content;
        this.type = type;
        this.url=url;
        this.filename=filename;
        this.filesize=filesize;
        this.filestate=filestate;
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
}
