/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventmanager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arpit
 */
public class EventManager extends Thread{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        Thread t1=new Thread()
        {
            public void run()
            {
                try {
                    Server.main(new String[0]);
                } 
                catch (IOException ex) {
                    Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (SQLException ex) {
                    Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Thread t2=new Thread()
        {
            public void run()
            {
                Login_Form lf=new Login_Form();
                lf.setVisible(true);
            }
        };
        t1.start();
        Thread.sleep(5000);
        t2.start();
    }
    
}
