package qbittorrentapi.exceptions;

/**
 * Created by Romain on 03/11/2018.
 */
public class QBitAPIException extends Exception {

    private static final long serialVersionUID = 1L;

    public QBitAPIException(String message) {
        super(message);
    }
}
