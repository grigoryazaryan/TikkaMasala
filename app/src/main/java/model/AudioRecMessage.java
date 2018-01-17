package model;

/**
 * Created by grigory on 17/01/18.
 */

public class AudioRecMessage extends ChatMessage {

    String dataBase64;

    public AudioRecMessage() {
        this.type = TYPE_AUDIO_REC;
    }

    public AudioRecMessage(String dataBase64) {
        this();
        this.dataBase64 = dataBase64;
    }

    public String getDataBase64() {
        return dataBase64;
    }

    public void setDataBase64(String dataBase64) {
        this.dataBase64 = dataBase64;
    }
}
