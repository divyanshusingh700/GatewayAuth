package com.truecodes.user.service;

import com.truecodes.user.dto.UserDto;
import com.truecodes.user.entity.User;
import com.truecodes.user.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public UserDto saveOrUpdate(UserDto dto) {
        User user = repository.findByContactNumber(dto.getContactNumber()).orElse(new User());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAdharCard(dto.getAdharCard());
        user.setContactNumber(dto.getContactNumber());
        user.setUpdatedAt(LocalDateTime.now());
        if (user.getCreatedAt() == null) user.setCreatedAt(LocalDateTime.now());
        repository.save(user);
        return mapToDTO(user);
    }

    public UserDto getByContact(String contact) {
        User user = repository.findByContactNumber(contact)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    private UserDto mapToDTO(User user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}
