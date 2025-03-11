package com.github.benshi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.benshi.example.model.User;
import com.github.benshi.example.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userMapper.selectAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        int rows = userMapper.insert(user);
        if (rows == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User createdUser = userMapper.selectById(user.getId());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        int rows = userMapper.update(user);
        if (rows == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User updatedUser = userMapper.selectById(user.getId());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        int rows = userMapper.deleteById(id);
        if (rows == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        long count = userMapper.count(new User());
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
