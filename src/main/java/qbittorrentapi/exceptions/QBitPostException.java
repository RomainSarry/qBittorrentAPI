package qbittorrentapi.exceptions;

import java.util.Map;

/**
 * Created by Romain on 03/11/2018.
 */
public class QBitPostException extends QBitAPIException {

    private static final long serialVersionUID = 1L;

    public QBitPostException(String url) {
        super("Error posting to url : " + url);
    }

    public QBitPostException(String url, Map<String, String> parameters) {
        super("Error posting to url : " + url + " with parameters : " + parameters.toString());
    }
}
