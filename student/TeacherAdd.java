package student;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

// 用于学生信息管理中增加或修改某条记录的界面
class TeacherAdd extends JFrame implements ActionListener{
	
	JLabel lsno = new JLabel("编号：");
	JLabel lsname = new JLabel("姓名：");
	JLabel lssex = new JLabel("性别：");
	JLabel lpost = new JLabel("职称：");
	JLabel lsdept = new JLabel("专业：");
	
	JTextField tsno = new JTextField(14);
	JTextField tsname = new JTextField(14);
	JComboBox<String> cbssex = new JComboBox<String>();
	JTextField tpost = new JTextField(14);
	JComboBox<String> cbsdept = new JComboBox<String>();
	
	JButton btnOK = new JButton("确     定");
	JButton btnCancel = new JButton("取     消");
	
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	Listener listener;

	public TeacherAdd(Listener listener) {// 构造方法
		
		setTitle("增加");
		setBounds(900, 200, 300, 300);
		cbssex.addItem("男");
		cbssex.addItem("女");
		cbsdept.addItem("卓越工程师");
		cbsdept.addItem("网络安全");
		cbsdept.addItem("物联网工程");
		cbsdept.addItem("软件工程");
		cbsdept.addItem("计算机软件");
		cbsdept.addItem("计算机应用");
		getContentPane().setLayout(null);
		lsno.setBounds(60, 10, 45, 20);
		getContentPane().add(lsno);
		tsno.setBounds(110, 10, 120, 20);
		getContentPane().add(tsno);
		lsname.setBounds(60, 40, 45, 20);
		getContentPane().add(lsname);
		tsname.setBounds(110, 40, 120, 20);
		getContentPane().add(tsname);
		lpost.setBounds(60, 70, 45, 20);
		getContentPane().add(lpost);
		tpost.setBounds(110, 70, 120, 20);
		getContentPane().add(tpost);
		lssex.setBounds(60, 100, 60, 20);
		getContentPane().add(lssex);
		cbssex.setBounds(110, 100, 120, 20);
		getContentPane().add(cbssex);
		lsdept.setBounds(60, 130, 120, 20);
		getContentPane().add(lsdept);
		cbsdept.setBounds(110, 130, 120, 20);
		getContentPane().add(cbsdept);
		btnOK.setBounds(70, 160, 160, 30);
		getContentPane().add(btnOK);
		btnCancel.setBounds(70, 200, 160, 30);
		getContentPane().add(btnCancel);
		btnOK.addActionListener(this);
		btnCancel.addActionListener(this);
		this.setVisible(true);
		this.listener=listener;
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
		if (e.getActionCommand() == "确     定")
		{
			if(getTitle()=="修改")
			{
				update();
			}
			else
			{
				insert();
			}
		}
		if (e.getActionCommand() == "取     消") {
			this.setVisible(false);
		}
	}

	//更新记录
	public void update(){
		String xh=tsno.getText();
		String xm=tsname.getText();
		String nl=tpost.getText();
		String yx=cbsdept.getSelectedItem()+"";
		String xb=cbssex.getSelectedItem()+"";
		connDB();
		try{
			nl=(String)tpost.getText();
			if(xm.equals("")){
				JOptionPane.showMessageDialog(null, "姓名不能有空值！");
			}else{
				String sql="update t set tname='"+xm+"',tsex='"+xb+"',tpost='"+nl+"',tdept='"+yx+"' where tno='"+xh+"'";
				try {
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "修改成功！");
					listener.refreshUI();
					setVisible(false);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}catch (NumberFormatException e) {
			tpost.setText("");
		}
		closeDB();
	}
	
	public void insert() { // 插入记录
		String xh=tsno.getText();
		String xm=tsname.getText();
		String nl="";
		String yx=cbsdept.getSelectedItem().toString();
		String xb=cbssex.getSelectedItem().toString();
		connDB();
		try {
			nl = (String) tpost.getText();
			if(xh.equals("")||xm.equals("")){
				JOptionPane.showMessageDialog(null, "学号、姓名、职称不能有空值！");
			}else{
				String sql="insert into t values('"+xh+"','"+xm+"','"+xb
						+"','"+nl+"','"+yx+"','"+xh+"','"+xh+"')";
				try {
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null,"增加成功！");
					listener.refreshUI();
					setVisible(false);
			    } catch (SQLException e) {
				    JOptionPane.showMessageDialog(null, "学号已存在！");
				    tsno.setText("");
		    	}
			}
		} catch (NumberFormatException e) {
			
			tpost.setText("");
		}
		closeDB();
	}
}
