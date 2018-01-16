package cn.jit.immessage;

/**
 * Created by liweiwei on 2018/1/5.
 */
public class Msg {
    public static final int TYPE_RECEIVCED = 0;
    public static final int TYPE_SENT = 1;
    private String content;
    private int type;
    private String url;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }
    public Msg(String content, int type,String url) {
        this.content = content;
        this.type = type;
        this.url=url;
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
}
