package qbittorrentapi.beans;

/**
 * Created by Romain on 29/01/2018.
 */
public class QBitTorrentContent {
    private String name;

    private Float progress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getProgress() {
        return progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }
}
