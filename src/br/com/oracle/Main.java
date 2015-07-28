/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.oracle;

import br.com.oracle.jdbc.ConnectionFactory;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author Lucas
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Connection connection = new ConnectionFactory().getConnection();
            Main main = new Main();
            main.selecionar(connection);
            
            System.out.println("-------------------------");
            
            main.procedure(connection);
            
            System.out.println("-------------------------");
            
            main.function(connection);
            
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Chamada de function por select
     * @param connection Conexão com o banco de dados
     * @throws SQLException 
     */
    public void selecionar(Connection connection) throws SQLException {
        
        PreparedStatement ps = connection.prepareStatement("SELECT FN_COUNTRIES(?) AS COUNTRY_NAME FROM DUAL");
        ps.setString(1, "JP");
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            System.out.println(rs.getString("COUNTRY_NAME"));
        }
        
        ps.close();
    }

    /**
     * Chamada de procedure
     * @param connection
     * @throws SQLException
     */
    public void procedure(Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("call SP_COUNTRIES(?,?)");

        //registra os parametros de entrada IN
        cs.setString(1, "BR");

        //registra os parametros de saida OUT
        cs.registerOutParameter(2, Types.VARCHAR);
        
        cs.execute();
        
        String pais = cs.getString(2);
        
        cs.close();
        System.out.println(pais);
    }

    /**
     * Chamada de uma function do banco oracle
     *
     * @param connection Conexão com o banco de dados
     * @throws SQLException
     */
    public void function(Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{? = call FN_COUNTRIES(?)}");

        //registra os parametros de saida OUT
        cs.registerOutParameter(1, Types.VARCHAR);

        //registra os parametros de entrada IN
        cs.setString(2, "BR");
        
        cs.execute();
        
        String pais = cs.getString(1);
        
        cs.close();
        System.out.println(pais);
    }
}
