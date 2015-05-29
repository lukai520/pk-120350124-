import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.sql.*;



public class AddBook extends JDialog implements WindowListener,ActionListener{

	private JTextArea intro_ta;
	private JTextField price_txf;
	private JTextField date_txf;
	private JTextField press_txf;
	private JTextField author_txf;
	private JTextField type_txf;
	private JTextField name_txf;
	private JTextField id_txf;
	private JButton sure_button ;

	public void windowClosing(WindowEvent e)
	{
		this.dispose();
	}
	public AddBook() {
		super();
		setResizable(false);
	
		setTitle("添加书籍");
		getContentPane().setLayout(null);
		setBounds(330, 200, 548, 425);
		
		final JLabel label1 = new JLabel();
		label1.setText("书号");
		label1.setBounds(39, 10, 66, 18);
		getContentPane().add(label1);

		final JLabel label_1 = new JLabel();
		label_1.setText("书名");
		label_1.setBounds(263, 10, 66, 18);
		getContentPane().add(label_1);

		final JLabel label_2 = new JLabel();
		label_2.setText("类型");
		label_2.setBounds(263, 63, 66, 18);
		getContentPane().add(label_2);

		id_txf = new JTextField();
		id_txf.setBounds(90, 9, 97, 18);
		getContentPane().add(id_txf);

		name_txf = new JTextField();
		name_txf.setBounds(366, 9, 112, 20);
		getContentPane().add(name_txf);

		final JLabel label = new JLabel();
		label.setText("作者");
		label.setBounds(39, 63, 66, 18);
		getContentPane().add(label);

		sure_button = new JButton();
		sure_button.setText("确定");
		sure_button.addActionListener(this);
		sure_button.setBounds(403, 348, 75, 28);
		getContentPane().add(sure_button);
		this.setVisible(true);

		type_txf = new JTextField();
		type_txf.setBounds(366, 61, 112, 22);
		getContentPane().add(type_txf);

		author_txf = new JTextField();
		author_txf.setBounds(90, 61, 99, 22);
		getContentPane().add(author_txf);

		final JLabel press_label = new JLabel();
		press_label.setText("出版社");
		press_label.setBounds(39, 120, 39, 18);
		getContentPane().add(press_label);

		press_txf = new JTextField();
		press_txf.setBounds(90, 118, 97, 22);
		getContentPane().add(press_txf);

		final JLabel label_3 = new JLabel();
		label_3.setText("出版日期");
		label_3.setBounds(263, 120, 66, 18);
		getContentPane().add(label_3);

		date_txf = new JTextField();
		date_txf.setBounds(366, 118, 112, 20);
		getContentPane().add(date_txf);

		final JLabel label_4 = new JLabel();
		label_4.setText("价格");
		label_4.setBounds(39, 177, 66, 18);
		getContentPane().add(label_4);

		price_txf = new JTextField();
		price_txf.setBounds(90, 176, 97, 19);
		getContentPane().add(price_txf);

		final JLabel labelb = new JLabel();
		labelb.setText("备注");
		labelb.setBounds(39, 229, 66, 18);
		getContentPane().add(labelb);

		intro_ta = new JTextArea();
		intro_ta.setBounds(92, 229, 364, 97);
		getContentPane().add(intro_ta);
	}
	public void addBook()
	{
		DataBase au_db=new DataBase();
		ResultSet rs=null;
		if(id_txf.getText().trim()=="")
		{
			JOptionPane.showMessageDialog(null, "书号不能为空!");
			return;
		}
		if(name_txf.getText().trim()=="")
		{
			JOptionPane.showMessageDialog(null, "书名不能为空!");
			return;
		}
		
		if(id_txf.getText().equals(""))
		{
				JOptionPane.showMessageDialog(null,"用户编号不能为空!");
				return;
		}
		String ss="insert into t_books values('"+id_txf.getText().trim()+"','"+name_txf.getText().trim()+"','"+type_txf.getText().trim()+"','"+author_txf.getText().trim()+"','"+press_txf.getText().trim()+"','"+date_txf.getText().trim()+"','"+intro_ta.getText().trim()+"','"+price_txf.getText().trim()+"','否')";
		au_db.updateSql(ss);
		JOptionPane.showMessageDialog(null,"添加书籍成功!");
		this.dispose();	
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(sure_button))
		{
			addBook();	
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
