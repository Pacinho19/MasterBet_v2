package pl.pacinho.MasterBet.model.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum PasswordMessage {
    OLD_NEW_PASS_EQUALS("New Password must be distinct from current password!", 1),
    OLD_PASS_WRONG("Current Password is wrong !", 2),
    OTHER_ERROR("Unidentified error.", 3);

    @Getter
    private String message;
    @Getter
    private int id;

    public static PasswordMessage byId(int id) {
        return Arrays.stream(PasswordMessage.values())
                .filter(passwordMessage -> passwordMessage.id == id)
                .findFirst()
                .orElse(OTHER_ERROR);
    }
}
