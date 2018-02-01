package qbittorentapi.services;

import org.junit.Before;
import org.junit.Test;
import qbittorrentapi.beans.QBitTorrentContent;
import qbittorrentapi.beans.QBitTorrentList;
import qbittorrentapi.services.QBitAPI;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Romain on 28/01/2018.
 */
public class QBitAPITest {
    QBitAPI qBitAPI;

    QBitTorrentList qBitTorrentList;

    List<QBitTorrentContent> qBitTorrentContentList;

    QBitTorrentContent qBitTorrentContent;

    @Before
    public void before() {
        qBitAPI = new QBitAPI("http://localhost:8282");
    }

    @Test
    public void getTorrentList() {
        try {
            qBitTorrentList = qBitAPI.getTorrentList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTorrentListWithParams() {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("category", "Série");
        params.put("sort", "name");

        try {
            qBitTorrentList = qBitAPI.searchTorrents(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTorrentContents() {
        try {
            qBitTorrentContentList = qBitAPI.getTorrentContents("88d9f48e75e8a5a737394f44bf8e977e996b1e94");
            qBitTorrentContent = qBitTorrentContentList.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addTorrent() {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("category", "VO");
        params.put("savepath", "B:\\Videos\\Films");
        params.put("rename", "Thor-Ragnarok");
        params.put("paused", "false");

        try {
            qBitAPI.addTorrent("magnet:?xt=urn:btih:9581b8c8ff8dc070e672bb56fe0ac9908789dc39&dn=Thor.Ragnarok.2017.1080p.WEB-DL.X264.AC3-EVO&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Fzer0day.ch%3A1337&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969",
                    params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTorrent() {
        try {
            qBitAPI.deleteTorrent("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
