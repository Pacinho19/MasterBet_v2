/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pacinho.MasterBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.pacinho.MasterBet.entities.User;
import pl.pacinho.MasterBet.model.MyUserDetails;
import pl.pacinho.MasterBet.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author pojdana
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(userName);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));
        return user.map(MyUserDetails::new).get();
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateBallance(int id, BigDecimal toWin) {
        userRepository.updateBalance(id, toWin);
    }

    public Optional<User> getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordCode(token);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getById(int id) {
        return userRepository.findById(id);
    }

}
