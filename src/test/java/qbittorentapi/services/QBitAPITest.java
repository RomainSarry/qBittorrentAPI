package qbittorentapi.services;

import org.junit.Before;
import org.junit.Test;
import qbittorrentapi.beans.QBitTorrentList;
import qbittorrentapi.services.QBitAPI;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Romain on 28/01/2018.
 */
public class QBitAPITest {
    QBitTorrentList qBitTorrentList;

    QBitAPI qBitAPI;

    @Before
    public void before() {
        qBitAPI = new QBitAPI();
    }

    @Test
    public void getTorrentList() {
        qBitTorrentList = qBitAPI.getTorrentList();
    }

    @Test
    public void getTorrentListWithParams() {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("category", "Série");
        params.put("sort", "name");

        qBitTorrentList = qBitAPI.getTorrentList(params);
    }
}
