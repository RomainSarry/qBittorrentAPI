package qbittorrentapi.beans;

import java.util.List;

/**
 * Created by Romain on 28/01/2018.
 */
public class QBitTorrent {
    private String category;

    private List<QBitTorrentContent> content;

    private String hash;

    private String name;

    private Float progress;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<QBitTorrentContent> getContent() {
        return content;
    }

    public void setContent(List<QBitTorrentContent> content) {
        this.content = content;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

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
