package pl.pacinho.MasterBet.model.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum LoginMessage {

    USER_NOT_FOUND("User not found or just active", 1),
    ACRIVATED("Account activated. Login now !", 2),
    BAD_SECRET("Bad Secret ! Not activated !", 3),
    PASSWORD_RESET("Password reset success ! Login Now.", 4),
    PASSWORT_RESET_EMAIL_SEND("To your ", 5),
    OTHER_ERROR("Unidentified error.", 6);

    @Getter
    private String message;
    private int id;

    public static LoginMessage byId(int id) {
        return Arrays.stream(LoginMessage.values())
                .filter(loginMessage -> loginMessage.id == id)
                .findFirst()
                .orElse(OTHER_ERROR);
    }


}
