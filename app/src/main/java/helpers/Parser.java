package helpers;

import com.google.gson.GsonBuilder;

import model.AudioRecMessage;
import model.ChatMessage;
import model.TextMessage;

/**
 * Created by grigory on 17/01/18.
 */

public class Parser {
    public static ChatMessage parseChatMessageFromGson(String message) {
        ChatMessage msg = new GsonBuilder().create().fromJson(message, ChatMessage.class); //todo improve. creates one excess object
        switch (msg.getType()) {
            case ChatMessage.TYPE_TEXT:
                return new GsonBuilder().create().fromJson(message, TextMessage.class);
            case ChatMessage.TYPE_AUDIO_REC:
                return new GsonBuilder().create().fromJson(message, AudioRecMessage.class);
            default:
                return msg;
        }
    }

    public static String stringifyJsonChatMessage(ChatMessage message) {
        return new GsonBuilder().create().toJson(message);
    }
}
