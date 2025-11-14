package com.pathum.lms.bo.custom;

import com.pathum.lms.bo.SuperBo;
import com.pathum.lms.dto.request.RequestStudentDto;
import com.pathum.lms.dto.response.ResponseStudentDto;
import com.pathum.lms.view.tm.StudentTm;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface StudentBo extends SuperBo {
    public boolean saveStudent(RequestStudentDto requestStudentDto) throws SQLException, ClassNotFoundException;
    public List<ResponseStudentDto> getStudents(String searchText) throws SQLException, ClassNotFoundException;
    public boolean deleteStudent(String id) throws SQLException, ClassNotFoundException;
    public boolean updateStudent(RequestStudentDto requestStudentDto) throws SQLException, ClassNotFoundException;
}
