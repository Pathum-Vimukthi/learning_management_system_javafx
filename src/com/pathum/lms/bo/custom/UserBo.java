package com.pathum.lms.bo.custom;

import com.pathum.lms.bo.SuperBo;
import com.pathum.lms.dto.request.RequestUserDto;
import com.pathum.lms.dto.response.ResponseUserDto;

import java.sql.SQLException;

public interface UserBo extends SuperBo {
    public boolean registerUser(RequestUserDto requestUserDto) throws SQLException, ClassNotFoundException;
    public ResponseUserDto loginUser(String email, String password) throws SQLException, ClassNotFoundException;
}
