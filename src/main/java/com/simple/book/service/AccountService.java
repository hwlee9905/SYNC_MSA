//package com.simple.book.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.simple.book.entity.UserEntity;
//import com.simple.book.repository.UserRepository;
//import com.simple.book.vo.Account;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class AccountService implements UserDetailsService{
//	
//	private final UserRepository userRepository;
//    
//	private final BCryptPasswordEncoder encoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        String id = "1";
//    	Account account = new Account();
//        account.setId(id); //entity set
//        account = id;
//        
//        //account = userMapper.findUser(account); // select
//        
//        if(account != null){
//            List<GrantedAuthority> authorities = new ArrayList();
//            return new User(account.getId(), account.getPasswd(), authorities);
//        }
//        return null;
//    }
//
//    @Transactional
//    public boolean join(String userId, String userPwd) {
//        Account checkUser = new Account();
//        checkUser.setId(userId);
//
//        if (userMapper.findUser(checkUser) != null){
//            return false;
//        }
//        Account newUser = new Account();
//        newUser.setId(userId);
//        newUser.setPasswd(encoder.encode(userPwd));
//        userMapper.save(newUser);
//        return true;
//    }
//}
