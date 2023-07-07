/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pacinho.MasterBet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import pl.pacinho.MasterBet.model.Role;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Blob;

/**
 * @author pojdana
 */
@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GenericGenerator(name = "userIdGen", strategy = "increment")
    @GeneratedValue(generator = "userIdGen")
    private int id;
    @Column(name = "USERNAME", unique = true)
    private String username;
    @Column(name = "EMAIL", unique = true)
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "ACTIVE")
    private int active;
    @Column(name = "ROLES")
    @Enumerated(EnumType.STRING)
    private Role roles;
    @Column(name = "RESET_PASSWORD_CODE")
    private String resetPasswordCode;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "balance", columnDefinition = "DOUBLE PRECISION default 50")
    private BigDecimal balance;

    public User(String username, String email, String password, int active, Role roles, BigDecimal balance) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.balance = balance;
    }
}
