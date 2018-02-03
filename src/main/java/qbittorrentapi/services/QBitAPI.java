package qbittorrentapi.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import qbittorrentapi.beans.QBitTorrentContent;
import qbittorrentapi.beans.QBitTorrentList;

import javax.naming.Name;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Romain on 28/01/2018.
 */
public class QBitAPI {
    private static final Logger LOGGER = Logger.getLogger(QBitAPI.class.getName());

    private String urlTorrentList;

    private String urlTorrentContent;

    private String urlTorrentDownload;

    private String urlTorrentDelete;

    private String urlPauseAll;

    private String urlResumeAll;

    public QBitAPI(String urlBase) {
        urlTorrentList = urlBase + "/query/torrents";
        urlTorrentContent = urlBase + "/query/propertiesFiles";
        urlTorrentDownload = urlBase + "/command/download";
        urlTorrentDelete = urlBase + "/command/delete";
        urlPauseAll = urlBase + "/command/pauseAll";
        urlResumeAll = urlBase + "/command/resumeAll";
    }

    public QBitTorrentList getTorrentList() throws IOException {
        ObjectMapper mapper = buildObjectMapper();
        LOGGER.log(Level.INFO, "Fetching torrent list : " + urlTorrentList);
        return mapper.readValue(new URL(urlTorrentList), new TypeReference<QBitTorrentList>(){});
    }

    public QBitTorrentList searchTorrents(Map<String, Serializable> parameters) throws IOException {
        ObjectMapper mapper = buildObjectMapper();
        String url = urlTorrentList + "?" + getParamsAsString(parameters);
        LOGGER.log(Level.INFO, "Searching torrents : " + url);
        return mapper.readValue(new URL(url), new TypeReference<QBitTorrentList>(){});
    }

    public List<QBitTorrentContent> getTorrentContents(String hash) throws IOException {
        ObjectMapper mapper = buildObjectMapper();
        String url = urlTorrentContent + "/" + hash;
        LOGGER.log(Level.INFO, "Fetching torrent content : " + url);
        return mapper.readValue(new URL(url), new TypeReference<List<QBitTorrentContent>>(){});
    }

    public void addTorrent(String urls, Map<String, Serializable> parameters) throws IOException {
        parameters.put("urls", urls);
        LOGGER.log(Level.INFO, "Adding torrent : " + urls);
        postRequest(urlTorrentDownload, parameters);
    }

    public void deleteTorrent(String hashes) throws IOException {
        Map<String, Serializable> parameters = new HashMap<String, Serializable>();
        parameters.put("hashes", hashes);
        LOGGER.log(Level.INFO, "Deleting torrent : " + hashes);
        postRequest(urlTorrentDelete, parameters);
    }

    public void pauseAllTorrents() throws IOException {
        LOGGER.log(Level.INFO, "Pausing all torrents");
        postRequest(urlPauseAll, null);
    }

    public void resumeAllTorrents() throws IOException {
        LOGGER.log(Level.INFO, "Resuming all torrents");
        postRequest(urlResumeAll, null);
    }

    private ObjectMapper buildObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    private String getParamsAsString(Map<String, Serializable> parameters) {
        return URLEncodedUtils.format(getParamsAsNameValuePairs(parameters), "utf-8").replace("+", "%20");
    }

    private List<NameValuePair> getParamsAsNameValuePairs(Map<String, Serializable> parameters) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, Serializable> entry : parameters.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
        }
        return params;
    }

    private void postRequest(String url, Map<String, Serializable> parameters) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        if (parameters != null && !parameters.isEmpty()) {
            String entityValue = getParamsAsString(parameters);
            StringEntity entity = new StringEntity(entityValue, "utf-8");
            entity.setContentType(URLEncodedUtils.CONTENT_TYPE);
            post.setEntity(entity);
        }
        client.execute(post);
    }
}
