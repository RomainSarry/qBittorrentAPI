package qbittorrentapi.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import qbittorrentapi.beans.QBitTorrent;
import qbittorrentapi.beans.QBitTorrentContent;
import qbittorrentapi.exceptions.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by Romain on 28/01/2018.
 */
public class QBitAPI {

    private static final String UTF_8 = "UTF-8";

    private String urlString;

    private String sessionId;

    public QBitAPI(String urlString) throws QBitURLException, QBitLoginException, QBitParametersException {
        this(urlString, null, null);
    }

    public QBitAPI(String urlString, String username, String password) throws QBitURLException, QBitLoginException, QBitParametersException {
        this.urlString = urlString;

        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", username);
            parameters.put("password", password);

            postRequest(urlString + "/login", parameters);

            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
            for (HttpCookie cookie : cookies) {
                if (cookie.getName().equals("SID")) {
                    sessionId = cookie.getValue();
                }
            }
        } catch (QBitPostException e) {
            throw new QBitURLException(urlString);
        }

        if (sessionId == null) {
            throw new QBitLoginException(username);
        }
    }

    public List<QBitTorrent> getTorrentList(Map<String, String> parameters) throws QBitTorrentsFetchingException, QBitParametersException {
        String url = null;

        try {
            return getRequest(urlString + "/query/torrents" + getParamsAsString(parameters), new TypeReference<List<QBitTorrent>>() {});
        } catch (QBitParametersException e) {
            throw e;
        } catch (Exception e) {
            throw new QBitTorrentsFetchingException(url);
        }
    }

    public List<QBitTorrentContent> getTorrentContents(String hash) throws QBitTorrentsFetchingException {
        String url = null;

        try {
            return getRequest(urlString + "/query/propertiesFiles/" + hash, new TypeReference<List<QBitTorrentContent>>() {});
        } catch (Exception e) {
            throw new QBitTorrentsFetchingException(url);
        }
    }

    public void addTorrents(List<String> urls, Map<String, String> parameters) throws QBitParametersException, QBitPostException {
        parameters.put("urls", String.join("\r\n", urls));
        postRequest(urlString + "/command/download", parameters);
    }

    public void deleteTorrents(List<String> hashes) throws QBitParametersException, QBitPostException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("hashes", String.join("|", hashes));
        postRequest(urlString + "/command/delete", parameters);
    }

    public void pauseAllTorrents() throws QBitParametersException, QBitPostException {
        postRequest(urlString + "/command/pauseAll", null);
    }

    public void resumeAllTorrents() throws QBitParametersException, QBitPostException {
        postRequest(urlString + "/command/resumeAll", null);
    }

    private String getParamsAsString(Map<String, String> parameters) throws QBitParametersException {
        try {
            List<NameValuePair> paramsAsNameValuePairs = new LinkedList<NameValuePair>();
            if (parameters != null && !parameters.isEmpty()) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    paramsAsNameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }

            return URLEncodedUtils.format(paramsAsNameValuePairs, UTF_8);
        } catch (Exception e) {
            throw new QBitParametersException(parameters);
        }
    }

    private <T extends Object> T getRequest(String url, TypeReference<T> type) throws QBitGetException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Cookie", "SID=" + sessionId);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(connection.getInputStream(), type);
        } catch (Exception e) {
            throw new QBitGetException(url);
        }
    }

    private void postRequest(String url, Map<String, String> parameters) throws QBitParametersException, QBitPostException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Cookie", "SID=" + sessionId);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
            writer.write(getParamsAsString(parameters));
            writer.flush();
            writer.close();
            os.close();

            connection.getInputStream();
        } catch (QBitParametersException e) {
            throw e;
        } catch (Exception e) {
            if (parameters == null || parameters.isEmpty()) {
                throw new QBitPostException(url);
            } else {
                throw new QBitPostException(url, parameters);
            }
        }
    }
}
