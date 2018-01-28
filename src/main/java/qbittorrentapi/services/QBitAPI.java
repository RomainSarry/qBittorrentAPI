package qbittorrentapi.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import qbittorrentapi.beans.QBitTorrentList;

import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Romain on 28/01/2018.
 */
public class QBitAPI {
    private static String URL_BASE = "http://localhost:8282";

    private static String URL_TORRENT_LIST = URL_BASE + "/query/torrents";

    private QBitTorrentList getTorrentListFromUrl(String url) {
        QBitTorrentList torrentList = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            torrentList = mapper.readValue(new URL(url), new TypeReference<QBitTorrentList>(){});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return torrentList;
        }
    }

    public QBitTorrentList getTorrentList() {
        return getTorrentListFromUrl(URL_TORRENT_LIST);
    }

    public QBitTorrentList getTorrentList(Map<String, Serializable> parameters) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        for (Map.Entry<String, Serializable> entry : parameters.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
        }
        String paramString = URLEncodedUtils.format(params, "utf-8");

        return getTorrentListFromUrl(URL_TORRENT_LIST + "?" + paramString);
    }
}
