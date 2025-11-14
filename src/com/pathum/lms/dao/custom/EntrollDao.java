package com.pathum.lms.dao.custom;

import com.pathum.lms.dao.CrudDao;

import java.sql.SQLException;

public interface EntrollDao extends CrudDao<com.pathum.lms.entity.Entroll, String> {
    public Boolean deletebyStudentId(String studentId) throws SQLException, ClassNotFoundException;
}
