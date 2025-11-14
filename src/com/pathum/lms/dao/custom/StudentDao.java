package com.pathum.lms.dao.custom;

import com.pathum.lms.dao.CrudDao;
import com.pathum.lms.entity.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentDao extends CrudDao<Student, String> {
    public List<Student> findbyName(String searchText) throws SQLException, ClassNotFoundException;
}
