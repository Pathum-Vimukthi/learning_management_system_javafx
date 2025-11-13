package com.pathum.lms.dao.custom;

import com.pathum.lms.dao.CrudDao;
import com.pathum.lms.dto.response.ResponseUserDto;
import com.pathum.lms.entity.User;

import java.sql.SQLException;

public interface UserDao extends CrudDao<User, String> {
    public User findByEmail(String email) throws SQLException, ClassNotFoundException;
}
