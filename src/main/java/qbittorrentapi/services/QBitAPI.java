package qbittorrentapi.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import qbittorrentapi.beans.QBitTorrentContent;
import qbittorrentapi.beans.QBitTorrentList;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Romain on 28/01/2018.
 */
public class QBitAPI {
    private static String URL_BASE = "http://localhost:8282";

    private static String URL_TORRENT_LIST = URL_BASE + "/query/torrents";

    private static String URL_TORRENT_CONTENT = URL_BASE + "/query/propertiesFiles";

    private static String URL_TORRENT_DOWNLOAD = URL_BASE + "/command/download";

    private static String URL_TORRENT_DELETE = URL_BASE + "/command/delete";

    private static String URL_PAUSE_ALL = URL_BASE + "/command/pauseAll";

    private static String URL_RESUME_ALL = URL_BASE + "/command/resumeAll";

    public QBitTorrentList getTorrentList() throws IOException {
        ObjectMapper mapper = buildObjectMapper();
        return mapper.readValue(new URL(URL_TORRENT_LIST), new TypeReference<QBitTorrentList>(){});
    }

    public QBitTorrentList searchTorrents(Map<String, Serializable> parameters) throws IOException {
        ObjectMapper mapper = buildObjectMapper();
        return mapper.readValue(new URL(URL_TORRENT_LIST + "?" + getParamsAsString(parameters)), new TypeReference<QBitTorrentList>(){});
    }

    public List<QBitTorrentContent> getTorrentContents(String hash) throws IOException {
        ObjectMapper mapper = buildObjectMapper();
        return mapper.readValue(new URL(URL_TORRENT_CONTENT + "/" + hash), new TypeReference<List<QBitTorrentContent>>(){});
    }

    public void addTorrent(String urls, Map<String, Serializable> parameters) throws IOException {
        List<NameValuePair> params = getParamsAsNameValuePairs(parameters);
        params.add(new BasicNameValuePair("urls", urls));
        postRequest(URL_TORRENT_DOWNLOAD, params);
    }

    public void deleteTorrent(String hashes) throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("hashes", hashes));
        postRequest(URL_TORRENT_DELETE, params);
    }

    public void pauseAllTorrents() throws IOException {
        postRequest(URL_PAUSE_ALL, null);
    }

    public void resumeAllTorrents() throws IOException {
        postRequest(URL_RESUME_ALL, null);
    }

    private ObjectMapper buildObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    private String getParamsAsString(Map<String, Serializable> parameters) {
        return URLEncodedUtils.format(getParamsAsNameValuePairs(parameters), "utf-8");
    }

    private List<NameValuePair> getParamsAsNameValuePairs(Map<String, Serializable> parameters) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        for (Map.Entry<String, Serializable> entry : parameters.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
        }
        return params;
    }

    private void postRequest(String url, List<NameValuePair> parameters) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        if (parameters != null) {
            post.setEntity(new UrlEncodedFormEntity(parameters));
        }
        client.execute(post);
    }
}
