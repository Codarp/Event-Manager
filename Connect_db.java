import java.sql.*;
import javax.swing.*;
public class Connect_db {
    public static Connection still_connecting(){ 
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/arpit","root","return");
            return con;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
}
