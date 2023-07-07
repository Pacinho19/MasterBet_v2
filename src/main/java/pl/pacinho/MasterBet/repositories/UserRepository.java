/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pacinho.MasterBet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.pacinho.MasterBet.entities.User;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author pojdana
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordCode(String resetPasswordCode);

    @Modifying
    @Transactional
    @Query(value = "update users u set u.balance=u.balance+(:amount) where u.id=:id", nativeQuery = true)
    void updateBalance(@Param("id") int id, @Param("amount") BigDecimal amount);

    Optional<User> findByIdAndActive(int id, int active);

}
