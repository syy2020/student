package student;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

//学生选课系统
public class esSelect extends JPanel implements ActionListener{

	JTable tableSelected;   //已选
	JTable tableNotSelected;//可选
	JScrollPane scrollSelected;   //已选
	JScrollPane scrollNotSelected;//可选
	JButton btnSelect = new JButton("疫情填报");
	JButton btnDrop = new JButton("删除信息");
	JLabel labelSelect = new JLabel("已填信息");
	JLabel labelNotSelect = new JLabel("疫情填报");
	JPanel panelHeader=new JPanel();
	JPanel panelContent = new JPanel();
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	Object[][] valuesSelected,valuesNotSelected;
	String[] columnNames;
	DefaultTableModel modelSelected,modelNotSelected;
	String 学号=LoginFrame.loginName;
	
	public esSelect() {
		setLayout(new BorderLayout(0, 0));
		panelHeader.add(btnSelect);
		panelHeader.add(btnDrop);
		add(panelHeader, BorderLayout.NORTH);
		btnSelect.addActionListener(this);
		btnDrop.addActionListener(this);
		add(panelContent);
		panelContent.setLayout(null);
		columnNames = new String []{"学号","姓名","填报时间","所在地","当前健康状况"};
		getNotSelected(); //获取可选课程
		modelNotSelected=new DefaultTableModel(valuesNotSelected,columnNames);
		labelNotSelect.setBounds(94, 0, 60, 30);
		panelContent.add(labelNotSelect);
		tableNotSelected=new JTable(modelNotSelected);
		scrollNotSelected=new JScrollPane(tableNotSelected);
		scrollNotSelected.setLocation(4, 30);
		scrollNotSelected.setSize(240, 210);
		panelContent.add(scrollNotSelected);
		getSelected();  //获取已选课程
		modelSelected=new DefaultTableModel(valuesSelected,columnNames);
		labelSelect.setBounds(338, 0, 60, 30);
		panelContent.add(labelSelect);
		tableSelected=new JTable(modelSelected);
		scrollSelected=new JScrollPane(tableSelected);
		scrollSelected.setSize(240, 210);
		scrollSelected.setLocation(248, 30);
		panelContent.add(scrollSelected);
	}
	
	public void getNotSelected(){
		int count=0,i = 0;
		connDB();
		try{
			rs=stmt.executeQuery("select count(*) from su where 学号");
			rs.next();
			count=rs.getInt(1);
		}catch(SQLException e){
			e.printStackTrace();
		}
		valuesNotSelected = new Object[count][5];
		try {
			rs = stmt.executeQuery("select * from su where 学号 ");
			while (rs.next()) {
				valuesNotSelected[i][0] = rs.getString("学号");
				valuesNotSelected[i][1] = rs.getString("姓名");
				valuesNotSelected[i][2] = rs.getInt("填报时间");
				valuesNotSelected[i][3] = rs.getString("所在地");
				valuesNotSelected[i][4] = rs.getString("当前健康状况");
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
	}
	
	
	public void getSelected(){
		int count=0,i = 0;
		connDB();
		try{
			rs=stmt.executeQuery("select count(*) from su where 学号");
			rs.next();
			count=rs.getInt(1);
		}catch(SQLException e){
			e.printStackTrace();
		}
		valuesSelected = new Object[count][5];
		try {
			rs = stmt.executeQuery("select * from su where 学号");
			while (rs.next()) {
				valuesSelected[i][0] = rs.getString("学号");
				valuesSelected[i][1] = rs.getString("姓名");
				valuesSelected[i][2] = rs.getInt("填报时间");
				valuesSelected[i][3] = rs.getString("所在地");
				valuesSelected[i][4] = rs.getString("当前健康状况");
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
	}

	//选课和退课按钮监听
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "选课"){
			selectCourse();
		}
		if(e.getActionCommand() == "退课"){
			dropCourse();
		}	
	}
	
	public void selectCourse(){
		int rowNotSelected = -1,rowSelected=-1;
		rowNotSelected = tableNotSelected.getSelectedRow();
		rowSelected =tableSelected.getSelectedRow();
		connDB();
		if (rowNotSelected == -1 && rowSelected == -1) {
			JOptionPane.showMessageDialog(null, "请选择要选的课程！");
		}else if(rowNotSelected == -1 && rowSelected != -1){
			JOptionPane.showMessageDialog(null, "您已选修此课程，请重新选择！");
		}else{
			try{
				String cno=valuesNotSelected[rowNotSelected][0].toString();
				String sql="insert into su values('"+学号+"','"+cno+"',null)";
				stmt.executeUpdate(sql);
				JOptionPane.showMessageDialog(null, "选课成功！");
				refresh();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		closeDB();
	}
	
	public void dropCourse(){
		int rowNotSelected = -1,rowSelected=-1;
		rowNotSelected = tableNotSelected.getSelectedRow();
		rowSelected =tableSelected.getSelectedRow();
		connDB();
		if (rowNotSelected == -1 && rowSelected == -1) {
			JOptionPane.showMessageDialog(null, "请选择要删除信息！");
		} else if(rowNotSelected != -1 && rowSelected == -1){
			JOptionPane.showMessageDialog(null, "您未填报，请重新选择！");
		}else{
			try{
				String cno=valuesSelected[rowSelected][0].toString();
				String sql="delete from su where 学号='"+学号+"' and 姓名='"+cno+"'";
				stmt.executeUpdate(sql);
				JOptionPane.showMessageDialog(null, "退课成功！");
				refresh();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		closeDB();
	}
	
	public void refresh(){
		getNotSelected();
		//getSelected();
		modelNotSelected.setDataVector(valuesNotSelected, columnNames);
		modelNotSelected.fireTableDataChanged();
		modelSelected.setDataVector(valuesSelected, columnNames);
		modelSelected.fireTableDataChanged();
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
}
