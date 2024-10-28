package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/** Represents a gitlet commit object.
 *  It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Frank
 */
public class Commit implements Serializable {
    /**
     * add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String parent;
    private String timestamp;
    private HashMap<String, String> blobs; // 存储文件名到文件内容哈希的映射

    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        if (this.parent == null) {
            Date date = new Date(0);
            this.timestamp = dateTotimestamp(date);
        } else {
            this.timestamp = dateTotimestamp(new Date());
        }
        this.blobs = new HashMap<>(); //初始化blobs
    }

    public HashMap<String, String> getBlobs() {
        return blobs;
    }

    public String getMessage() {
        return this.message;
    }
    public String getParent() {
        return this.parent;
    }
    public String getTimestamp() {
        return this.timestamp;
    }

    /** branch */


    //below is the realization of the timestamp

    private static String dateTotimestamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    public void setBlobs(HashMap<String, String> blobs) {
        this.blobs = blobs;
    }
}
