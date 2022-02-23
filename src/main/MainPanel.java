package main;

import connection.ConnectDB;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author wang yuxuan
 * @create 2022.01.02
 */
public class MainPanel extends JFrame {
    private final String USER;
    private final String PASS;
    private final String POSS;
    private final int WIDTH = 1920;
    private final int HEIGHT = 1200;
    private final JLabel title;
    private final JPanel cards;
    private final ConnectDB db;

    public MainPanel(String user, String pass, String poss) {
        USER = user;
        PASS = pass;
        POSS = poss;
        db = new ConnectDB(USER, PASS);

        /* 窗口居中 */
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* 主窗口设置 */
        setTitle("公交安全管理系统");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        /* title设置 */
        title = new JLabel("公交安全管理系统主界面", JLabel.CENTER);
        title.setFont(new Font("", Font.BOLD, 72));
        title.setPreferredSize(new Dimension (WIDTH, (int) (HEIGHT*0.10)));
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(title,BorderLayout.NORTH);

        /* operation设置 */
        JPanel operation = new JPanel();
        operation.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        operation.setPreferredSize(new Dimension((int) (WIDTH*0.2), (int) (HEIGHT*0.9)));
        JButton[] operationList = {
                new JButton("查询车队成员信息"),
                new JButton("查询车队成员违章信息"),
                new JButton("查询车队统计违章信息"),
                new JButton("查询线路成员信息"),
                new JButton("查询线路成员违章信息"),
                new JButton("查询线路统计违章信息"),
                new JButton("查询个人信息"),
                new JButton("查询个人违章信息"),
                new JButton("录入司机信息"),
                new JButton("录入违章信息"),
                new JButton("修改司机信息")
        };
        for (int i = 0; i < operationList.length; i++) {
            operationList[i].setPreferredSize(new Dimension((int) (getWidth()*0.2), (int) (getHeight()*0.078)));
            operationList[i].setFont(new Font("", Font.BOLD, 32));
            operationList[i].setBackground(Color.decode("#dee7f2"));
            operation.add(operationList[i]);
        }
        /* 各 operation 对应的页面 */
        operationList[0].addActionListener(new Button0ActionListener());
        operationList[1].addActionListener(new Button1ActionListener());
        operationList[2].addActionListener(new Button2ActionListener());
        operationList[3].addActionListener(new Button3ActionListener());
        operationList[4].addActionListener(new Button4ActionListener());
        operationList[5].addActionListener(new Button5ActionListener());
        operationList[6].addActionListener(new Button6ActionListener());
        operationList[7].addActionListener(new Button7ActionListener());
        operationList[8].addActionListener(new Button8ActionListener());
        operationList[9].addActionListener(new Button9ActionListener());
        operationList[10].addActionListener(new Button10ActionListener());
        /* 依据角色权限，禁用相关操作 */
        if (Objects.equals(POSS, "司机")) {
            operationList[0].setEnabled(false);
            operationList[1].setEnabled(false);
            operationList[2].setEnabled(false);
            operationList[3].setEnabled(false);
            operationList[4].setEnabled(false);
            operationList[5].setEnabled(false);
            operationList[8].setEnabled(false);
            operationList[9].setEnabled(false);
            operationList[10].setEnabled(false);
        }
        if (Objects.equals(POSS, "路队长")) {
            operationList[0].setEnabled(false);
            operationList[1].setEnabled(false);
            operationList[2].setEnabled(false);
            operationList[8].setEnabled(false);
            operationList[10].setEnabled(false);
        }
        if (Objects.equals(POSS, "队长")) {
            operationList[3].setEnabled(false);
            operationList[4].setEnabled(false);
            operationList[5].setEnabled(false);
            operationList[7].setEnabled(false);
        }
        add(operation, BorderLayout.WEST);

        /* detail设置 */
        cards = new JPanel();
        cards.setLayout(new CardLayout());
        cards.setPreferredSize(new Dimension((int) (WIDTH*0.8), (int) (HEIGHT*0.9)));
        JPanel index = new JPanel(new GridLayout(4,1));
        index.add(new JPanel());
        JLabel index1 = new JLabel("欢迎使用公交安全管理系统", SwingConstants.CENTER);
        JLabel index2 = new JLabel(USER + "，欢迎您，你当前的权限级别为：" + POSS, SwingConstants.CENTER);
        index1.setFont(new Font("微软雅黑", Font.BOLD, 72));
        index2.setFont(new Font("微软雅黑", Font.PLAIN, 48));
        index.add(index1);
        index.add(index2);
        cards.add(index);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        JPanel[] detailList = {
                createCard0(),
                createCard1("1999-12-31 23:59:59", date),
                createCard2(),
                createCard3(),
                createCard4("1999-12-31 23:59:59", date),
                createCard5(),
                createCard6(),
                createCard7(),
                createCard8(),
                createCard9(),
                createCard10()
        };

        for (int i = 0; i < detailList.length; i++) {
            cards.add(detailList[i], "card" + i);
        }
        add(cards, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createCard0() {
        if (! Objects.equals(POSS, "队长")) return new JPanel();
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int) (WIDTH*0.8), (int) (HEIGHT*0.9)));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("查询司机信息如下。");
//        msg.setPreferredSize(new Dimension((int) (panel.getWidth()*0.8), (int) (panel.getHeight()*0.2)));
        msg.setFont(new Font("", Font.PLAIN, 48));
        panel.add(msg);

        ResultSet set = db.select("SELECT * FROM 车队成员信息;");
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        try {
            while (set.next()) {
                Object[] temp = {set.getInt("工号"), set.getString("姓名"),
                        set.getString("性别"), set.getString("司机所属线路"),
                        set.getString("路队长姓名"), set.getString("职位"),
                        set.getString("身份证号"), set.getInt("入职年份"),
                        set.getInt("出生年份"), set.getString("就职状态")};
                data.add(temp);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        String[] name={"工号","姓名","性别","司机所属线路","路队长姓名","职位","身份证号","入职年份","出生年月","就职状态"};
        Object[][] objects = new Object[data.size()][10];
        data.toArray(objects);
        /* JTable 表格设置 */
        JTable table=new JTable(objects, name){ public boolean isCellEditable(int row, int column) { return false; }};
        table.setRowHeight((int) (getHeight()*0.05));
        table.setFont(new Font("", Font.PLAIN, 24));
        table.setPreferredSize(new Dimension((int) (getWidth()*0.78), table.getRowHeight()*(13 + table.getRowCount())));
        /* 表头设置 */
        JTableHeader tab_header = table.getTableHeader();
        tab_header.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        tab_header.setPreferredSize(new Dimension(tab_header.getWidth(), (int) (getHeight()*0.07)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension((int) (getWidth()*0.78), (int) (getHeight()*0.8)));
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createCard1(String stime, String etime) {
        if (! Objects.equals(POSS, "队长")) return new JPanel();
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int) (WIDTH*0.8), (int) (HEIGHT*0.9)));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("                           查询车队内司机违章信息如下。                           ", SwingConstants.CENTER);
//        msg.setPreferredSize(new Dimension((int) (panel.getWidth()*0.8), (int) (panel.getHeight()*0.2)));
        msg.setFont(new Font("", Font.PLAIN, 48));
        panel.add(msg);

        JPanel timeline = new JPanel();
        JLabel start = new JLabel("起始时间：");
        JLabel end = new JLabel("        结束时间：");
        start.setFont(new Font("", Font.PLAIN, 32));
        end.setFont(new Font("", Font.PLAIN, 32));
        JTextField started = new JTextField(stime, 15);
        JTextField ended = new JTextField(etime, 15);
        started.setFont(new Font("", Font.PLAIN, 30));
        ended.setFont(new Font("", Font.PLAIN, 30));
        started.setPreferredSize(new Dimension(30, 32));
        ended.setPreferredSize(new Dimension(30, 32));

        JButton check = new JButton("确定");
        check.setFont(new Font("", Font.BOLD, 32));

        timeline.add(start);
        timeline.add(started);
        timeline.add(end);
        timeline.add(ended);
        timeline.add(check);
        panel.add(timeline);

        ResultSet set = db.select("SELECT * FROM 车队成员违章信息 " +
                "WHERE 违章时间 BETWEEN '" + stime + "' AND '" + etime + "';");
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        try {
            while (set.next()) {
                Object[] temp = {set.getInt("违章编号"), set.getInt("违章司机工号"),
                        set.getString("违章司机姓名"), set.getString("司机所属线路号"),
                        set.getString("路队长姓名"), set.getString("所驾车车牌号"),
                        set.getInt("违章所在站点号"), set.getString("违章类别"),
                        set.getString("违章时间"), set.getString("违章处罚"),
                        set.getInt("录入者工号"), set.getString("备注信息")
                };
                data.add(temp);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        String[] name={"编号", "司机工号", "姓名", "所属线路", "路队长", "所驾车车牌号", "违章所在站点号", "类别", "时间", "处罚",
                "录入者", "备注"};
        Object[][] objects = new Object[data.size()][12];
        data.toArray(objects);
        /* JTable 表格设置 */
        JTable table=new JTable(objects, name){ public boolean isCellEditable(int row, int column) { return false; }};
        table.setRowHeight((int) (HEIGHT*0.05));
        table.setFont(new Font("", Font.PLAIN, 24));
        table.setPreferredSize(new Dimension((int) (WIDTH*0.78), table.getRowHeight()*(table.getRowCount()+10)));
        /* 表头设置 */
        JTableHeader tab_header = table.getTableHeader();
        tab_header.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        tab_header.setPreferredSize(new Dimension(tab_header.getWidth(), (int) (HEIGHT*0.07)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension((int) (WIDTH*0.78), (int) (HEIGHT*0.8)));
        panel.add(scrollPane);

        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.add(createCard1(started.getText(), ended.getText()), "card1");
                ((CardLayout) (cards.getLayout())).show(cards, "card1");
            }
        });

        return panel;
    }

    private JPanel createCard2() {
        if (! Objects.equals(POSS, "队长")) return new JPanel();
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int) (WIDTH*0.8), (int) (HEIGHT*0.9)));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("查询车队违章统计信息如下。");
//        msg.setPreferredSize(new Dimension((int) (panel.getWidth()*0.8), (int) (panel.getHeight()*0.2)));
        msg.setFont(new Font("", Font.PLAIN, 48));
        panel.add(msg);

        ResultSet set = db.select("SELECT * FROM 车队统计违章信息;");
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        try {
            while (set.next()) {
                Object[] temp = {set.getString("违章类别"), set.getInt("违章次数")};
                data.add(temp);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        String[] name={"违章类别", "总计违章次数"};
        Object[][] objects = new Object[data.size()][2];
        data.toArray(objects);
        /* JTable 表格设置 */
        JTable table=new JTable(objects, name){ public boolean isCellEditable(int row, int column) { return false; }};
        table.setRowHeight((int) (HEIGHT*0.05));
        table.setFont(new Font("", Font.PLAIN, 24));
        table.setPreferredSize(new Dimension((int) (WIDTH*0.78), table.getRowHeight()*(table.getRowCount()+10)));
        /* 表头设置 */
        JTableHeader tab_header = table.getTableHeader();
        tab_header.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        tab_header.setPreferredSize(new Dimension(tab_header.getWidth(), (int) (HEIGHT*0.07)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension((int) (WIDTH*0.78), (int) (HEIGHT*0.8)));
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createCard3() {
        if (! Objects.equals(POSS, "路队长")) return new JPanel();
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int) (WIDTH*0.8), (int) (HEIGHT*0.9)));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("查询线路成员信息如下。");
//        msg.setPreferredSize(new Dimension((int) (panel.getWidth()*0.8), (int) (panel.getHeight()*0.2)));
        msg.setFont(new Font("", Font.PLAIN, 48));
        panel.add(msg);

        ResultSet set = db.select("SELECT * FROM 线路成员信息;");
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        try {
            while (set.next()) {
                Object[] temp = {set.getString("工号"), set.getString("姓名"),
                        set.getString("性别"), set.getString("职位"),
                        set.getString("身份证号"), set.getString("入职年份"),
                        set.getString("出生年份"), set.getString("就职状态")
                };
                data.add(temp);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        String[] name={"工号", "姓名", "性别", "职位", "身份证号", "入职年份", "出生年份", "就职状态"};
        Object[][] objects = new Object[data.size()][8];
        data.toArray(objects);
        /* JTable 表格设置 */
        JTable table=new JTable(objects, name){ public boolean isCellEditable(int row, int column) { return false; }};
        table.setRowHeight((int) (HEIGHT*0.05));
        table.setFont(new Font("", Font.PLAIN, 24));
        table.setPreferredSize(new Dimension((int) (WIDTH*0.78), table.getRowHeight()*(table.getRowCount()+10)));
        /* 表头设置 */
        JTableHeader tab_header = table.getTableHeader();
        tab_header.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        tab_header.setPreferredSize(new Dimension(tab_header.getWidth(), (int) (HEIGHT*0.07)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension((int) (WIDTH*0.78), (int) (HEIGHT*0.8)));
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createCard4(String stime, String etime) {
        if (! Objects.equals(POSS, "路队长")) return new JPanel();
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int) (WIDTH*0.8), (int) (HEIGHT*0.9)));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("                           查询线路内司机违章信息如下。                           ");
//        msg.setPreferredSize(new Dimension((int) (panel.getWidth()*0.8), (int) (panel.getHeight()*0.2)));
        msg.setFont(new Font("", Font.PLAIN, 48));
        panel.add(msg);

        JPanel timeline = new JPanel();
        JLabel start = new JLabel("起始时间：");
        JLabel end = new JLabel("        结束时间：");
        start.setFont(new Font("", Font.PLAIN, 32));
        end.setFont(new Font("", Font.PLAIN, 32));
        JTextField started = new JTextField(stime, 15);
        JTextField ended = new JTextField(etime, 15);
        started.setFont(new Font("", Font.PLAIN, 30));
        ended.setFont(new Font("", Font.PLAIN, 30));
        started.setPreferredSize(new Dimension(30, 32));
        ended.setPreferredSize(new Dimension(30, 32));

        JButton check = new JButton("确定");
        check.setFont(new Font("", Font.BOLD, 32));

        timeline.add(start);
        timeline.add(started);
        timeline.add(end);
        timeline.add(ended);
        timeline.add(check);
        panel.add(timeline);

        ResultSet set = db.select("SELECT * FROM 线路成员违章信息 " +
                "WHERE 违章时间 BETWEEN '" + stime + "' AND '" + etime + "';");
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        try {
            while (set.next()) {
                Object[] temp = {set.getInt("违章编号"), set.getInt("违章司机工号"),
                        set.getString("违章司机姓名"), set.getString("所驾车车牌号"),
                        set.getInt("违章所在站点号"), set.getString("违章类别"),
                        set.getString("违章时间"), set.getString("违章处罚"),
                        set.getInt("录入者工号"), set.getString("备注信息")
                };
                data.add(temp);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        String[] name={"编号", "司机工号", "姓名", "所驾车车牌号", "违章所在站点号", "类别", "时间", "处罚",
                "录入者", "备注"};
        Object[][] objects = new Object[data.size()][10];
        data.toArray(objects);
        /* JTable 表格设置 */
        JTable table=new JTable(objects, name){ public boolean isCellEditable(int row, int column) { return false; }};
        table.setRowHeight((int) (HEIGHT*0.05));
        table.setFont(new Font("", Font.PLAIN, 24));
        table.setPreferredSize(new Dimension((int) (WIDTH*0.78), table.getRowHeight()*(table.getRowCount()+10)));
        /* 表头设置 */
        JTableHeader tab_header = table.getTableHeader();
        tab_header.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        tab_header.setPreferredSize(new Dimension(tab_header.getWidth(), (int) (HEIGHT*0.07)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension((int) (WIDTH*0.78), (int) (HEIGHT*0.8)));
        panel.add(scrollPane);

        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.add(createCard4(started.getText(), ended.getText()), "card4");
                ((CardLayout) (cards.getLayout())).show(cards, "card4");
            }
        });

        return panel;
    }

    private JPanel createCard5() {
        if (! Objects.equals(POSS, "路队长")) return new JPanel();
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int) (WIDTH*0.8), (int) (HEIGHT*0.9)));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("查询线路违章统计信息如下。");
//        msg.setPreferredSize(new Dimension((int) (panel.getWidth()*0.8), (int) (panel.getHeight()*0.2)));
        msg.setFont(new Font("", Font.PLAIN, 48));
        panel.add(msg);

        ResultSet set = db.select("SELECT * FROM 线路统计违章信息;");
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        try {
            while (set.next()) {
                Object[] temp = {set.getString("违章类别"), set.getInt("违章次数")};
                data.add(temp);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        String[] name={"违章类别", "总计违章次数"};
        Object[][] objects = new Object[data.size()][2];
        data.toArray(objects);
        /* JTable 表格设置 */
        JTable table=new JTable(objects, name){ public boolean isCellEditable(int row, int column) { return false; }};
        table.setRowHeight((int) (HEIGHT*0.05));
        table.setFont(new Font("", Font.PLAIN, 24));
        table.setPreferredSize(new Dimension((int) (WIDTH*0.78), table.getRowHeight()*(table.getRowCount()+10)));
        /* 表头设置 */
        JTableHeader tab_header = table.getTableHeader();
        tab_header.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        tab_header.setPreferredSize(new Dimension(tab_header.getWidth(), (int) (HEIGHT*0.07)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension((int) (WIDTH*0.78), (int) (HEIGHT*0.8)));
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createCard6() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int) (WIDTH*0.8), (int) (HEIGHT*0.9)));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("查询您的个人信息如下。");
//        msg.setPreferredSize(new Dimension((int) (panel.getWidth()*0.8), (int) (panel.getHeight()*0.2)));
        msg.setFont(new Font("", Font.PLAIN, 48));
        panel.add(msg);

        ResultSet set = db.select("SELECT * FROM 个人信息;");
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        try {
            while (set.next()) {
                Object[] temp = {set.getString("工号"), set.getString("姓名"),
                        set.getString("性别"), set.getString("职位"),
                        set.getString("身份证号"), set.getString("入职年份"),
                        set.getString("出生年份")
                };
                data.add(temp);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        String[] name={"工号", "姓名", "性别", "职位", "身份证号", "入职年份", "出生年份"};
        Object[][] objects = new Object[data.size()][7];
        data.toArray(objects);
        /* JTable 表格设置 */
        JTable table=new JTable(objects, name){ public boolean isCellEditable(int row, int column) { return false; }};
        table.setRowHeight((int) (HEIGHT*0.05));
        table.setFont(new Font("", Font.PLAIN, 24));
        table.setPreferredSize(new Dimension((int) (WIDTH*0.78), table.getRowHeight()*(table.getRowCount()+10)));
        /* 表头设置 */
        JTableHeader tab_header = table.getTableHeader();
        tab_header.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        tab_header.setPreferredSize(new Dimension(tab_header.getWidth(), (int) (HEIGHT*0.07)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension((int) (WIDTH*0.78), (int) (HEIGHT*0.8)));
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createCard7() {
        if (! Objects.equals(POSS, "路队长") && ! POSS.equals("司机")) return new JPanel();
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int) (WIDTH*0.8), (int) (HEIGHT*0.9)));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("查询您的违章信息如下。");
//        msg.setPreferredSize(new Dimension((int) (panel.getWidth()*0.8), (int) (panel.getHeight()*0.2)));
        msg.setFont(new Font("", Font.PLAIN, 48));
        panel.add(msg);

        ResultSet set = db.select("SELECT * FROM 个人违章信息;");
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        try {
            while (set.next()) {
                Object[] temp = {set.getInt("违章编号"), set.getString("所驾车车牌号"),
                        set.getInt("违章所在站点号"), set.getString("违章类别"),
                        set.getString("违章时间"), set.getString("违章处罚"),
                        set.getInt("录入者工号"), set.getString("录入者姓名"),
                        set.getString("备注信息")
                };
                data.add(temp);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        String[] name={"编号", "所驾车车牌号", "违章所在站点号", "类别", "时间", "处罚", "录入人工号", "录入人姓名", "备注"};
        Object[][] objects = new Object[data.size()][9];
        data.toArray(objects);
        /* JTable 表格设置 */
        JTable table=new JTable(objects, name){ public boolean isCellEditable(int row, int column) { return false; }};
        table.setRowHeight((int) (HEIGHT*0.05));
        table.setFont(new Font("", Font.PLAIN, 24));
        table.setPreferredSize(new Dimension((int) (WIDTH*0.78), table.getRowHeight()*(table.getRowCount()+10)));
        /* 表头设置 */
        JTableHeader tab_header = table.getTableHeader();
        tab_header.setFont(new Font("微软雅黑", Font.PLAIN, 26));
        tab_header.setPreferredSize(new Dimension(tab_header.getWidth(), (int) (HEIGHT*0.07)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension((int) (WIDTH*0.78), (int) (HEIGHT*0.8)));
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createCard8() {
        if (Objects.equals(POSS, "路队长") || POSS.equals("司机")) return new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 1));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("请按格式输入司机信息。", SwingConstants.CENTER);
        msg.setFont(new Font("", Font.PLAIN, 48));
        panel.add(msg);
        panel.add(new JPanel());

        JPanel name = new JPanel();
        JLabel l1 = new JLabel("　　　姓名：", SwingConstants.LEFT);
        l1.setFont(new Font("", Font.BOLD, 32));
        JTextField f1 = new JTextField(20);
        f1.setFont(new Font("", Font.PLAIN, 32));
        name.add(l1);
        name.add(f1);
        panel.add(name);

        JPanel gender = new JPanel();
        JLabel l2 = new JLabel("　　　性别：", SwingConstants.LEFT);
        l2.setFont(new Font("", Font.BOLD, 32));
        JRadioButton rb1 = new JRadioButton("男");
        JRadioButton rb2 = new JRadioButton("女");
        rb1.setFont(new Font("", Font.PLAIN, 32));
        rb2.setFont(new Font("", Font.PLAIN, 32));
        ButtonGroup group1 = new ButtonGroup();
        group1.add(rb1);
        group1.add(rb2);
        gender.add(l2);
        gender.add(rb1);
        gender.add(rb2);
        panel.add(gender);

        JPanel id = new JPanel();
        JLabel l3 = new JLabel("　身份证号：", SwingConstants.LEFT);
        l3.setFont(new Font("", Font.BOLD, 32));
        JTextField f3 = new JTextField(20);
        f3.setFont(new Font("", Font.PLAIN, 32));
        id.add(l3);
        id.add(f3);
        panel.add(id);

        JPanel year = new JPanel();
        JLabel l4 = new JLabel("　入职年份：", SwingConstants.LEFT);
        Calendar calendar = Calendar.getInstance();
        l4.setFont(new Font("", Font.BOLD, 32));
        JTextField f4 = new JTextField("" + calendar.get(Calendar.YEAR), 20);
        f4.setFont(new Font("", Font.PLAIN, 32));
        year.add(l4);
        year.add(f4);
        panel.add(year);

        JPanel statement = new JPanel();
        JLabel l5 = new JLabel("　就职状态：", SwingConstants.LEFT);
        l5.setFont(new Font("", Font.BOLD, 32));
        JRadioButton rb3 = new JRadioButton("正常");
        JRadioButton rb4 = new JRadioButton("休假");
        JRadioButton rb5 = new JRadioButton("停职");
        rb3.setFont(new Font("", Font.PLAIN, 32));
        rb4.setFont(new Font("", Font.PLAIN, 32));
        rb5.setFont(new Font("", Font.PLAIN, 32));
        ButtonGroup group2 = new ButtonGroup();
        group2.add(rb3);
        group2.add(rb4);
        group2.add(rb5);
        statement.add(l5);
        statement.add(rb3);
        statement.add(rb4);
        statement.add(rb5);
        panel.add(statement);

        JPanel line = new JPanel();
        JLabel l6 = new JLabel("所属线路号：", SwingConstants.LEFT);
        l6.setFont(new Font("", Font.BOLD, 32));

        JComboBox jb = new JComboBox();
        jb.setFont(new Font("", Font.PLAIN, 30));
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ResultSet set = db.select("SELECT 线路号 FROM 线路,队长 WHERE 隶属车队号 = 管辖车队号 AND LEFT(USER(), 6) = 队长工号");
        try {
            while (set.next()) jb.addItem(set.getInt("线路号"));
        } catch (SQLException se) {
            se.printStackTrace();
        }
        line.add(l6);
        line.add(jb);
        panel.add(line);
        panel.add(new Panel());

        JPanel check = new JPanel();
        JButton button = new JButton();
        button.setText("提交");
        button.setFont(new Font("", Font.BOLD, 36));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = f1.getText();
                String sid = f3.getText();
                String year = f4.getText();
                int line = (int) jb.getSelectedItem();
                String gender = "女";
                String statement = "正常";
                if (rb1.isSelected()) {
                    gender = "男";
                }
                if (rb4.isSelected()) {
                    statement = "休假";
                } else if (rb5.isSelected()) {
                    statement = "停职";
                }
                db.insertDriverInformation(name, gender, sid, year, statement, line);

                JFrame success = new JFrame("提示");
                success.setSize(480, 300);
                Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
                success.setBounds(p.x - success.getWidth() / 2, p.y - success.getHeight() / 2,
                        success.getWidth(), success.getHeight());
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JLabel label = new JLabel("录入成功！", SwingConstants.CENTER);
                label.setFont(new Font("", Font.BOLD, 48));
                success.add(label);
                success.setVisible(true);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(new Date());
                JPanel[] detailList = {
                        createCard0(),
                        createCard1("1999-12-31 23:59:59", date),
                        createCard2(),
                        createCard3(),
                        createCard4("1999-12-31 23:59:59", date),
                        createCard5(),
                        createCard6(),
                        createCard7(),
                        createCard8(),
                        createCard9(),
                        createCard10()
                };

                for (int i = 0; i < detailList.length; i++) {
                    cards.add(detailList[i], "card" + i);
                }
            }
        });
        check.add(button);
        panel.add(check);

        return panel;
    }

    private JPanel createCard9() {
        if (POSS.equals("司机")) return new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 1));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("请按格式录入违章信息。", SwingConstants.CENTER);
        msg.setFont(new Font("", Font.PLAIN, 48));
        panel.add(msg);
        panel.add(new JPanel());

        JPanel id = new JPanel();
        JLabel l1 = new JLabel("违章司机工号：", SwingConstants.LEFT);
        l1.setFont(new Font("", Font.BOLD, 32));
        JTextField f1 = new JTextField(20);
        f1.setFont(new Font("", Font.PLAIN, 32));
        id.add(l1);
        id.add(f1);
        panel.add(id);

        JPanel type = new JPanel();
        JLabel l2 = new JLabel("　　违章类别：", SwingConstants.LEFT);
        l2.setFont(new Font("", Font.BOLD, 32));
        JTextField f2 = new JTextField(20);
        f2.setFont(new Font("", Font.PLAIN, 32));
        type.add(l2);
        type.add(f2);
        panel.add(type);

        JPanel time = new JPanel();
        JLabel l3 = new JLabel("　　违章时间：", SwingConstants.LEFT);
//        Calendar calendar = Calendar.getInstance();
        l3.setFont(new Font("", Font.BOLD, 32));
        JTextField f3 = new JTextField(20);
        f3.setFont(new Font("", Font.PLAIN, 32));
        time.add(l3);
        time.add(f3);
        panel.add(time);

        JPanel sid = new JPanel();
        JLabel l4 = new JLabel("　所处站点号：", SwingConstants.LEFT);
        l4.setFont(new Font("", Font.BOLD, 32));
        JTextField f4 = new JTextField(20);
        f4.setFont(new Font("", Font.PLAIN, 32));
        sid.add(l4);
        sid.add(f4);
        panel.add(sid);

        JPanel cid = new JPanel();
        JLabel l5 = new JLabel("所驾车车牌号：", SwingConstants.LEFT);
        l5.setFont(new Font("", Font.BOLD, 32));
        JTextField f5 = new JTextField(20);
        f5.setFont(new Font("", Font.PLAIN, 32));
        cid.add(l5);
        cid.add(f5);
        panel.add(cid);

        JPanel remark = new JPanel();
        JLabel l6 = new JLabel("　　　　备注：", SwingConstants.LEFT);
        l6.setFont(new Font("", Font.BOLD, 32));
        JTextField f6 = new JTextField(20);
        f6.setFont(new Font("", Font.PLAIN, 32));
        remark.add(l6);
        remark.add(f6);
        panel.add(remark);
        panel.add(new Panel());

        JPanel check = new JPanel();
        JButton button = new JButton();
        button.setText("提交");
        button.setFont(new Font("", Font.BOLD, 36));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(f1.getText());
                String type = f2.getText();
                String time = f3.getText();
                int sid = Integer.parseInt(f4.getText());
                String cid = f5.getText();
                String remark = f6.getText();
                db.insertViolationInformation(id, cid, sid, type, time, remark);

                JFrame success = new JFrame("提示");
                success.setSize(480, 300);
                Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
                success.setBounds(p.x - success.getWidth() / 2, p.y - success.getHeight() / 2,
                        success.getWidth(), success.getHeight());
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JLabel label = new JLabel("录入成功！", SwingConstants.CENTER);
                label.setFont(new Font("", Font.BOLD, 48));
                success.add(label);
                success.setVisible(true);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(new Date());
                JPanel[] detailList = {
                        createCard0(),
                        createCard1("1999-12-31 23:59:59", date),
                        createCard2(),
                        createCard3(),
                        createCard4("1999-12-31 23:59:59", date),
                        createCard5(),
                        createCard6(),
                        createCard7(),
                        createCard8(),
                        createCard9(),
                        createCard10()
                };

                for (int i = 0; i < detailList.length; i++) {
                    cards.add(detailList[i], "card" + i);
                }
            }
        });
        check.add(button);
        panel.add(check);

        return panel;
    }

    private JPanel createCard10() {
        if (Objects.equals(POSS, "路队长") || POSS.equals("司机")) return new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11, 1));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel msg = new JLabel("输入所需要修改信息的司机工号，并输入修改后的其他信息", SwingConstants.CENTER);
        msg.setFont(new Font("", Font.PLAIN, 42));
        panel.add(msg);
        panel.add(new JPanel());

        JPanel id = new JPanel();
        JLabel l0 = new JLabel("　　　工号：", SwingConstants.LEFT);
        l0.setFont(new Font("", Font.BOLD, 32));
        JTextField f0 = new JTextField(20);
        f0.setFont(new Font("", Font.PLAIN, 32));
        id.add(l0);
        id.add(f0);
        panel.add(id);

        JPanel name = new JPanel();
        JLabel l1 = new JLabel("　　　姓名：", SwingConstants.LEFT);
        l1.setFont(new Font("", Font.BOLD, 32));
        JTextField f1 = new JTextField(20);
        f1.setFont(new Font("", Font.PLAIN, 32));
        name.add(l1);
        name.add(f1);
        panel.add(name);

        JPanel gender = new JPanel();
        JLabel l2 = new JLabel("　　　性别：", SwingConstants.LEFT);
        l2.setFont(new Font("", Font.BOLD, 32));
        JRadioButton rb1 = new JRadioButton("男");
        JRadioButton rb2 = new JRadioButton("女");
        rb1.setFont(new Font("", Font.PLAIN, 32));
        rb2.setFont(new Font("", Font.PLAIN, 32));
        ButtonGroup group1 = new ButtonGroup();
        group1.add(rb1);
        group1.add(rb2);
        gender.add(l2);
        gender.add(rb1);
        gender.add(rb2);
        panel.add(gender);

        JPanel sid = new JPanel();
        JLabel l3 = new JLabel("　身份证号：", SwingConstants.LEFT);
        l3.setFont(new Font("", Font.BOLD, 32));
        JTextField f3 = new JTextField(20);
        f3.setFont(new Font("", Font.PLAIN, 32));
        sid.add(l3);
        sid.add(f3);
        panel.add(sid);

        JPanel year = new JPanel();
        JLabel l4 = new JLabel("　入职年份：", SwingConstants.LEFT);
        Calendar calendar = Calendar.getInstance();
        l4.setFont(new Font("", Font.BOLD, 32));
        JTextField f4 = new JTextField("" + calendar.get(Calendar.YEAR), 20);
        f4.setFont(new Font("", Font.PLAIN, 32));
        year.add(l4);
        year.add(f4);
        panel.add(year);

        JPanel statement = new JPanel();
        JLabel l5 = new JLabel("　就职状态：", SwingConstants.LEFT);
        l5.setFont(new Font("", Font.BOLD, 32));
        JRadioButton rb3 = new JRadioButton("正常");
        JRadioButton rb4 = new JRadioButton("休假");
        JRadioButton rb5 = new JRadioButton("停职");
        JRadioButton rb6 = new JRadioButton("离职");
        rb3.setFont(new Font("", Font.PLAIN, 32));
        rb4.setFont(new Font("", Font.PLAIN, 32));
        rb5.setFont(new Font("", Font.PLAIN, 32));
        rb6.setFont(new Font("", Font.PLAIN, 32));
        ButtonGroup group2 = new ButtonGroup();
        group2.add(rb3);
        group2.add(rb4);
        group2.add(rb5);
        group2.add(rb6);
        statement.add(l5);
        statement.add(rb3);
        statement.add(rb4);
        statement.add(rb5);
        statement.add(rb6);
        panel.add(statement);

        JPanel line = new JPanel();
        JLabel l6 = new JLabel("所属线路号：", SwingConstants.LEFT);
        l6.setFont(new Font("", Font.BOLD, 32));

        JComboBox jb = new JComboBox();
        jb.setFont(new Font("", Font.PLAIN, 30));
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ResultSet set = db.select("SELECT 线路号 FROM 线路,队长 WHERE 隶属车队号 = 管辖车队号 AND LEFT(USER(), 6) = 队长工号");
        try {
            while (set.next()) jb.addItem(set.getInt("线路号"));
        } catch (SQLException se) {
            se.printStackTrace();
        }
        line.add(l6);
        line.add(jb);
        panel.add(line);
        panel.add(new Panel());

        JPanel check = new JPanel();
        JButton button = new JButton();
        button.setText("提交");
        button.setFont(new Font("", Font.BOLD, 36));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(f0.getText());
                String name = f1.getText();
                String sid = f3.getText();
                int year = Integer.parseInt(f4.getText());
                int line = (int) jb.getSelectedItem();
                String gender = "女";
                String statement = "正常";
                if (rb1.isSelected()) {
                    gender = "男";
                }
                if (rb4.isSelected()) {
                    statement = "休假";
                } else if (rb5.isSelected()) {
                    statement = "停职";
                } else if (rb6.isSelected()) {
                    statement = "离职";
                }
                db.updatesDriverInformation(id, name, gender, sid, year, statement, line);

                JFrame success = new JFrame("提示");
                success.setSize(480, 300);
                Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
                success.setBounds(p.x - success.getWidth() / 2, p.y - success.getHeight() / 2,
                        success.getWidth(), success.getHeight());
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JLabel label = new JLabel("更改成功！", SwingConstants.CENTER);
                label.setFont(new Font("", Font.BOLD, 48));
                success.add(label);
                success.setVisible(true);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(new Date());
                JPanel[] detailList = {
                        createCard0(),
                        createCard1("1999-12-31 23:59:59", date),
                        createCard2(),
                        createCard3(),
                        createCard4("1999-12-31 23:59:59", date),
                        createCard5(),
                        createCard6(),
                        createCard7(),
                        createCard8(),
                        createCard9(),
                        createCard10()
                };

                for (int i = 0; i < detailList.length; i++) {
                    cards.add(detailList[i], "card" + i);
                }
            }
        });
        check.add(button);
        panel.add(check);

        return panel;
    }

    /* 各操作选项卡，以及对应的按钮监听器 */
    class Button0ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card0");
            title.setText("查询车队成员信息");
        }
    }

    class Button1ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card1");
            title.setText("查询车队成员违章信息");
        }
    }

    class Button2ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card2");
            title.setText("查询车队统计违章信息");
        }
    }

    class Button3ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card3");
            title.setText("查询线路成员信息");
        }
    }

    class Button4ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card4");
            title.setText("查询线路成员违章信息");
        }
    }

    class Button5ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card5");
            title.setText("查询线路统计违章信息");
        }
    }

    class Button6ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card6");
            title.setText("查询个人信息");
        }
    }

    class Button7ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card7");
            title.setText("查询个人违章信息");
        }
    }

    class Button8ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card8");
            title.setText("录入司机信息");
        }
    }

    class Button9ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card9");
            title.setText("录入违章信息");
        }
    }

    class Button10ActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((CardLayout) (cards.getLayout())).show(cards, "card10");
            title.setText("修改司机信息");
        }
    }

    public static void main(String[] args) {
        new MainPanel("100000", "123456", "队长");
    }
}
