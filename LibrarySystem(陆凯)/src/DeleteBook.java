import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.sql.*;
import java.util.Random;



public class DeleteBook extends JDialog implements WindowListener,ActionListener{

	private JTextField rm_txf;
	private JButton del_sure_button; 
	private JTextField id_txf;
	private JLabel rm_label;
	private JButton refresh_button ;
	public void windowClosing(WindowEvent e)
	{
		this.dispose();
	}
	public DeleteBook() {
		super();
	
		setTitle("删除书籍");
		getContentPane().setLayout(null);
		setBounds(500, 220, 340, 272);
		this.setVisible(true);

	del_sure_button= new JButton();
		del_sure_button.setText("确定");
		del_sure_button.addActionListener(this);
		del_sure_button.setBounds(171, 174, 106, 28);
		getContentPane().add(del_sure_button);

		id_txf = new JTextField();
		id_txf.setBounds(149, 32, 127, 28);
		getContentPane().add(id_txf);

		final JLabel label = new JLabel();
		label.setText("书籍编号");
		label.setBounds(56, 37, 66, 18);
		getContentPane().add(label);

		final JLabel label_1 = new JLabel();
		label_1.setText("验证码");
		label_1.setBounds(56, 98, 66, 18);
		getContentPane().add(label_1);

		 rm_label= new JLabel();
		 rm_label.setForeground(new Color(255, 0, 255));
		 rm_label.setFont(new Font("", Font.BOLD | Font.ITALIC, 16));
		rm_label.setText("");
		rm_label.setBounds(149, 127, 106, 28);
		getContentPane().add(rm_label);

		rm_txf = new JTextField();
		rm_txf.setBounds(149, 93, 127, 28);
		getContentPane().add(rm_txf);
		
		Random rm=new Random();
		String rm_str1="";
		int i=0;
		for(i=0;i<4;i++)
			rm_str1+=Integer.toString(rm.nextInt(9));
		rm_label.setText(rm_str1);

		refresh_button = new JButton();
		refresh_button.setText("刷新");
		refresh_button.addActionListener(this);
		refresh_button.setBounds(56, 174, 66, 28);
		getContentPane().add(refresh_button);
	}
	public void delBook()
	{
		
		DataBase au_db=new DataBase();
		ResultSet rs=null;
		if(id_txf.getText().trim().equals(""))
		{
			JOptionPane.showMessageDialog(null, "请输入要删除的书籍编号");
			return;
		}
		rs=au_db.getResult("select * from t_books where book_id='"+id_txf.getText().trim()+"'");
		if(id_txf.getText().trim().length()>20)
		{
			JOptionPane.showMessageDialog(null, "输入的书籍编号过长!");
			return;
		}
		try {
			if(rs.next()==false)
			{
				JOptionPane.showMessageDialog(null, "不存在该书籍"+id_txf.getText()+"");
				return;
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		//如果该用户借的有书，则不能删除该用户
		rs=au_db.getResult("select * from t_books where book_id='"+id_txf.getText().trim()+"' and bl_state='否'");
	
		try {
			if(rs.next()==false)
			{
				JOptionPane.showMessageDialog(null, "该书已借出，不能删除!");
				return;
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(rm_txf.getText().trim().equals(rm_label.getText().trim()))
		{
			au_db.updateSql("delete from t_record where book_id='"+id_txf.getText().trim()+"'");
			String delStringStr="delete from t_books where book_id='"+id_txf.getText().trim()+"'";
			au_db.updateSql(delStringStr);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "验证码不正确!");
			return;
		}
		JOptionPane.showMessageDialog(null, "已成功删除"+id_txf.getText().trim()+"书籍!");
		this.dispose();
		
		
		
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(del_sure_button))
		{
			delBook();
			Random rm=new Random();
			String rm_str1="";
			int i=0;
			for(i=0;i<4;i++)
				rm_str1+=Integer.toString(rm.nextInt(9));
			rm_label.setText(rm_str1);
			
		}
		else if(e.getSource().equals(refresh_button))
		{
			Random rm=new Random();
			String rm_str1="";
			int i=0;
			for(i=0;i<4;i++)
				rm_str1+=Integer.toString(rm.nextInt(9));
			rm_label.setText(rm_str1);
		}
			
	}
	public void windowActivated(WindowEvent arg0) {
		
	}

	public void windowClosed(WindowEvent arg0) {
		
	}

	public void windowDeactivated(WindowEvent arg0) {
		
	}

	public void windowDeiconified(WindowEvent arg0) {
		
	}

	public void windowIconified(WindowEvent arg0) {
		
	}
	public void windowOpened(WindowEvent arg0) {
		
	}

}
