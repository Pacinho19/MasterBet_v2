package pl.pacinho.MasterBet.model.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum CouponMessage {
    MATCH_ALREDY_EXIST_IN_COUPON("This Match already exist in coupon !", 1),
    AMOUNT_GREATER_THAN_BALANCE("Coupon amount is greater than account balance !", 2),
    NO_BET_IN_COUPON("No Bet in coupon!", 3),
    OTHER_ERROR("Unidentified error.", 4);

    @Getter
    private String message;
    private int id;

    public static CouponMessage byId(int id) {
        return Arrays.stream(CouponMessage.values())
                .filter(couponMessage ->  couponMessage.id == id)
                .findFirst()
                .orElse(OTHER_ERROR);
    }
}
