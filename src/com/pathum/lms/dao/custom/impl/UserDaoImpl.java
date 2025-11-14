package com.pathum.lms.dao.custom.impl;

import com.pathum.lms.DB.DbConnection;
import com.pathum.lms.dao.CrudUtils;
import com.pathum.lms.dao.custom.UserDao;
import com.pathum.lms.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public User findByEmail(String email) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtils.execute("SELECT * FROM user WHERE email = ?", email);
        while (rs.next()) {
            return new User(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3),
                    rs.getString(4)
            );
        }
        return null;
    }

    @Override
    public boolean save(User user) throws SQLException, ClassNotFoundException {
        return CrudUtils.execute("INSERT INTO user VALUES (?,?,?,?)", user.getEmail(), user.getFullName(),user.getAge(),user.getPassword());
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public String findById(String s) {
        return "";
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }
}
