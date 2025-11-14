package com.pathum.lms.dao.custom.impl;

import com.pathum.lms.dao.CrudUtils;
import com.pathum.lms.dao.custom.StudentDao;
import com.pathum.lms.entity.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    @Override
    public boolean save(Student student) throws SQLException, ClassNotFoundException {
        return CrudUtils.execute("INSERT INTO student VALUES (?,?,?,?,?)",
                student.getId(),
                student.getName(),
                student.getAddress(),
                student.getDob(),
                student.getUserEmail()
        );
    }

    @Override
    public boolean update(Student student) throws SQLException, ClassNotFoundException {
        return CrudUtils.execute("UPDATE student SET name=?, address=?, dob=? WHERE id=?",
                student.getName(),
                student.getAddress(),
                student.getDob(),
                student.getId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtils.execute("DELETE FROM student WHERE id = ?", id);
    }

    @Override
    public String findById(String s) {
        return "";
    }

    @Override
    public List<Student> findAll() {
        return List.of();
    }

    @Override
    public List<Student> findbyName(String searchText) throws SQLException, ClassNotFoundException {
        List<Student> students = new ArrayList<>();
        ResultSet rs = CrudUtils.execute("SELECT * FROM student WHERE name LIKE ?","%"+searchText+"%");
        while (rs.next()) {
            students.add(new Student(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getDate(4),
                    rs.getString(5)
            ));
        }
        return students;
    }
}
