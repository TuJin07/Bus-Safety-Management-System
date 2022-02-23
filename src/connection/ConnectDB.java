package connection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * @author wang yuxuan
 * @create 2022.01.02
 */
public class ConnectDB {
//    MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/公交安全管理数据库" +
            "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF8";

//    用户名及密码
    private final String USER;
    private final String PASS;
    private Connection conn;
    private Statement stmt;

    public ConnectDB(String user, String pass) {
        USER = user;
        PASS = pass;
        connect();
    }

    /* 连接数据库 */
    private void connect() {
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println("连接成功。");
            stmt = conn.createStatement();
        } catch (SQLException se) {
            /* 处理用户名密码不正确 */
            JFrame frame = new JFrame("提示");
            frame.setSize(480, 300);
            Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
            frame.setBounds(p.x - frame.getWidth() / 2, p.y - frame.getHeight() / 2, frame.getWidth(), frame.getHeight());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Label msg = new Label("用户名或密码不正确。");
            msg.setFont(new Font("", Font.PLAIN, 48));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 传入 select 的 sql 语句，返回结果集 */
    public ResultSet select(String sql) {
        try {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* 新增司机信息 */
    public void insertDriverInformation(String name, String gender, String sid, String year, String statement, int line) {
        try {
            ResultSet query = select("SELECT MAX(工号) FROM 车队职员");
            int id = 0;
            while (query.next()) {
                id = query.getInt("MAX(工号)");
            }
            id++;
            String sql = "INSERT INTO 车队职员 VALUES (?,?,?,?,?,?,?,?);";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, gender);
            pstmt.setString(4, "司机");
            pstmt.setString(5, sid);
            pstmt.setInt(6, Integer.parseInt(year));
            pstmt.setString(7, sid.substring(6, 10));
            pstmt.setString(8, statement);
            pstmt.executeUpdate();

            sql = "INSERT INTO 司机 VALUES (?,?);";
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, line);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 新增违章记录 */
    public void insertViolationInformation(int id, String cid, int sid, String type, String time, String remark) {
        try {
            ResultSet query = select("SELECT MAX(违章编号) FROM 违章记录");
            int wid = 0;
            while (query.next()) {
                wid = query.getInt("MAX(违章编号)");
            }
            wid++;
            String sql = "INSERT INTO 违章记录 VALUES (?,?,?,?,?,?,?,?);";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, wid);
            pstmt.setInt(2, id);
            pstmt.setString(3, cid);
            pstmt.setInt(4, sid);
            pstmt.setString(5, type);
            pstmt.setString(6, time);
            pstmt.setString(7, USER);
            pstmt.setString(8, remark);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 更新司机信息 */
    public void updatesDriverInformation(int id, String name, String gender, String sid, int year, String statement, int line) {
        try {
            String sql = "UPDATE 车队职员 SET 姓名 = ?, 性别 = ?, 身份证号 = ?, 入职年份 = ?, 出生年份 = ?, 就职状态 = ?" +
                    "WHERE " + id +" = 工号;";

            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, gender);
            pstmt.setString(3, sid);
            pstmt.setInt(4, year);
            pstmt.setString(5, sid.substring(6, 10));
            pstmt.setString(6, statement);
            pstmt.executeUpdate();

            sql = "UPDATE 司机 SET 所属线路号 = ? WHERE " + id + " = 司机工号;";
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, line);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 关闭连接 */
    public void exit() {
        try {
            stmt.close();
            conn.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException ignored){}
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("关闭连接！");
    }

}
