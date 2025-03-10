package com.github.benshi;

import java.util.List;

import com.github.benshi.mapper.UserMapper;

public class Main {
    public static void main(String[] args) {
        UserMapper mapper = new UserMapperTest();
        mapper.selectAll();
    }

    public static class UserMapperTest implements UserMapper {

        @Override
        public List<User> selectAll() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'selectAll'");
        }

        @Override
        public int insert(User entity) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'insert'");
        }

    }
}