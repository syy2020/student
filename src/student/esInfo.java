package student;

import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

class esInfo extends JPanel implements ActionListener,Listener {// 课程信息管理
	
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
	String [] columnNames;
	DefaultTableModel tableModel;

	esInfo() {// 构造方法
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
		columnNames=new String []{"学号","姓名","填报时间","所在地","当前健康状况"};
		getAllCourses();
		tableModel=new DefaultTableModel(columnValues,columnNames);
		table=new JTable(tableModel);
		scroll=new JScrollPane(table);
		add(scroll);
	}
	
	//获取课程列表
	public void getAllCourses(){
		int count=0,index=0;
		connDB();// 连接数据库
		try{
			rs=stmt.executeQuery("select count(*) from su");
			rs.next();
			count=rs.getInt(1);
		}catch(SQLException e){
			e.printStackTrace();
		}
		columnValues=new Object[count][5];
		try{
			rs=stmt.executeQuery("select * from su order by 学号");
			while(rs.next()){
				columnValues[index][0]=rs.getString("学号");
				columnValues[index][1]=rs.getString("姓名");
				columnValues[index][2]=rs.getInt("填报时间");
				columnValues[index][3]=rs.getString("所在地");
				columnValues[index][4]=rs.getString("当前健康状况");
				index++;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		closeDB();
	}

	//刷新课程列表
	public void refresh(){
		getAllCourses();
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

	public void closeDB() // 关闭连接
	{
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 删除某个课程信息
	public void delete() {
		int row = -1;
		row = table.getSelectedRow();
		connDB();
		if (row == -1) {// 判断要删除的信息是否被选中
			JOptionPane.showMessageDialog(null, "请选择要删除的记录！");
		} else {
			String cno=columnValues[row][0].toString();
			try{
				stmt.executeUpdate("delete from su where 学号='"+cno+"'");   //删除选课表中的记录
				stmt.executeUpdate("delete from su where cno='"+cno+"'");    //删除开课表中的记录
				JOptionPane.showMessageDialog(null, "记录删除成功！");
				refresh();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		closeDB();
	}

	// 修改某个课程记录
	public void update() {
		int row = -1;
		row = table.getSelectedRow();
		connDB();
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "请选择要修改的记录！");
		} else {
			esAdd cadd=new esAdd(this);
			cadd.setTitle("修改");
			cadd.t学号.setText(columnValues[row][0].toString());
			cadd.t姓名.setText(columnValues[row][1].toString());
			cadd.t填报时间.setText(columnValues[row][2].toString());
			cadd.t所在地.setText(columnValues[row][3].toString());
			cadd.t当前健康状况.setText(columnValues[row][4].toString());
			cadd.t学号.setEnabled(false);
		}
		closeDB();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "增加") {
			new esAdd(this);
		}
		if (e.getActionCommand() == "删除") {
			delete();
		}
		if (e.getActionCommand() == "修改") {
			update();
		}
		if (e.getActionCommand() == "查询") {
			new InputNumber("学号",this);
		}
		if (e.getActionCommand() == "刷新") {
			refresh();
		}
	}
	
	@Override
	public void refreshUI() {
		refresh();
	}

	@Override
	public void getMessage(String message) {
		connDB();
		columnValues = new Object[1][5];
		try {
			rs = stmt.executeQuery("select * from su where 学号='" +message+ "'");
			rs.next();
			columnValues[0][0] = rs.getString("学号");
			columnValues[0][1] = rs.getString("姓名");
			columnValues[0][2] = rs.getInt("填报时间");
			columnValues[0][3] = rs.getString("所在地");
			columnValues[0][4] = rs.getString("当前健康状况");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (columnValues[0][1] == null) {
			JOptionPane.showMessageDialog(null, "学号不存在！");
			refresh();
		} else {
		    tableModel.setDataVector(columnValues, columnNames);
			tableModel.fireTableDataChanged();
		}
		closeDB();
	}

}

