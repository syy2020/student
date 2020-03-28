package student;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//管理员界面
class ManagerFrame extends JFrame {
	JTabbedPane tabPane=new JTabbedPane();
	StudentInfo studentInfo=new StudentInfo();
	esInfo courseInfo=new esInfo();
	TeacherInfo teacherinfo = new TeacherInfo();

	ManagerFrame() {// 构造方法
		super("西北师范大学疫情防控子系统");
		this.setBounds(450, 450, 750, 595);
		add("Center", tabPane);
		tabPane.add("学生信息管理", studentInfo);
		tabPane.add("疫情上报信息管理", courseInfo);
		tabPane.add("学院负责人信息管理",teacherinfo);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}

