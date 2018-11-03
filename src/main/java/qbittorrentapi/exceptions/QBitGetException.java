package qbittorrentapi.exceptions;

import java.util.Map;

/**
 * Created by Romain on 03/11/2018.
 */
public class QBitGetException extends QBitAPIException {

    private static final long serialVersionUID = 1L;

    public QBitGetException(String url) {
        super("Error getting from url : " + url);
    }

    public QBitGetException(String url, Map<String, String> parameters) {
        super("Error getting from url : " + url + " with parameters : " + parameters.toString());
    }
}
