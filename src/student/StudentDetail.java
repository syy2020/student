package student;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

//学生个人信息查看
public class StudentDetail extends JPanel implements ActionListener,Listener{
		
	JTable table; //用于显示学生详细信息
	JScrollPane scroll; 
	JButton btnAlter = new JButton("信息修改");
	JPanel panel=new JPanel();
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	Object[][] columnValues;
	String[] columnNames;
	DefaultTableModel tableModel;
	
	StudentDetail() {
		connDB();
		btnAlter.addActionListener(this);
		setLayout(new BorderLayout(0, 0));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(btnAlter);
		add(panel, BorderLayout.NORTH);
		columnValues = new Object[1][5];
		columnNames = new String []{ "学号", "姓名", "性别","年龄",  "专业" };
		try {
			rs = stmt.executeQuery("select * from s where 用户名='" + LoginFrame.loginName
					+ "'"); //查询数据库中与登录名一致的学生信息
			rs.next();
			columnValues[0][0] = rs.getString("学号");
			columnValues[0][1] = rs.getString("姓名");
			columnValues[0][2] = rs.getString("性别");
			columnValues[0][3] = rs.getInt("年龄");
			columnValues[0][4] = rs.getString("专业");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tableModel = new DefaultTableModel(columnValues,columnNames);
		table = new JTable(tableModel);
		scroll = new JScrollPane(table);
		add(scroll);
		closeDB();
	}
	
	public void connDB() { // 连接数据库
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			con = DriverManager
					.getConnection(
							"jdbc:sqlserver://localhost:1433; DatabaseName=ers",
							"SA", "111111");
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeDB() { // 关闭连接
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		StudentAdd sadd = new StudentAdd(this);
		sadd.setTitle("修改");
		sadd.tsno.setText(columnValues[0][0].toString());
		sadd.tsname.setText(columnValues[0][1].toString());
		sadd.cbssex.setSelectedItem(columnValues[0][2].toString());
		sadd.tsage.setText(columnValues[0][3].toString());
		sadd.cbsdept.setSelectedItem(columnValues[0][4].toString());
		sadd.tsno.setEnabled(false);
	}

	@Override
	public void refreshUI() {
		connDB();
		try {
			rs = stmt.executeQuery("select * from s where 用户名='" + LoginFrame.loginName
					+ "'"); //查询数据库中与登录名一致的学生信息
			rs.next();
			columnValues[0][0] = rs.getString("学号");
			columnValues[0][1] = rs.getString("姓名");
			columnValues[0][2] = rs.getString("性别");
			columnValues[0][3] = rs.getInt("年龄");
			columnValues[0][4] = rs.getString("专业");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tableModel.setDataVector(columnValues, columnNames);
		tableModel.fireTableDataChanged();
		closeDB();
	}

	@Override
	public void getMessage(String message) {
	}
}