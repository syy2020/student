package student;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;

class TeacherInfo extends JPanel implements ActionListener,Listener {// 学生信息管理
	
	JPanel panel = new JPanel();
	JButton btnAdd = new JButton("增加");
	JButton btnDelete = new JButton("删除");
	JButton btnAlter = new JButton("修改");
	JButton btnSearch = new JButton("查询");
	JButton btnDisplay = new JButton("刷新");
	JTable table;
	JScrollPane scroll;
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	Object[][] columnValues;
	String[] columnNames;
	DefaultTableModel tableModel;
	
	TeacherInfo() {// 构造方法
		setLayout(new BorderLayout(0, 0));
		add(panel, BorderLayout.NORTH);
		panel.add(btnAdd);
		panel.add(btnDelete);
		panel.add(btnAlter);
		panel.add(btnSearch);
		panel.add(btnDisplay);
		btnAdd.addActionListener(this);
		btnDelete.addActionListener(this);
		btnAlter.addActionListener(this);
		btnSearch.addActionListener(this);
		btnDisplay.addActionListener(this);
		columnNames=new String []{ "院工号", "姓名", "用户名" };
		getAllStudents();
		tableModel = new DefaultTableModel(columnValues,columnNames);
		table = new JTable(tableModel);
		scroll = new JScrollPane(table);
		add(scroll);
	}
	
	//获取学生列表
	public void getAllStudents(){
		int count = 0,index=0;
		connDB(); // 连接数据库	
		try {
			rs = stmt.executeQuery("select count(*) from t");
			rs.next();
			count=rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		columnValues = new Object[count][5];
		try {
			rs = stmt.executeQuery("select * from t order by 院工号");
			while (rs.next()) {
				columnValues[index][0] = rs.getString("tno");
				columnValues[index][1] = rs.getString("tname");
				columnValues[index][2] = rs.getString("tsex");
				//columnValues[index][3] = rs.getString("tpost");
				//columnValues[index][4] = rs.getString("tdept");
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
	}

	//刷新学生列表
	public void refresh() {
		getAllStudents();
	    tableModel.setDataVector(columnValues, columnNames);
		tableModel.fireTableDataChanged();
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

	public void closeDB() // 关闭数据库连接
	{
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 删除某个学生的基本信息
	public void delete() {
		int row = -1;
		row = table.getSelectedRow();
		connDB();
		if (row == -1) {// 判断要删除的信息是否被选中
			JOptionPane.showMessageDialog(null, "请选择要删除的记录！");
		} else {
			String tname=columnValues[row][4].toString();
			String tno = columnValues[row][0].toString();
			try {
				stmt.executeUpdate("delete from su where 学号='"+tname+"'");   //删除选课表中的记录
				stmt.executeUpdate("delete from t where 院工号='"+tno+"'");    //删除学生表中的记录
				JOptionPane.showMessageDialog(null, "记录删除成功！");
				refresh();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		closeDB();
	}

	// 修改某个教师的基本信息
	public void update() {
		int row = -1;
		row = table.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "请选择要修改的记录！");
		} else {
			TeacherAdd tadd = new TeacherAdd(this);
			tadd.setTitle("修改");
			tadd.tsno.setText(columnValues[row][0].toString());
			tadd.tsname.setText(columnValues[row][1].toString());
			//tadd.cbssex.setSelectedItem(columnValues[row][2].toString());
			//tadd.tpost.setText(columnValues[row][3].toString());
			//tadd.cbsdept.setSelectedItem(columnValues[row][4].toString());
			tadd.tsno.setEnabled(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "增加") {
			new TeacherAdd(this);
		}
		if (e.getActionCommand() == "删除") {
			delete();
		}
		if (e.getActionCommand() == "修改") {
			update();
		}
		if (e.getActionCommand() == "查询") {
			new InputNumber("编号",this);
		}
		if (e.getActionCommand() == "刷新") {
			refresh();
		}
	}

	@Override
	public void getMessage(String message) {
		columnValues = new Object[1][5];
		connDB();
		try {
			rs = stmt.executeQuery("select * from t where 院工号='" +message+ "'");
			rs.next();
			columnValues[0][0] = rs.getString("tno");
			columnValues[0][1] = rs.getString("tname");
			columnValues[0][2] = rs.getString("tsex");
			//columnValues[0][3] = rs.getInt("tpost");
			//columnValues[0][4] = rs.getString("tdept");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (columnValues[0][1] == null) {
			JOptionPane.showMessageDialog(null, "编号不存在！");
			refresh();
		} else {
		    tableModel.setDataVector(columnValues, columnNames);
			tableModel.fireTableDataChanged();
		}
		closeDB();
	}

	@Override
	public void refreshUI() {
		refresh();
	}
}