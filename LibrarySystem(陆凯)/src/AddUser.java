import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.sql.*;



public class AddUser extends JDialog implements WindowListener,ActionListener{

	private JPasswordField psw_txf1;
	private JPasswordField psw_txf2;
	private JTextField name_txf;
	private JTextField id_txf;
	private JButton sure_button ;

	public void windowClosing(WindowEvent e)
	{
		this.dispose();
	}
	public AddUser() {
		super();
	
		setTitle("����û�");
		getContentPane().setLayout(null);
		setBounds(500, 220, 377, 292);
		
		final JLabel label1 = new JLabel();
		label1.setText("�û����");
		label1.setBounds(53, 10, 66, 18);
		getContentPane().add(label1);

		final JLabel label_1 = new JLabel();
		label_1.setText("�û���");
		label_1.setBounds(53, 52, 66, 18);
		getContentPane().add(label_1);

		final JLabel label_2 = new JLabel();
		label_2.setText("����");
		label_2.setBounds(53, 101, 66, 18);
		getContentPane().add(label_2);

		id_txf = new JTextField();
		id_txf.setBounds(170, 8, 112, 20);
		getContentPane().add(id_txf);

		name_txf = new JTextField();
		name_txf.setBounds(170, 50, 112, 20);
		getContentPane().add(name_txf);

		final JLabel label = new JLabel();
		label.setText("ȷ������");
		label.setBounds(53, 152, 66, 18);
		getContentPane().add(label);

		sure_button = new JButton();
		sure_button.setText("ȷ��");
		sure_button.addActionListener(this);
		sure_button.setBounds(207, 203, 75, 28);
		getContentPane().add(sure_button);

		psw_txf2 = new JPasswordField();
		psw_txf2.setBounds(170, 150, 112, 22);
		getContentPane().add(psw_txf2);

		psw_txf1 = new JPasswordField();
		psw_txf1.setBounds(170, 99, 112, 22);
		getContentPane().add(psw_txf1);	
		this.setVisible(true);
	}
	public void addUser()
	{
		DataBase au_db=new DataBase();
		ResultSet rs=null;
		String s1=psw_txf1.getText().trim();
		String s2=psw_txf2.getText().trim();
		
		if(id_txf.getText().equals(""))
		{
			
				JOptionPane.showMessageDialog(null,"�û���Ų���Ϊ��!");
				return;
			
		}
		if(name_txf.getText().equals(""))
		{
			
				JOptionPane.showMessageDialog(null,"�û�������Ϊ��!");
				return;
			
		}
		if(s1.equals("")||s2.equals(""))
		{
			JOptionPane.showMessageDialog(null,"���벻��Ϊ��!");
			return;
		}
		
		if(id_txf.getText().toString().trim().length()>20)
		{
			JOptionPane.showMessageDialog(null,"����ı�Ź���!");
			return;
		}
		if(name_txf.getText().toString().trim().length()>20)
		{
			JOptionPane.showMessageDialog(null,"������û�������!");
			return;
		}
		if(psw_txf1.getText().trim().length()>20)
		{
			JOptionPane.showMessageDialog(null,"������������!");
			return;
		}
		if(s1.equals(s2))
		{
		String addUserStrSQL="insert into t_users (user_id,user_name,user_pwd,user_bookMount) values ('"+id_txf.getText().trim()+"','"+name_txf.getText().trim()+"'," +
				"'"+s1+"',0)";
		au_db.updateSql(addUserStrSQL);
		JOptionPane.showMessageDialog(null,"����û��ɹ�!");
		this.dispose();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "�������벻ƥ��,����������!");
			return;
		}
		
		
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(sure_button))
		{
			addUser();
			
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
