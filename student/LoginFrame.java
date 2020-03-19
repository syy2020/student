package student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


class LoginFrame extends JFrame implements ActionListener, ItemListener {
	
	JPanel p1 = null,p2 = null,p3 = null;
	JLabel userNumber = new JLabel("用户：");
	JTextField txtUser = new JTextField(); 
	
	JLabel password = new JLabel("密码：");
	JPasswordField txtPwd = new JPasswordField();
	
	JLabel role = new JLabel("身份：");
	JComboBox cbrole = new JComboBox();
	
	JButton btnLogin = new JButton("登录");
	JButton btnReset = new JButton("重置");
	JButton btnCancel = new JButton("取消");
	
	JLabel imageLabel;
	Icon image;
	static int OK = 1;
	static int CANCEL = 0;
	static String loginName="";
	int actionCode = 0;
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	int index = 0;

	public LoginFrame() {// 构造方法
		super("西北师范大学疫情上报系统");
		p1 = new JPanel();
		p1.setBounds(80, 10, 340, 120);
		p2 = new JPanel();
		p3 = new JPanel();
		p3.setBounds(80, 257, 340, 37);
		cbrole.addItem("学校负责人");
		cbrole.addItem("学生");
		cbrole.addItem("学院负责人");
		image = new ImageIcon("src/images/shu.png");
		p1.setLayout(new BorderLayout(0, 0));
		imageLabel = new JLabel(image);
		p1.add(imageLabel, BorderLayout.EAST);
		this.setBounds(200, 200, 500, 345);
		p2.setBounds(150, 147, 200, 110);
		p2.setLayout(new GridLayout(4, 2));
		p2.add(userNumber);
		p2.add(txtUser);
		p2.add(password);
		p2.add(txtPwd);
		p2.add(role);
		p2.add(cbrole);
		p3.add(btnLogin);
		p3.add(btnReset);
		p3.add(btnCancel);
		getContentPane().setLayout(null);
		getContentPane().add(p1);
		getContentPane().add(p2);
		getContentPane().add(p3);
		//this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
		this.setVisible(true);
		//添加监听事件
		btnLogin.addActionListener(this);
		cbrole.addItemListener(this);
		btnReset.addActionListener(this);
		btnCancel.addActionListener(this);
	}

	// 连接数据库
	public void connDB() { 
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

	// 关闭连接
	public void closeDB() 
	{
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//获取用户所选择身份的下标
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			JComboBox jcb = (JComboBox) e.getSource();
			index = jcb.getSelectedIndex();
		}
	}

	//登录选项按钮的监听
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		String un = null;
		String pw = null;
		//监听到点击的按钮是登录按钮
		if (source == btnLogin) {
			// 判断是否输入了用户名和密码
			if (txtUser.getText().equals("") || txtPwd.getPassword().equals("")) {
				JOptionPane.showMessageDialog(null, "登录名和密码不能为空！");
			}else {
				if(index==0) {//管理员登录
					if(txtUser.getText().equals("1")){
						if(String.valueOf(txtPwd.getPassword()).equals("1")) {
							new ManagerFrame();// 进入管理员界面
							setVisible(false);
						}else {
							JOptionPane.showMessageDialog(null, "密码错误！");
							txtPwd.setText("");
						}
					}else {
						JOptionPane.showMessageDialog(null, "登录名错误！");
						txtUser.setText("");
						txtPwd.setText("");
					}
				}else if(index==1) {//学生登录
					connDB();
					try {
					    rs = stmt.executeQuery("select * from s");
					    boolean user=false;
					    while (rs.next()) {
					    	un = rs.getString("用户名").trim();
					        pw = rs.getString("密码").trim();
					        if (txtUser.getText().equals(un)) {//登录名正确
					        	user=true;
					            if (String.valueOf(txtPwd.getPassword()).equals(pw)) {//密码正确
					                actionCode = OK;
					                this.setVisible(false);
					                loginName=un;
									new StudentFrame();
								    break;
								} else {//密码错误
								    JOptionPane.showMessageDialog(null, "密码错误！");
								    txtPwd.setText("");
								}
						    }
						}
					    if(!user) {
					    	JOptionPane.showMessageDialog(null, "登录名错误！");
							txtUser.setText("");
							txtPwd.setText("");
					    }
				    }catch (SQLException e1) {
					    e1.printStackTrace();
				    }
					closeDB();
			    }else
			    {
			    	connDB();
					try {
					    rs = stmt.executeQuery("select * from t");
					    boolean user=false;
					    while (rs.next()) {
					    	un = rs.getString("用户名").trim();
					        pw = rs.getString("密码").trim();
					        if (txtUser.getText().equals(un)) {//登录名正确
					        	user=true;
					            if (String.valueOf(txtPwd.getPassword()).equals(pw)) {//密码正确
					                actionCode = OK;
					                this.setVisible(false);
					                loginName=un;
									new TeacherFrame();//
								    break;
								} else {//密码错误
								    JOptionPane.showMessageDialog(null, "密码错误！");
								    txtPwd.setText("");
								}
						    }
						}
					    if(!user) {
					    	JOptionPane.showMessageDialog(null, "登录名错误！");
							txtUser.setText("");
							txtPwd.setText("");
					    }
				    }catch (SQLException e1) {
					    e1.printStackTrace();
				    }
					closeDB();
			    }
			}
		} else if (source == btnReset) {
			//监听到点击的按钮是重置按钮
			txtUser.setText("");
			txtPwd.setText("");
		} else if (source == btnCancel) {
			//监听到点击的按钮是取消按钮
			System.exit(0);
		}
	}
}

