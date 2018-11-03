package qbittorrentapi.exceptions;

/**
 * Created by Romain on 28/10/2018.
 */
public class QBitTorrentsFetchingException extends QBitAPIException {

	private static final long serialVersionUID = 1L;

    public QBitTorrentsFetchingException(String urlString) {
        super("Error fetching torrents at URL : " + urlString);
    }
}
