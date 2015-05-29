import java.awt.*;
import javax.swing.*;
import javax.swing.UIManager;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class LoginFrame extends JFrame implements ActionListener,ItemListener{
	private ButtonGroup bgroup = new ButtonGroup();
	private JPasswordField pwd_txf;
	private JTextField user_txf;
	JLabel img_label;
	Container c;
	ResultSet rs;
	DataBase db;
	String select_user_SQL = "select user_name,user_pwd from t_users";
	String select_mg_SQL = "select mg_name,mg_pwd from t_managers";
	String SQLString;
	JCheckBox user_cb,mg_cb;
	String Login_type;//���ݸ�������
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().trim().equals("�˳�"))
			System.exit(0);
		if (e.getActionCommand().trim().equals("��¼")) {
			String username = user_txf.getText().trim();
			String password = pwd_txf.getText().trim();
			System.out.println("�����ǣ�"+password);
			if (user_cb.isSelected())//���ѡ�������ͨ�û�
			{
				SQLString = "select user_name,user_pwd from t_users where user_name='"
						+ username + "' and user_pwd='" + password + "'";
				Login_type="��ͨ�û�";
			}
			if(mg_cb.isSelected())//ѡ����ǹ���Ա
			
				{
				SQLString = "select mg_name,mg_pwd from t_managers where mg_name='"
										+ username + "' and mg_pwd='" + password + "'";
				Login_type="����Ա";
				}
			db = new DataBase();
			rs = db.getResult(SQLString);
			try {
				if (!rs.next()) {
					db.closeConnection();
					JOptionPane.showMessageDialog(null, "�û��������벻��ȷ");	
				} else {
					setVisible(false);
					new MainFrame(Login_type,username);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}
	}
	
	public LoginFrame() {
		super();
		setBackground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		getContentPane().setLayout(null);
		Container cp = this.getContentPane();
		setResizable(false);
		this.setTitle("��¼����");
		c = getContentPane();
		setBounds(400, 150, 337, 281);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel top_panel = new JPanel();
		top_panel.setBackground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		top_panel.setBounds(0, 0, 333, 246);
		top_panel.setAutoscrolls(true);
		top_panel.setLayout(null);
		top_panel.setOpaque(false);
		getContentPane().add(top_panel);

		final JPanel text_panel = new JPanel();
		text_panel.setBackground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		text_panel.setLayout(null);
		text_panel.setBounds(0, 0, 333, 246);
		text_panel.setOpaque(false);
		top_panel.add(text_panel);

		final JLabel user_label = new JLabel();
		user_label.setBounds(53, 30, 51, 18);
		text_panel.add(user_label);
		user_label.setText("�û���");

		final JLabel pwd_label = new JLabel();
		pwd_label.setBounds(53, 82, 66, 18);
		text_panel.add(pwd_label);
		pwd_label.setText("����");

		pwd_txf = new JPasswordField();
		pwd_txf.setBounds(136, 78, 108, 26);
		text_panel.add(pwd_txf);

		user_txf = new JTextField();
		user_txf.setBounds(136, 26, 108, 26);
		text_panel.add(user_txf);

		final JButton exit_button = new JButton();
		exit_button.setText("�˳�");
		exit_button.setBounds(53, 188, 60, 26);
		exit_button.addActionListener(this);
		text_panel.add(exit_button);

		final JButton login_button = new JButton();
		login_button.setText("��¼");
		login_button.setBounds(184, 188, 60, 26);
		login_button.addActionListener(this);
		text_panel.add(login_button);

		mg_cb= new JCheckBox("����Ա",false);
		
		mg_cb.setBounds(167, 135, 66, 24);
		bgroup.add(mg_cb);
		text_panel.add(mg_cb);

		 user_cb = new JCheckBox("��ͨ�û�",true);
		bgroup.add(user_cb);
		user_cb.setBounds(53, 136, 77, 22);
		text_panel.add(user_cb);
		setVisible(true);

	}

	public void itemStateChanged(ItemEvent e)
	{
		
	}

}
