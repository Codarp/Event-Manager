package eventmanager;



import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Scanner;
import javax.swing.*;

public class Server extends Thread{
    
    static ServerSocket ss;
    static Socket s;
    static DataOutputStream dout;
    static DataInputStream dis;
    static Connection con;
    Object obj1=new Object();
    Object obj2=new Object();
    Object obj3=new Object();
    Object obj4=new Object();
    Object obj5=new Object();
    Object obj6=new Object();
    Object obj7=new Object();
    
    public void run()
    {
        try
        {
            DataInputStream dis=new DataInputStream(s.getInputStream());
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        System.out.println("Streams created");
        String name=dis.readUTF();
        PreparedStatement stmt=null;
        if(name.equals("Registration")==true)
        {
            synchronized(obj1)
            {
                String event=dis.readUTF();
                String abc=dis.readUTF();
                System.out.println("Data Inputted from the client");
                stmt=con.prepareStatement("select * from "+abc+" where Events='"+event+"';");
                ResultSet rs=stmt.executeQuery();
                if(rs.next())
                {
                    dout.writeUTF("Already Registered");
                }
                else
                {
                    stmt=con.prepareStatement("insert into "+abc+" values(?);");
                    stmt.setString(1,event);
                    stmt.execute();
                    dout.writeUTF("You have been successfully registered");
                    dout.flush();
                }
            }
        }
        else if(name.equals("Signup")==true)
        {
            synchronized(obj2)
            {
                String a=dis.readUTF();
                String b=dis.readUTF();
                System.out.println(a+" "+b);
                stmt=con.prepareStatement("select * from login where user=?");
                stmt.setString(1,a);
                ResultSet res=stmt.executeQuery();
                if(res.next())
                {
                    dout.writeUTF("Already used username!!!");
                }  
                else
                {
                    stmt=con.prepareStatement("insert into login values(?,?)");
                    stmt.setString(1,a);
                    stmt.setString(2,b);
                    stmt.execute();
                    Statement st=con.createStatement();
                    st.executeUpdate("create table "+a+" ( Events varchar(20) );");
                    dout.writeUTF("Successfully Signed up");
                    dout.flush();
                    st.close();
                }
            }
        }
        else if(name.equals("Developer")==true)
        {
            synchronized(obj3)
            {
                Statement st=con.createStatement();
                ResultSet res=st.executeQuery("select * from develop;");
                System.out.println("Data Inputted from the database");
                String a,b,c;
                int l=0;
                while(res.next())
                {
                    l++;
                }
                dout.writeUTF(""+l);
                res.absolute(0);
                while(res.next())
                {
                    a=res.getString("Event");
                    b=res.getString("Venue");
                    c=res.getString("Timing");
                    dout.writeUTF(a);
                    dout.writeUTF(b);
                    dout.writeUTF(c);
                }
                st.close();
                res.close();
            }
        }
        else if(name.equals("Update"))
        {
            synchronized(obj4)
            {
                String a=dis.readUTF();
                String b=dis.readUTF();
                String c=dis.readUTF();
                stmt=con.prepareStatement("select * from develop where Event=? ;");
                stmt.setString(1,a);
                ResultSet res=stmt.executeQuery();
                if(res.next())
                {
                    stmt=con.prepareStatement("update develop set Venue=?, Timing=? "
                        + "where Event = ?;");
                    stmt.setString(1,b);
                    stmt.setString(2,c);
                    stmt.setString(3,a);
                    stmt.execute();
                    dout.writeUTF("Successfully Updated current record");
                }
                else
                {
                    dout.writeUTF("Insert That Event first!");
                }
            }
            
        }
        else if(name.equals("Insert"))
        {
            synchronized(obj5)
            {
                String a=dis.readUTF();
                String b=dis.readUTF();
                String c=dis.readUTF();
                stmt=con.prepareStatement("select * from develop where Event=?;");
                stmt.setString(1,a);
                System.out.println("Statement prepared!!!!");
                ResultSet res=stmt.executeQuery();
                System.out.println("Query Executed!!!");
                if(res.next())
                {
                dout.writeUTF("Already created Event");
                }
                else
                {
                stmt=con.prepareStatement("insert into develop values(?,?,?);");
                System.out.println("Second statemwnt created");
                stmt.setString(1, a);
                stmt.setString(2, b);
                stmt.setString(3, c);
                System.out.println("About to execute query");
                stmt.execute();
                dout.writeUTF("Successfully inserted");
                }
            }
            
        }
        else if(name.equals("Venue"))
        {
            synchronized(obj6)
            {
                stmt=con.prepareStatement("select * from develop;");
                ResultSet res=stmt.executeQuery();
                int n=0;
                while(res.next())
                    n++;
                dout.writeUTF(""+n);
                res.absolute(0);
                while(res.next())
                {
                    dout.writeUTF(""+res.getString(1)+" - "+res.getString(2)+" - "+res.getString(3));
                }
                res.close();
            }
            
        }
        else if(name.equals("Combo"))
        {
            synchronized(obj7)
            {
                stmt=con.prepareStatement("select Event from develop;");
                ResultSet res=stmt.executeQuery();
                int n=0;
                while(res.next())
                {
                n++;
                }
                dout.writeUTF(""+n);
                res.absolute(0);
                while(res.next())
                {
                dout.writeUTF(res.getString(1));
                }
            }
        }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    public static void main(String args[]) throws IOException, SQLException{
        System.out.println("Enter the port number!!");
        Scanner sc=new Scanner(System.in);
        String strin=sc.next();
        int port=Integer.parseInt(strin);
        con=Connect_db.still_connecting();
        ss=new ServerSocket(port);
        
        while(true)
        {
            try
            {
                System.out.println("Waiting for client requests");
                s=ss.accept();
                System.out.println("Connection Established");
                Server t1=new Server();
                t1.start();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null,e);
                break;
            }
        }
        
        dout.close();
        dis.close();
        s.close();
        ss.close();
        con.close();
        
    }
    
}
