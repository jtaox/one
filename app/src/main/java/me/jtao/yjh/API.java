package me.jtao.yjh;

/**
 * Created by jiangtao on 2016/8/3.
 */
public interface API {

    String DEFAULT_NICKNAME = "路人甲";

    int PAGE_SIZE = 10;

    String PROTOCOL = "http://";
//    String HOST = "123.206.64.118:";
    String HOST = "jtaox.cn/";
//    String HOST = "192.168.5.68:";
//    String PORT = "8080/";
    String FILE = "my3/admin.php?";
    String URL_DRAWER_DESC = PROTOCOL + HOST  + FILE + "controller=getdata&method=getyj";
    /**
     * APP一句话接口 分页处理 +page
     */
    String URL_APP_YJH = PROTOCOL + HOST  + FILE + "controller=getdata&method=getappyjh&pagesize="+ PAGE_SIZE +"&p=";

    /**
     * 妹纸图片 + page
     */
    String URL_APP_MEIZHI = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/";

    /**
     * 音乐 + page
     */
    String ULL_APP_MUSIC = PROTOCOL + HOST  + FILE + "controller=getdata&method=getappmusic&pagesize=" + PAGE_SIZE + "&p=";

    /**
     * 文章 + page
     */
    String URL_APP_ARTICLE = PROTOCOL + HOST + FILE + "controller=getdata&method=getapparticle&pagesize="+ PAGE_SIZE +"&p=";

    /**
     * 获取songid歌曲信息 使用方法
     * MessageFormat.format(messageFormat, songid)
     */
    String URL_APP_MUSIC_INFO = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.song.getInfos&" +
            "format=json&songid={0}&ts=1408284347323&" +
            "e=JoN56kTXnnbEpd9MVczkYJCSx%2FE1mkLx%2BPMIkTcOEu4%3D&" +
            "nw=2&ucf=1&res=1";


    /**
     * 搜索api 使用方法
     * MessageFormat.format(messageFormat, str)
     */
    String URL_APP_QUERY = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&" +
            "method=baidu.ting.search.common&" +
            "format=json&query={0}" +
            "page_no=1&page_size=30";

    /**
     * 搜索建议 使用方法
     * MessageFormat.format(messageFormat, str)
     */
    String URL_APP_QUERY_PROPOSAL="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&" +
            "version=2.1.0&method=baidu.ting.search.catalogSug&" +
            "format=json&" +
            "query={0}";

    /**
     * 搜索建议，只有title，更简洁，建议使用 使用方法
     * MessageFormat.format(messageFormat, str)
     */
    String URL_APP_QUERY_PROPOSAL_SIMPLE = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "method=baidu.ting.search.suggestion&" +
            "query={0}" +
            "format=json&from=ios&version=2.1.1";


    /**
     * 获取歌手信息 使用方法
     * MessageFormat.format(messageFormat, str)
     */
    String URL_APP_SINGER_INFO = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.artist.getinfo&" +
            "format=json&tinguid={0}";

    /**
     * 搜索歌曲 + 关键字
     */
    String URL_APP_QUERY_SIMPLE = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.search.catalogSug&format=json&" +
            "query=";

    String URL_APP_QUERY_DETAIL = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0" +
            "&method=baidu.ting.search.common&format=json&query={0}&page_no={1}&page_size=30";

    String URL_APP_QUERY_ALL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&query=";


    String URL_APP_SEND_PARAM_CONTENT = "content";
    String URL_APP_SEND_PARAM_AUTHOR = "author";
    /**
     * 提交一句话
     * post 方式提交
     *  content 内容
     *  author 作者
     */
    String URL_APP_SEND_YJH = "http://jtaox.cn/my3/admin.php?controller=mobile&method=addyjh";
//    String URL_APP_SEND_YJH = "http://192.168.5.68/yjh2/test2/admin.php?controller=mobile&method=addyjh";



    String URL_APP_SEND_PARAM_DESC = "desc";
    String URL_APP_SEND_PARAM_SONGID = "songid";
    /**
     * 提交音乐分享
     * post 方式提交
     *  desc 描述
     *  author 分享者
     *  songid id
     */
    String URL_APP_SEND_MUSIC = "http://jtaox.cn/my3/admin.php?controller=mobile&method=addmusic";

}
