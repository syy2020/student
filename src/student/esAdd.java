package student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

//用于课程信息管理中增加或修改某条记录的界面
class esAdd extends JFrame implements ActionListener{
	
	JLabel l学号 = new JLabel("学号 ：");
	JLabel l姓名 = new JLabel("姓名：");
	JLabel l填报时间 = new JLabel("填报时间：");
	JLabel l所在地 = new JLabel("所在地：");
	JLabel l当前健康状况 = new JLabel("当前健康状况：");
	JTextField t学号 = new JTextField(15);
	JTextField t姓名 = new JTextField(15);
	JTextField t填报时间 = new JTextField(35);
	JTextField t所在地 = new JTextField(25);
	//JComboBox<String> cb所在地 = new JComboBox<String>();
	JTextField t当前健康状况 = new JTextField(35);
	
	JButton btnOK = new JButton("确     定");
	JButton btnCancel = new JButton("取     消");
	
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	Listener listener;

	public esAdd(Listener listener) {// 构造方法
		this.listener=listener;
		setTitle("增加");
		setBounds(900, 200, 300, 300);
		//cbcdept.addItem("卓越工程师");
		//cbcdept.addItem("网络安全");
		//cbcdept.addItem("物联网工程");
		//cbcdept.addItem("软件工程");
		//cbcdept.addItem("计算机软件");
		//cbcdept.addItem("计算机应用");
		getContentPane().setLayout(null);
		l学号.setBounds(55, 10, 60, 20);
		getContentPane().add(t学号);
		t学号.setBounds(115, 10, 120, 20);
		getContentPane().add(t学号);
		l姓名.setBounds(55, 40, 60, 20);
		getContentPane().add(l姓名);
		t姓名.setBounds(115, 40, 120, 20);
		getContentPane().add(t姓名);
		l填报时间.setBounds(55, 70, 60, 20);
		getContentPane().add(l填报时间);
		t填报时间.setBounds(115, 70, 120, 20);
		getContentPane().add(t填报时间);
		l所在地.setBounds(55, 100, 60, 20);
		getContentPane().add(l所在地);
		t所在地.setBounds(115, 100, 120, 20);
		getContentPane().add(t所在地);
		l当前健康状况.setBounds(55, 130, 75, 20);
		getContentPane().add(l当前健康状况);
		t当前健康状况.setBounds(125, 130, 110, 20);
		getContentPane().add(t当前健康状况);
		btnOK.setBounds(70, 160, 160, 30);
		getContentPane().add(btnOK);
		btnCancel.setBounds(70, 200, 160, 30);
		getContentPane().add(btnCancel);
		btnOK.addActionListener(this);
		btnCancel.addActionListener(this);
		this.setVisible(true);
	}

	public void connDB() { // 连接数据库
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433; DatabaseName=ers",
					"SA", "111111");
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeDB() // 关闭连接
	{
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "确     定") {
			if(getTitle()=="修改"){
				update();
			}else{
				insert();
			}
		}
		if (e.getActionCommand() == "取     消") {
			this.setVisible(false);
		}
	}
	
	public void update(){//更新记录
		String kch = t学号.getText();
		String kcm = t姓名.getText();
        int xfs = 0;
        String yx = t填报时间.getText();
		//String yx = cbcdept.getSelectedItem().toString();
		String rkjs = t所在地.getText();
		connDB();
		try{
			xfs=Integer.parseInt((String)t当前健康状况.getText());
			if(kcm.equals("")||rkjs.equals("")||xfs==0){
				JOptionPane.showMessageDialog(null, "不能有空值！");
			}else{
				String sql="update su set 姓名='"+kcm+"',当前健康状况='"+xfs+
						"',填报时间='"+yx+"',所在地='"+rkjs+"' where 学号='"+kch+"'";
				try {
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "修改成功！");
					listener.refreshUI();
					setVisible(false);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}catch (NumberFormatException e) {// 判断年龄是否为数字
			//JOptionPane.showMessageDialog(null, "必须是整数！");
			t当前健康状况.setText("");
		}
		closeDB();
	}

	public void insert() 
	{ // 插入记录
		String kch = t学号.getText();
		String kcm = t姓名.getText();
        int xfs = 0;
        String yx = t填报时间.getText();
		//String yx = cbcdept.getSelectedItem().toString();
		String rkjs = t所在地.getText();
		connDB();
		try{
			xfs=Integer.parseInt((String)t当前健康状况.getText());
			if(kch.equals("")||kcm.equals("")||rkjs.equals("")||xfs==0){
				JOptionPane.showMessageDialog(null, "不能有空值！");
			}
			else{
				String sql="insert into su values('"+kch+"','"+kcm+"','"+xfs
						+"','"+yx+"','"+rkjs+"')";
				try 
				{
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "增加成功！");
					listener.refreshUI();
					setVisible(false);
				}
				catch (SQLException e)
				{
					//JOptionPane.showMessageDialog(null, "已存在！");
					e.printStackTrace();
				}
			}
		}catch (NumberFormatException e) {// 判断年龄是否为数字
			JOptionPane.showMessageDialog(null, "必须是整数！");
			t当前健康状况.setText("");
		}
		closeDB();
	}
}