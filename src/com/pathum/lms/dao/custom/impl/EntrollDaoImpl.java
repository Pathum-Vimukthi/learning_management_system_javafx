package com.pathum.lms.dao.custom.impl;

import com.pathum.lms.dao.CrudUtils;
import com.pathum.lms.dao.custom.EntrollDao;
import com.pathum.lms.entity.Entroll;

import java.sql.SQLException;
import java.util.List;

public class EntrollDaoImpl implements EntrollDao {
    @Override
    public Boolean deletebyStudentId(String studentId) throws SQLException, ClassNotFoundException {
        return CrudUtils.execute("DELETE FROM entroll WHERE student_id=?", studentId);
    }

    @Override
    public boolean save(Entroll entroll) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Entroll entroll) {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String findById(String s) {
        return "";
    }

    @Override
    public List<Entroll> findAll() {
        return List.of();
    }
}
