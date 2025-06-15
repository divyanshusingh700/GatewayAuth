package com.truecodes.user.controller;

import com.truecodes.user.dto.UserDto;
import com.truecodes.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/addUpdate")
    public ResponseEntity<UserDto> addOrUpdate(@RequestBody UserDto dto) {
        return ResponseEntity.ok(service.saveOrUpdate(dto));
    }

    @GetMapping("/details/{contactNumber}")
    public ResponseEntity<UserDto> getDetails(@PathVariable String contactNumber) {
        return ResponseEntity.ok(service.getByContact(contactNumber));
    }

}

