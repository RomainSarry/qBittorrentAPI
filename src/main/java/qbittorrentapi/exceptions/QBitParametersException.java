package qbittorrentapi.exceptions;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Romain on 02/11/2018.
 */
public class QBitParametersException extends QBitAPIException {

    private static final long serialVersionUID = 1L;

    public QBitParametersException(Map<String, String> parameters) {
        super("Error parsing parameters : " + parameters.toString());
    }
}
