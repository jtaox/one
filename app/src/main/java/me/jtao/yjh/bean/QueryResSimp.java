package me.jtao.yjh.bean;

import java.util.List;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class QueryResSimp {

    /**
     * song : [{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","yyr_artist":"0","songname":"小苹果（新年Remix版）","artistname":"筷子兄弟","control":"0000000000","songid":"129552034","has_mv":"0","encrypted_songid":"40077b8cea20956ea5274L"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","yyr_artist":"0","songname":"小苹果 (Little Apple)","artistname":"T-ara,筷子兄弟","control":"0000000000","songid":"124671589","has_mv":"0","encrypted_songid":"970776e56650957999efeL"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","yyr_artist":"0","songname":"小苹果","artistname":"谭咏麟","control":"0000000000","songid":"241886048","has_mv":"0","encrypted_songid":"4107e6ae36009561d467fL"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","yyr_artist":"1","songname":"小苹果","artistname":"Beyond Entertainment Ltd.","control":"0000000000","songid":"74115197","has_mv":"0","encrypted_songid":""},{"bitrate_fee":"{\"0\":\"129|-1\",\"1\":\"-1|-1\"}","yyr_artist":"0","songname":"小苹果","artistname":"温拿乐队","control":"0000000000","songid":"257395359","has_mv":"0","encrypted_songid":"1207f578a9f095640c75dL"},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","yyr_artist":"1","songname":"小苹果","artistname":"魏湘云","control":"0000000000","songid":"74102998","has_mv":"0","encrypted_songid":""},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","yyr_artist":"1","songname":"小苹果(remix) Harmonica.Ver","artistname":"一只猫迷了个路","control":"0000000000","songid":"74014362","has_mv":"0","encrypted_songid":""},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","yyr_artist":"1","songname":"小苹果c调伴奏","artistname":"DANNY","control":"0000000000","songid":"74027321","has_mv":"0","encrypted_songid":""},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","yyr_artist":"1","songname":"小苹果（翻唱）","artistname":"何龙","control":"0100000000","songid":"73896320","has_mv":"0","encrypted_songid":""},{"bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","yyr_artist":"0","songname":"小苹果 (Live)","artistname":"杨洪基,伊一","control":"0000000000","songid":"241934204","has_mv":"0","encrypted_songid":"8307e6b9f7c09568cdbf5L"}]
     * order : song,album
     * error_code : 22000
     * album : [{"albumname":"小苹果（新年Remix版）","artistpic":"http://qukufile2.qianqian.com/data2/pic/129551237/129551237.jpg","albumid":"129552065","artistname":"筷子兄弟"},{"albumname":"最炫小苹果","artistpic":"http://qukufile2.qianqian.com/data2/pic/fa6286aba2b3186bc12795f79bf50a60/261959521/261959521.jpg","albumid":"137407102","artistname":"凤凰传奇,筷子兄弟"},{"albumname":"我是你的 小苹果","artistpic":"http://qukufile2.qianqian.com/data2/pic/122315657/122315657.jpg","albumid":"122315663","artistname":"河静静"}]
     */

    public String order;
    public int error_code;
    /**
     * bitrate_fee : {"0":"0|0","1":"0|0"}
     * yyr_artist : 0
     * songname : 小苹果（新年Remix版）
     * artistname : 筷子兄弟
     * control : 0000000000
     * songid : 129552034
     * has_mv : 0
     * encrypted_songid : 40077b8cea20956ea5274L
     */

    public List<SongBean> song;
    /**
     * albumname : 小苹果（新年Remix版）
     * artistpic : http://qukufile2.qianqian.com/data2/pic/129551237/129551237.jpg
     * albumid : 129552065
     * artistname : 筷子兄弟
     */

    public List<AlbumBean> album;

    public static class SongBean {
        public String bitrate_fee;
        public String yyr_artist;
        public String songname;
        public String artistname;
        public String control;
        public String songid;
        public String has_mv;
        public String encrypted_songid;
    }

    public static class AlbumBean {
        public String albumname;
        public String artistpic;
        public String albumid;
        public String artistname;
    }
}
