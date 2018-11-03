package qbittorrentapi.exceptions;

/**
 * Created by Romain on 28/10/2018.
 */
public class QBitURLException extends QBitAPIException {

	private static final long serialVersionUID = 1L;

    public QBitURLException(String urlString) {
        super("Cannot connect to qBittorrent with URL : " + urlString);
    }
}
