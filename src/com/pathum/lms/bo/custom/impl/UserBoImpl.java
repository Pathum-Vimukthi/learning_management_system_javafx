package com.pathum.lms.bo.custom.impl;

import com.pathum.lms.bo.custom.UserBo;
import com.pathum.lms.dao.DaoFactory;
import com.pathum.lms.dao.custom.impl.UserDaoImpl;
import com.pathum.lms.dto.request.RequestUserDto;
import com.pathum.lms.dto.response.ResponseUserDto;
import com.pathum.lms.entity.User;
import com.pathum.lms.utils.DaoType;
import com.pathum.lms.utils.security.PasswordManager;

import java.sql.SQLException;

public class UserBoImpl implements UserBo {
    UserDaoImpl user = DaoFactory.getInstance().getDao(DaoType.USER);
    @Override
    public boolean registerUser(RequestUserDto requestUserDto) throws SQLException, ClassNotFoundException {
        boolean isSaved = user.save(new User(
                requestUserDto.getEmail(),
                requestUserDto.getFullName(),
                requestUserDto.getAge(),
                new PasswordManager().encodePassword(requestUserDto.getPassword())
        ));
        if(isSaved){
            return true;
        }
        return false;
    }

    @Override
    public ResponseUserDto loginUser(String email, String password) throws SQLException, ClassNotFoundException {
        User selectedUser = user.findByEmail(email);
        if(selectedUser != null) {
            if(new PasswordManager().checkPassword(password, selectedUser.getPassword())) {
                return new ResponseUserDto(
                        selectedUser.getEmail(),
                        selectedUser.getFullName(),
                        200,
                        "Login successful!"
                );
            }else {
                return new ResponseUserDto(
                        null,
                        null,
                        401,
                        "Incorrect password!"
                );
            }
        }else {
            return new ResponseUserDto(
                    null,
                    null,
                    404,
                    "User not found!"
            );
        }
    }
}
