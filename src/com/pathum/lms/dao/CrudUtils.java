package com.pathum.lms.dao;

import com.pathum.lms.DB.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtils {
    public static <T>T execute(String sql, Object ...params) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        if(sql.toLowerCase().startsWith("select")){
            return (T) ps.executeQuery();
        }
        return (T) (Boolean)(ps.executeUpdate()>0);
    }
}
