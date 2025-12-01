package vpunko.spotify.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageRequestEvent implements Serializable {

    public UserMessageRequestEvent(long chatId, String message) {
        this.chatId = chatId;
        this.message = message;
    }

    long chatId;
    String message;
    boolean forTest;
}