package student;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//教师登录界面
class TeacherFrame extends JFrame{
	JTabbedPane tabPane=new JTabbedPane();
	TeacherDetail detail=new TeacherDetail();
	Selectedstudents sstu=new Selectedstudents();
	ChangePassword1 cpassword=new ChangePassword1();
	//GradeInfo gradeInfo=new GradeInfo();

	TeacherFrame() {
		super("疫情上报系统");
		add("Center", tabPane);
		this.setBounds(200, 200, 500, 345);
		tabPane.add("个人信息查看", detail);
		tabPane.add("疫情上报管理", sstu);
		tabPane.add("登录密码修改", cpassword);
		//tabPane.add("成绩信息管理", gradeInfo);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}
