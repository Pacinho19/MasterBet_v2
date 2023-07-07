package pl.pacinho.MasterBet.model;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseModel {

    private BigDecimal courseA;
    private BigDecimal courseX;
    private BigDecimal courseB;

}
