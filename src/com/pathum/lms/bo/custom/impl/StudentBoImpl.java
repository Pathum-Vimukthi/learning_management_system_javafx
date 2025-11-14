package com.pathum.lms.bo.custom.impl;

import com.pathum.lms.DB.DbConnection;
import com.pathum.lms.bo.custom.StudentBo;
import com.pathum.lms.dao.DaoFactory;
import com.pathum.lms.dao.custom.EntrollDao;
import com.pathum.lms.dao.custom.StudentDao;
import com.pathum.lms.dto.request.RequestStudentDto;
import com.pathum.lms.dto.response.ResponseStudentDto;
import com.pathum.lms.entity.Student;
import com.pathum.lms.env.Session;
import com.pathum.lms.utils.DaoType;
import com.pathum.lms.view.tm.StudentTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StudentBoImpl implements StudentBo {
    StudentDao studentDao = DaoFactory.getInstance().getDao(DaoType.STUDENT);
    EntrollDao entrollDao = DaoFactory.getInstance().getDao(DaoType.ENTROLL);
    @Override
    public boolean saveStudent(RequestStudentDto requestStudentDto) throws SQLException, ClassNotFoundException {
        return studentDao.save(new Student(
                requestStudentDto.getId(),
                requestStudentDto.getName(),
                requestStudentDto.getAddress(),
                Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(requestStudentDto.getDob())),
                Session.getEmail()
        ));
    }

    @Override
    public List<ResponseStudentDto> getStudents(String searchText) throws SQLException, ClassNotFoundException {
        List<Student> students = studentDao.findbyName(searchText);
        List<ResponseStudentDto> responseStudentDtos = new ArrayList<>();
        for (Student student : students) {
            responseStudentDtos.add(new ResponseStudentDto(
                    student.getId(),
                    student.getName(),
                    student.getAddress(),
                    new SimpleDateFormat("yyyy-MM-dd").format(student.getDob()),
                    student.getUserEmail()
            ));
        }
        return responseStudentDtos;
    }

    @Override
    public boolean deleteStudent(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        try {
            connection.setAutoCommit(false);
            boolean isEntrollDeleted = entrollDao.deletebyStudentId(id);
            boolean isStudentDeleted = studentDao.delete(id);
            if (isEntrollDeleted && isStudentDeleted) {
                connection.commit();
                return true;
            }else {
                connection.rollback();
                return false;
            }
        }catch (SQLException e) {
            connection.rollback();
            throw e;
        }finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public boolean updateStudent(RequestStudentDto requestStudentDto) throws SQLException, ClassNotFoundException {
        return studentDao.update(new Student(
                requestStudentDto.getId(),
                requestStudentDto.getName(),
                requestStudentDto.getAddress(),
                Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(requestStudentDto.getDob())),
                Session.getEmail()
        ));
    }
}
