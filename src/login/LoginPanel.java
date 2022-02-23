package login;

import connection.ConnectDB;
import main.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wang yuxuan
 * @create 2022.01.01
 */
public class LoginPanel extends JFrame {
    private final int WIDTH = 768;
    private final int HEIGHT = 480;
    private final int FIELDHEIGHT = 28;

    private final JPasswordField f2;
    private final JTextField f1;
    private final LoginPanel lp = this;

    public LoginPanel() {
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("公交安全管理系统");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 0, 0));

        /* 标题 */
        JLabel msg = new JLabel("  输入工号与密码以登录系统。", JLabel.CENTER);
        msg.setSize(getWidth(), (int) (0.25*getHeight()));
        msg.setFont(new Font("", Font.BOLD, 36));
        msg.setBackground(Color.BLACK);
        add(msg);
        add(new JPanel());

        /* 账号框 */
        JPanel id = new JPanel();
        id.setSize(getWidth(), (int) (0.25*getHeight()));
        JLabel l1 = new JLabel("工号：");
        l1.setSize((int) (getWidth()*0.25), id.getHeight());
        l1.setFont(new Font("", Font.BOLD, 32));
        f1 = new JTextField(10);
        f1.setPreferredSize(new Dimension (id.getWidth(), (int) (id.getHeight()*0.4)));
        f1.setFont(new Font("", Font.PLAIN, FIELDHEIGHT));
        id.add(l1);
        id.add(f1);
        add(id);

        /* 密码框 */
        JPanel password = new JPanel();
        password.setSize(getWidth(), (int) (0.25*getHeight()));
        JLabel l2 = new JLabel("密码：");
        l2.setSize((int) (getWidth()*0.25), id.getHeight());
        l2.setFont(new Font("", Font.BOLD, 32));
        f2 = new JPasswordField(10);
        f2.setPreferredSize(new Dimension (id.getWidth(), (int) (id.getHeight()*0.4)));
        f2.setFont(new Font("", Font.PLAIN, FIELDHEIGHT));
        password.add(l2);
        password.add(f2);
        add(password);
        add(new JPanel());

        /* 按钮 */
        JPanel check = new JPanel();
        JButton login = new JButton("登录");
        login.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/10));
        login.setFont(new Font("", Font.BOLD, 30));
        login.addActionListener(new LoginActionListener());
        check.add(login);
        add(check);

        setVisible(true);
    }

    /* 登录监听 */
    class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String user = f1.getText();
            String pass = new String(f2.getPassword());
            String poss = null;
            ConnectDB db = new ConnectDB(user, pass);
            ResultSet query = db.select("SELECT 职位 FROM 个人信息;");
            try {
                while (query.next()) {
                    poss = query.getString("职位");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            new MainPanel(user, pass, poss);
            lp.dispose();
        }
    }

    public static void main(String[] args) {
        LoginPanel loginPanel = new LoginPanel();
    }
}
