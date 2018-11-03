package qbittorrentapi.exceptions;

/**
 * Created by Romain on 28/10/2018.
 */
public class QBitLoginException extends QBitAPIException {

	private static final long serialVersionUID = 1L;

    public QBitLoginException(String username) {
        super("Cannot connect to qBittorrent with user : " + username);
    }
}
