import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import java.awt.event.*;
public class WelcomeFrame extends JFrame implements ActionListener{
	JLabel img_label;
	Container c;
	DataBase db;
	JButton s_button;
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeFrame frame = new WelcomeFrame();
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
public void actionPerformed(ActionEvent e)
{
	if(e.getActionCommand().trim().equals("�˳�"))
		System.exit(0);
	if(e.getSource().equals(s_button))
	{
		setVisible(false);
		new MainFrame("�ο�","");
	}
		if(e.getActionCommand().trim().equals("�û���¼"))
		{
			//setVisible(false);
			this.dispose();
			new LoginFrame();
		}
			if(e.getActionCommand().trim().equals("������Ϣ"))
			{
				JOptionPane.showMessageDialog(null,"�༶:1311101��\n������½��\nѧ��:120350124");
				
			}
				
		
}
	public WelcomeFrame() {
		super();
		getContentPane().setLayout(null);
		Container cp = this.getContentPane();
		setResizable(false);
		this.setTitle("��ӭ����");
		c = getContentPane();

		setBounds(400, 150, 394, 322);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel button_panel = new JPanel();
		button_panel.setBackground(Color.LIGHT_GRAY);
		button_panel.setBounds(278, 47, 110, 238);
		button_panel.setLayout(null);
		getContentPane().add(button_panel);

		s_button = new JButton();
		s_button.setActionCommand("");
		s_button.setText("��ѯ");
		s_button.setBounds(10, 0, 99, 28);
		s_button.addActionListener(this);
		button_panel.add(s_button);

		final JButton user_button = new JButton();
		user_button.setText("�û���¼");
		user_button.setBounds(10, 62, 99, 28);
		user_button.addActionListener(this);
		button_panel.add(user_button);

		final JButton about_button = new JButton();
		about_button.setText("������Ϣ");
		about_button.setBounds(10, 129, 99, 28);
		about_button.addActionListener(this);
		button_panel.add(about_button);

		final JButton exit_button = new JButton();
		exit_button.setText("�˳�");
		exit_button.setBounds(10, 185, 99, 28);
		exit_button.addActionListener(this);
		button_panel.add(exit_button);

		final JPanel img_panel = new JPanel();
		img_panel.setLayout(null);
		img_panel.setBounds(0, 47, 281, 238);
		getContentPane().add(img_panel);

		final JLabel label = new JLabel(new ImageIcon("images/ͼ���.png"));
		label.setBounds(0, 0, 281, 238);
		img_panel.add(label);

		final JPanel title_panel = new JPanel();
		title_panel.setBackground(Color.LIGHT_GRAY);
		title_panel.setLayout(null);
		title_panel.setToolTipText("");
		title_panel.setFont(new Font("", Font.PLAIN, 22));
		title_panel.setForeground(new Color(255, 0, 0));
		title_panel.setBounds(0, 0, 388, 47);
		getContentPane().add(title_panel);

		final JLabel label_1 = new JLabel();
		label_1.setForeground(new Color(255, 0, 0));
		label_1.setFont(new Font("", Font.ITALIC, 36));
		label_1.setText("ͼ�����ϵͳ");
		label_1.setBounds(79, 0, 252, 47);
		title_panel.add(label_1);



	}

}
