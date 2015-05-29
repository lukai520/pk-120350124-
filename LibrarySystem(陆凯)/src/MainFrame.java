import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import com.swtdesigner.SwingResourceManager;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class MainFrame extends JFrame implements ActionListener {

	// ��������
	BackgroundMusic bm;
	// �����˵�
	JMenuItem mi1 = new JMenuItem("ˢ��");
	JMenuItem mi2 = new JMenuItem("��������");
	JMenuItem mi3 = new JMenuItem("ֹͣ����");
	JMenuItem mi4 = new JMenuItem("����");
	JMenuItem mi5 = new JMenuItem("ע��");
	JMenuItem mi6 = new JMenuItem("��¼");
	JMenuItem mi7 = new JMenuItem("�˳�");

	private JTable table;
	private JTextField user_record_id_txf;
	private JComboBox bl_com;
	private JTextField lb_bookid_txf;// ���
	private JTextField lb_userid_txf;// �û���
	private JButton bl_sure_button;// ȷ��
	private JComboBox press_com;
	private JComboBox type_com;
	private JComboBox lend_com;
	private JMenuItem jmi1;
	// �û���Ϣ
	private JButton userbookbutton;
	private JButton info_refresh_button;// ˢ��
	private JTextArea info_ta;
	private JTextField addr_txf;
	private JTextField mail_txf;
	private JTextField tel_txf;
	private JTextField birth_txf;
	private JTextField sex_txf;
	private JTextField name_txf;
	private JTextField id_txf;
	private int flag = 1;
	private int rm = 0;// ��¼����
	private JButton modi_button;
	// ͼ��һ���ϵĲ�ѯ��ť
	final JButton bs_button;
	private JMenuItem logout_jmi, exit_jmi;
	private JTable book_search_table = null;
	DefaultTableModel defaultModel = null;
	DefaultTableModel defaultModels1 = null;
	DefaultTableModel defaultModels2 = null;

	private JTable stable1 = null;
	private JTable stable2 = null;
	// �鼮����
	private JTable book_table;
	DefaultTableModel defaultModelbook = null;
	private JButton add_book_button;
	private JButton del_book_button;
	private JButton book_refresh_button;
	// ���ļ�¼
	private JTable record_table = null;
	DefaultTableModel defaultModelr1 = null;
	private JButton record_search_button = null;
	// �û�����
	private JButton user_refresh_button1;
	private JTable user_table = null;
	DefaultTableModel defaultModeluser = null;
	private JButton add_user_button, del_user_button;

	// ͼ��һ�����鼮�����ַ���
	String bookSearchString_SQL = "select book_id,book_name,book_type,book_author,book_press,book_date,book_price "
			+ "from t_books";
	String user = null;
	String login_type = null;

	public void borrowBook() {
		DataBase borrow_db = new DataBase();
		ResultSet rs = null;
		String dateString = null;
		DateFormat df = new DateFormat();
		dateString = df.dformat();
		if (lb_userid_txf.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "�������û����!");
			return;
		} else if (lb_bookid_txf.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "������ͼ����!");
			return;
		}
		rs = borrow_db.getResult("select* from t_users where user_id='"
				+ lb_userid_txf.getText().trim() + "'");
		try {
			if (rs.next() == false) {
				JOptionPane.showMessageDialog(null, "���û�������!");
				return;
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs = borrow_db.getResult("select* from remain_view where book_id='"
				+ lb_bookid_txf.getText().trim() + "'");
		try {
			if (rs.next() == false) {
				JOptionPane.showMessageDialog(null, "�����ڸ�ͼ����ͼ���ѽ��!");
				return;
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String update_user_strSQL, update_book_strSQL, update_borrow_strSQL, update_lend_strSQL;
		// �������Ѿ���ļ�¼
		borrow_db.updateSql("insert into t_borrow values('"
				+ lb_userid_txf.getText().trim() + "','"
				+ lb_bookid_txf.getText().trim() + "','" + dateString + "')");
		// ���¸��˽�����Ŀ����1
		borrow_db
				.updateSql("update t_users set user_bookMount=user_bookMount+1 where user_id='"
						+ lb_userid_txf.getText().trim() + "'");
		// t_record����
		rm++;
		borrow_db.updateSql("insert into t_record values (" + rm + ",'"
				+ lb_userid_txf.getText().trim() + "','"
				+ lb_bookid_txf.getText().trim() + "','����','" + dateString
				+ "')");
		// ������Ŀ����Ϊ���
		borrow_db.updateSql("update t_books set bl_state='��' where book_id='"
				+ lb_bookid_txf.getText().trim() + "'");

		JOptionPane.showMessageDialog(null, "����ɹ�!");
		// ˢ�¿���鼮
		lendManagerTableInit();
	}

	public void returnBook() {
		DataBase borrow_db = new DataBase();
		ResultSet rs = null;
		String dateString = null;
		DateFormat df = new DateFormat();
		dateString = df.dformat();
		if (lb_userid_txf.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "�������û����!");
			return;
		} else if (lb_bookid_txf.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "������ͼ����!");
			return;
		}
		rs = borrow_db.getResult("select* from t_users where user_id='"
				+ lb_userid_txf.getText().trim() + "'");
		try {
			if (rs.next() == false) {
				JOptionPane.showMessageDialog(null, "���û�������!");
				return;
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs = borrow_db.getResult("select* from t_borrow where user_id='"
				+ lb_userid_txf.getText().trim() + "'");
		try {
			if (rs.next() == false) {
				JOptionPane.showMessageDialog(null, "���û�û�н��Ȿ��!");
				return;
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String update_user_strSQL, update_book_strSQL, update_borrow_strSQL, update_lend_strSQL;
		borrow_db.updateSql("delete from t_borrow where user_id='"
				+ lb_userid_txf.getText().trim() + "'and book_id='"+lb_bookid_txf.getText().trim()+"'");

		borrow_db.updateSql("update t_books set bl_state='��' where book_id='"
				+ lb_bookid_txf.getText().trim() + "'");

		borrow_db.updateSql("update t_users set user_bookMount=user_bookMount-1 where user_id='"
						+ lb_userid_txf.getText().trim() + "'");
		rm++;
		borrow_db.updateSql("insert into t_record values (" + rm + ",'"
				+ lb_userid_txf.getText().trim() + "','"
				+ lb_bookid_txf.getText().trim() + "','����','" + dateString
				+ "')");
		// ������Ŀ����Ϊ���
		JOptionPane.showMessageDialog(null, "����ɹ�!");
		rm++;
		// ˢ�¿���鼮
		lendManagerTableInit();
	}

	public void userSearchRecord() {
		DataBase rc_db = new DataBase();
		ResultSet rs = null;
		String userRecordSQL = null;
		if (login_type.equals("��ͨ�û�"))
			userRecordSQL = "select user_id,user_name,book_id,book_name,record_type,record_date from record_view1 where user_name='"
					+ user + "'";
		else if (login_type.equals("����Ա")) {
			if (user_record_id_txf.getText().trim().equals(""))
				userRecordSQL = "select user_id,user_name,book_id,book_name,record_type,record_date from record_view1";
			else {
				rs = rc_db
						.getResult("select *from record_view1 where user_id='"
								+ user_record_id_txf.getText().trim() + "'");
				try {
					if (rs.next() == false) {
						JOptionPane.showMessageDialog(null, "û�и��û��ļ�¼!");
						return;
					}
				} catch (HeadlessException e) {

					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				userRecordSQL = "select user_id,user_name,book_id,book_name,record_type,record_date from record_view1 where user_id='"
						+ user_record_id_txf.getText().trim() + "'";
			}
		}
		rs = rc_db.getResult(userRecordSQL);
		try {
			// ����Ҫɾ��table�е������ȣ�
			int rowCount = defaultModelr1.getRowCount() - 1;// ȡ��table�е������У�
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModelr1.removeRow(j);// ɾ��rowCount�е����ݣ�
				defaultModelr1.setRowCount(j);// ��������������
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 6; s++)
					data.addElement(rs.getString(s));

				defaultModelr1.addRow(data);
			}
			// lm_db.closeConnection();// �ر�����Դ
			record_table.revalidate();
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	public void bookManage() {
		DataBase bm_db = new DataBase();
		ResultSet rs = null;
		String book_str_SQL = null;

		book_str_SQL = "select *from t_books";
		rs = bm_db.getResult(book_str_SQL);
		try {
			// ����Ҫɾ��table�е������ȣ�
			int rowCount = defaultModelbook.getRowCount() - 1;// ȡ��table�е������У�
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModelbook.removeRow(j);// ɾ��rowCount�е����ݣ�
				defaultModelbook.setRowCount(j);// ��������������
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 9; s++)
					data.addElement(rs.getString(s));

				defaultModelbook.addRow(data);
			}
			// lm_db.closeConnection();// �ر�����Դ
			book_table.revalidate();
			System.out.println("�鼮����");
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	public MainFrame(String login_type, String user) {
		// System.currentTimeMillis();
		// super();
		bm = new BackgroundMusic();
		this.login_type = login_type;
		this.user = user;
		this.setTitle(login_type + ":" + user);
		getContentPane().setLayout(new BorderLayout());
		setBounds(200, 150, 861, 545);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ------------------------------------------------------------------
		// �˵�����
		final JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout(new BorderLayout());

		// ϵͳ�˵�
		JMenu system_menu = new JMenu("ϵͳ");

		logout_jmi = new JMenuItem();
		logout_jmi.setText("ע��");
		logout_jmi.addActionListener(this);
		system_menu.add(logout_jmi);

		exit_jmi = new JMenuItem("�˳�");
		exit_jmi.addActionListener(this);
		system_menu.add(exit_jmi);
		menuBar.add(system_menu);
		setJMenuBar(menuBar);
		// tabpane�����
		final JPanel main_pane = new JPanel();
		main_pane.setLayout(null);
		main_pane.setAutoscrolls(false);
		JPanel user_info_pane = new JPanel();
		user_info_pane.setAutoscrolls(true);
		user_info_pane.setLayout(null);
		final JTabbedPane tp = new JTabbedPane();
		// �����

		tp.add(main_pane, "��ҳ");

		final JPopupMenu mainpppMenu = new JPopupMenu();

		final JPanel pane2 = new JPanel();
		// ����Ϊ͸��
		pane2.setOpaque(false);
		pane2.setBounds(0, 0, 848, 452);
		main_pane.add(pane2);
		addPopup(pane2, mainpppMenu);
		mainpppMenu.add(mi1);
		mainpppMenu.add(mi2);
		mainpppMenu.add(mi3);
		mainpppMenu.add(mi4);
		mainpppMenu.add(mi5);
		mainpppMenu.add(mi6);
		mainpppMenu.add(mi7);
		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);
		mi4.addActionListener(this);
		mi5.addActionListener(this);
		mi6.addActionListener(this);
		mi7.addActionListener(this);

		Icon ico1 = new ImageIcon("images/3.png");
		// ������˵�
		final JLabel img_label = new JLabel(ico1);
		img_label.setBounds(0, 0, 850, 454);
		img_label.setAutoscrolls(true);
		img_label.setBackground(Color.BLUE);
		img_label.setText("");
		main_pane.add(img_label);
		tp.add(user_info_pane, "�û���Ϣ");

		final JLabel id_label = new JLabel();
		id_label.setText("���");
		id_label.setBounds(35, 10, 66, 18);
		user_info_pane.add(id_label);

		final JLabel name_label = new JLabel();
		name_label.setText("����");
		name_label.setBounds(289, 10, 66, 18);
		user_info_pane.add(name_label);

		final JLabel sex_label = new JLabel();
		sex_label.setText("�Ա�");
		sex_label.setBounds(35, 48, 52, 18);
		user_info_pane.add(sex_label);

		final JLabel birth_label = new JLabel();
		birth_label.setText("��������");
		birth_label.setBounds(289, 48, 66, 18);
		user_info_pane.add(birth_label);

		final JLabel tel_label = new JLabel();
		tel_label.setText("��ϵ�绰");
		tel_label.setBounds(35, 91, 52, 18);
		user_info_pane.add(tel_label);

		final JLabel mail_label = new JLabel();
		mail_label.setText("��������");
		mail_label.setBounds(289, 91, 66, 18);
		user_info_pane.add(mail_label);

		final JLabel addr_label = new JLabel();
		addr_label.setText("��ַ");
		addr_label.setBounds(35, 135, 66, 18);
		user_info_pane.add(addr_label);

		final JLabel info_label = new JLabel();
		info_label.setText("��ע");
		info_label.setBounds(35, 175, 66, 18);
		user_info_pane.add(info_label);

		id_txf = new JTextField();
		id_txf.setEditable(false);
		id_txf.setBounds(107, 8, 122, 22);
		user_info_pane.add(id_txf);

		name_txf = new JTextField();
		name_txf.setEditable(false);
		name_txf.setBounds(378, 8, 150, 22);
		user_info_pane.add(name_txf);

		sex_txf = new JTextField();
		sex_txf.setBounds(107, 46, 122, 22);
		user_info_pane.add(sex_txf);

		birth_txf = new JTextField();
		birth_txf.setBounds(378, 46, 150, 22);
		user_info_pane.add(birth_txf);

		tel_txf = new JTextField();
		tel_txf.setBounds(107, 89, 122, 22);
		user_info_pane.add(tel_txf);

		mail_txf = new JTextField();
		mail_txf.setBounds(378, 89, 150, 22);
		user_info_pane.add(mail_txf);

		addr_txf = new JTextField();
		addr_txf.setBounds(107, 133, 421, 22);
		user_info_pane.add(addr_txf);

		info_ta = new JTextArea();
		info_ta.setBounds(106, 175, 422, 70);
		user_info_pane.add(info_ta);

		modi_button = new JButton();
		modi_button.setText("�޸���Ϣ");
		modi_button.addActionListener(this);
		modi_button.setBounds(422, 287, 106, 28);
		user_info_pane.add(modi_button);

		info_refresh_button = new JButton();
		info_refresh_button.setText("ˢ��");
		info_refresh_button.setBounds(243, 290, 106, 28);
		info_refresh_button.addActionListener(this);
		user_info_pane.add(info_refresh_button);

		userbookbutton = new JButton();
		userbookbutton.setText("�鿴�ѽ��鼮");
		userbookbutton.setBounds(60, 287, 112, 28);
		userbookbutton.addActionListener(this);

		String[] name = { "���", "����", "����", "����", "������", "��������", "�۸�" };

		String[][] data = new String[0][0];
		defaultModel = new DefaultTableModel(data, name);
		// tab�����

		// tp.setEnabledAt(1, false);
		tp.setTabPlacement(JTabbedPane.LEFT);
		getContentPane().add(tp);
		JPanel book_search_pane = new JPanel();
		book_search_pane.setAutoscrolls(true);
		book_search_pane.setLayout(null);
		// �鼮һ�����
		tp.add(book_search_pane, "ͼ��һ��");
		book_search_table = new JTable(defaultModel);
		book_search_table.setPreferredScrollableViewportSize(new Dimension(400,
				80));

		final JScrollPane scrollPane = new JScrollPane(book_search_table);

		scrollPane.setBackground(Color.BLACK);
		scrollPane.setAutoscrolls(true);
		scrollPane.setBounds(0, 0, 756, 375);
		book_search_pane.add(scrollPane);

		lend_com = new JComboBox();
		lend_com.setModel(new DefaultComboBoxModel(new String[] { "����", "��",
				"��" }));
		lend_com.setBounds(502, 404, 60, 27);
		book_search_pane.add(lend_com);

		type_com = new JComboBox();
		type_com.setModel(new DefaultComboBoxModel(new String[] { "����" }));
		type_com.setBounds(252, 404, 103, 27);
		book_search_pane.add(type_com);

		final JLabel label = new JLabel();
		label.setText("����");
		label.setBounds(197, 408, 34, 18);
		book_search_pane.add(label);

		final JLabel label_1 = new JLabel();
		label_1.setText("�Ƿ��ѽ��");
		label_1.setBounds(403, 408, 71, 18);
		book_search_pane.add(label_1);

		final JLabel label_4 = new JLabel();
		label_4.setText("������");
		label_4.setBounds(0, 408, 39, 18);
		book_search_pane.add(label_4);

		press_com = new JComboBox();
		press_com.setName("");
		press_com.setMaximumRowCount(10);
		press_com.setModel(new DefaultComboBoxModel(new String[] { "����" }));
		press_com.setBounds(45, 403, 116, 27);
		book_search_pane.add(press_com);

		bs_button = new JButton();
		bs_button.setText("��ѯ");
		bs_button.setBounds(650, 403, 106, 28);
		bs_button.addActionListener(this);
		book_search_pane.add(bs_button);

		// �����鼮��壬����ͻ���
		JPanel bl_pane = new JPanel();
		bl_pane.setAutoscrolls(true);
		bl_pane.setLayout(null);
		tp.add(bl_pane, "���Ĺ���");

		final JLabel label_2 = new JLabel();
		label_2.setText("�û����");
		label_2.setBounds(142, 423, 66, 19);
		bl_pane.add(label_2);

		final JLabel label_3 = new JLabel();
		label_3.setText("���");
		label_3.setBounds(401, 423, 26, 18);
		bl_pane.add(label_3);

		lb_userid_txf = new JTextField();
		lb_userid_txf.setBounds(227, 420, 105, 24);
		bl_pane.add(lb_userid_txf);

		lb_bookid_txf = new JTextField();
		lb_bookid_txf.setBounds(449, 421, 92, 23);
		bl_pane.add(lb_bookid_txf);

		bl_com = new JComboBox();
		bl_com.setModel(new DefaultComboBoxModel(new String[] { "����", "����" }));
		bl_com.setBounds(25, 420, 66, 24);
		bl_pane.add(bl_com);

		bl_sure_button = new JButton();
		bl_sure_button.setText("ȷ��");
		bl_sure_button.addActionListener(this);
		bl_sure_button.setBounds(594, 418, 77, 28);
		bl_pane.add(bl_sure_button);
		// ����ʱ��ʾ�����û����鼮
		String[] name1 = { "���", "�û���", "��������" };
		String[][] data1 = new String[0][0];
		defaultModels1 = new DefaultTableModel(data1, name1);
		stable1 = new JTable(defaultModels1);
		stable1.setPreferredScrollableViewportSize(new Dimension(400, 80));

		String[] name2 = { "���", "����" };
		String[][] data2 = new String[0][0];
		defaultModels2 = new DefaultTableModel(data2, name2);
		stable2 = new JTable(defaultModels2);
		stable2.setPreferredScrollableViewportSize(new Dimension(23, 34));

		final JScrollPane SPanel1 = new JScrollPane(stable1);
		SPanel1.setAutoscrolls(true);
		SPanel1.setBounds(0, 30, 383, 356);
		bl_pane.add(SPanel1);
		// �����˵�
		final JPopupMenu pppMenu1 = new JPopupMenu();
		pppMenu1.setLabel("");
		pppMenu1.setForeground(Color.LIGHT_GRAY);
		pppMenu1.setFont(new Font("", Font.BOLD, 15));
		jmi1 = new JMenuItem("�鿴");
		jmi1.addActionListener(this);
		pppMenu1.add(jmi1);
		addPopup(SPanel1, pppMenu1);

		final JScrollPane SPanel2 = new JScrollPane(stable2);
		SPanel2.setAutoscrolls(true);
		SPanel2.setBounds(389, 30, 369, 356);
		bl_pane.add(SPanel2);

		final JLabel label_5 = new JLabel();
		label_5.setText("�����û���");
		label_5.setBounds(0, -2, 92, 24);
		bl_pane.add(label_5);

		final JLabel label_6 = new JLabel();
		label_6.setText("����鼮��");
		label_6.setBounds(389, 6, 66, 18);
		bl_pane.add(label_6);

		final JLabel label_9 = new JLabel();
		label_9.setText("ѡ��һ���û����Ҽ��ɲ鿴���ĵ��鼮��");
		label_9.setBounds(0, 396, 272, 21);
		bl_pane.add(label_9);

		// �û�������壬�����û���Ӻ��û�ɾ��
		JPanel user_manage_pane = new JPanel();
		user_manage_pane.setAutoscrolls(true);
		user_manage_pane.setLayout(null);
		tp.add(user_manage_pane, "�û�����");

		String[] user_info_str = { "�û����", "�û���", "�Ա�", "��������", "�绰", "����",
				"�༶", "���", "������Ŀ" };
		String[][] user_data = new String[0][0];
		defaultModeluser = new DefaultTableModel(user_data, user_info_str);
		user_table = new JTable(defaultModeluser);
		user_table.setPreferredScrollableViewportSize(new Dimension(23, 34));
		// user_table= new JTable(defaultModeluser);

		final JScrollPane user_spane = new JScrollPane(user_table);
		user_spane.setBounds(0, 0, 766, 358);
		user_manage_pane.add(user_spane);

		add_user_button = new JButton();
		add_user_button.setText("����û�");
		add_user_button.setBounds(76, 382, 106, 28);
		add_user_button.addActionListener(this);
		user_manage_pane.add(add_user_button);

		del_user_button = new JButton();
		del_user_button.setText("ɾ���û�");
		del_user_button.setBounds(243, 382, 106, 28);
		del_user_button.addActionListener(this);
		user_manage_pane.add(del_user_button);

		user_refresh_button1 = new JButton();
		user_refresh_button1.setText("ˢ��");
		user_refresh_button1.addActionListener(this);
		user_refresh_button1.setBounds(407, 382, 106, 28);
		user_manage_pane.add(user_refresh_button1);

		// ���ļ�¼,����Ա���Բ鿴���м�¼���û����Բ鿴�Լ��ļ�¼
		JPanel bl_record_pane = new JPanel();
		bl_record_pane.setAutoscrolls(true);
		bl_record_pane.setLayout(null);
		tp.add(bl_record_pane, "���ļ�¼");

		String[] record_head_str = { "�û����", "�û���", "���", "����", "����", "ʱ��" };
		String[][] record_data = new String[0][0];
		defaultModelr1 = new DefaultTableModel(record_data, record_head_str);
		record_table = new JTable(defaultModelr1);
		record_table.setPreferredScrollableViewportSize(new Dimension(23, 34));

		final JScrollPane record_sPane = new JScrollPane(record_table);
		record_sPane.setAutoscrolls(true);
		record_sPane.setBounds(0, 0, 763, 337);
		bl_record_pane.add(record_sPane);

		final JPanel record_sc_panel = new JPanel();
		record_sc_panel.setLayout(null);
		record_sc_panel.setBounds(10, 335, 736, 74);

		final JLabel label_7 = new JLabel();
		label_7.setBounds(288, 39, 45, 26);
		record_sc_panel.add(label_7);
		label_7.setText("�û���");

		user_record_id_txf = new JTextField();
		user_record_id_txf.setBounds(353, 40, 106, 24);
		record_sc_panel.add(user_record_id_txf);

		record_search_button = new JButton();
		record_search_button.setBounds(545, 39, 89, 27);
		record_sc_panel.add(record_search_button);
		record_search_button.addActionListener(this);
		record_search_button.setText("ȷ��");
		// �鼮��������Ա�������Ӻ�ɾ���鼮

		JPanel book_manage_pane = new JPanel();
		book_manage_pane.setAutoscrolls(true);
		book_manage_pane.setLayout(null);
		tp.add(book_manage_pane, "�鼮����");

		String[] book_str = { "���", "����", "����", "����", "������", "��������", "�۸�",
				"��ע", "���" };
		String[][] book_data = new String[0][0];
		defaultModelbook = new DefaultTableModel(book_data, book_str);
		book_table = new JTable(defaultModelbook);
		book_table.setPreferredScrollableViewportSize(new Dimension(23, 34));
		// user_table= new JTable(defaultModeluser);

		final JScrollPane book_spane = new JScrollPane(book_table);
		book_spane.setAutoscrolls(true);
		book_spane.setBounds(0, 0, 766, 392);
		book_manage_pane.add(book_spane);

		add_book_button = new JButton();
		add_book_button.setText("����鼮");
		add_book_button.setBounds(117, 405, 106, 28);
		add_book_button.addActionListener(this);
		book_manage_pane.add(add_book_button);

		del_book_button = new JButton();
		del_book_button.setText("ɾ���鼮");
		del_book_button.addActionListener(this);
		del_book_button.setBounds(273, 405, 106, 28);
		book_manage_pane.add(del_book_button);

		book_refresh_button = new JButton();
		book_refresh_button.addActionListener(this);
		book_refresh_button.setText("ˢ��");
		book_refresh_button.setBounds(421, 405, 106, 28);
		book_manage_pane.add(book_refresh_button);

		// Ȩ�޹���
		if (login_type.equals("�ο�")) {
			tp.setEnabledAt(1, false);
			tp.setEnabledAt(3, false);
			tp.setEnabledAt(4, false);
			tp.setEnabledAt(5, false);
			tp.setEnabledAt(6, false);
		} else if (login_type.equals("��ͨ�û�")) {
			user_info_pane.add(userbookbutton);
			tp.setEnabledAt(3, false);
			tp.setEnabledAt(4, false);
			tp.setEnabledAt(6, false);
		} else
			bl_record_pane.add(record_sc_panel);
		final JLabel label_8 = new JLabel();
		label_8.setBounds(9, 0, 246, 26);
		record_sc_panel.add(label_8);
		label_8.setForeground(new Color(255, 0, 0));
		label_8.setFont(new Font("", Font.ITALIC, 15));
		label_8
				.setIcon(SwingResourceManager
						.getIcon(MainFrame.class, "g4.ico"));
		label_8.setText("��ѯָ���û��Ľ��ļ�¼��");
		bookSearch();
		bookManage();
		lendManagerTableInit();
		CountRecord cr = new CountRecord();
		rm = cr.borrowedMount();
		System.out.println("��¼��:" + rm);
		// ---------------------------------------------------
		// JOptionPane.showMessageDialog(null, login_type);
		this.setVisible(true);
		allUserInfo();
		userSearchRecord();
		this.userInfo();
		setResizable(true);
	}

	// ��combox���item�ĺ���
	public Object makeObj(final String item) {
		return new Object() {
			public String toString() {
				return item;
			}
		};
	}

	public void allUserInfo() {
		DataBase alluser_db = new DataBase();
		ResultSet rs = null;
		String userInfoSQL = "select user_id,user_name,user_sex,user_birth,user_tel,user_mail,user_addr,user_intro,user_bookMount from t_users";
		rs = alluser_db.getResult(userInfoSQL);
		try {
			// ����Ҫɾ��table�е������ȣ�
			int rowCount = defaultModeluser.getRowCount() - 1;// ȡ��table�е������У�
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModeluser.removeRow(j);// ɾ��rowCount�е����ݣ�
				defaultModeluser.setRowCount(j);// ��������������
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 9; s++)
					data.addElement(rs.getString(s));

				defaultModeluser.addRow(data);
			}
			user_table.revalidate();
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

	}

	// ��ʼ����
	public void lendManagerTableInit() {
		DataBase lm_db = new DataBase();
		ResultSet rs = null;
		String userStringSQL = "select user_id,user_name,user_bookMount from t_users";
		String remainBookStringSQL = "select book_id,book_name from remain_view";
		rs = lm_db.getResult(userStringSQL);
		try {
			// ����Ҫɾ��table�е������ȣ�
			int rowCount = defaultModels1.getRowCount() - 1;// ȡ��table�е������У�
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModels1.removeRow(j);// ɾ��rowCount�е����ݣ�
				defaultModels1.setRowCount(j);// ��������������
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 3; s++)
					data.addElement(rs.getString(s));

				defaultModels1.addRow(data);
			}
			// lm_db.closeConnection();// �ر�����Դ
			stable1.revalidate();
			System.out.println("��ʼ����1���!");
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// -------------------------
		rs = lm_db.getResult(remainBookStringSQL);
		try {
			// ����Ҫɾ��table�е������ȣ�
			int rowCount = defaultModels2.getRowCount() - 1;// ȡ��table�е������У�
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModels2.removeRow(j);// ɾ��rowCount�е����ݣ�
				defaultModels2.setRowCount(j);// ��������������
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 2; s++)
					data.addElement(rs.getString(s));

				defaultModels2.addRow(data);
			}
			lm_db.closeConnection();// �ر�����Դ
			stable2.revalidate();
			System.out.println("��ʼ����2���!");
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

	}

	// ���¹���Ա��Ϣ
	public void updatemgInfo() {
		DataBase upinfo_db = new DataBase();
		ResultSet rs = null;
		String update_info_mg_stringSQL1 = "update t_managers set mg_sex ='"
				+ sex_txf.getText().trim() + "'" + "where mg_id='"
				+ id_txf.getText().trim() + "'";
		String update_info_mg_stringSQL2 = "update t_managers set mg_birth='"
				+ birth_txf.getText().trim() + "'" + "where mg_id='"
				+ id_txf.getText().trim() + "'";
		String update_info_mg_stringSQL3 = "update t_managers set mg_addr='"
				+ addr_txf.getText().trim() + "'" + "where mg_id='"
				+ id_txf.getText().trim() + "'";
		String update_info_mg_stringSQL4 = "update t_managers set mg_tel='"
				+ tel_txf.getText().trim() + "'" + "where mg_id='"
				+ id_txf.getText().trim() + "'";
		String update_info_mg_stringSQL5 = "update t_managers set mg_mail='"
				+ mail_txf.getText().trim() + "'" + "where mg_id='"
				+ id_txf.getText().trim() + "'";
		String update_info_mg_stringSQL6 = "update t_managers set mg_intro='"
				+ info_ta.getText().trim() + "'" + "where mg_id='"
				+ id_txf.getText().trim() + "'";

		upinfo_db.updateSql(update_info_mg_stringSQL1);
		upinfo_db.updateSql(update_info_mg_stringSQL2);
		upinfo_db.updateSql(update_info_mg_stringSQL3);
		upinfo_db.updateSql(update_info_mg_stringSQL4);
		upinfo_db.updateSql(update_info_mg_stringSQL5);
		upinfo_db.updateSql(update_info_mg_stringSQL6);

		JOptionPane.showMessageDialog(null, "�޸ĳɹ�");
	}

	// �����û���Ϣ
	public void updateUserInfo() {
		DataBase upinfo_db = new DataBase();
		ResultSet rs = null;
		String update_info_user_stringSQL1 = "update t_users set user_sex ='"
				+ sex_txf.getText().trim() + "'" + "where user_id='"
				+ id_txf.getText().trim() + "'";
		String update_info_user_stringSQL2 = "update t_users set user_birth='"
				+ birth_txf.getText().trim() + "'" + "where user_id='"
				+ id_txf.getText().trim() + "'";
		String update_info_user_stringSQL3 = "update t_users set user_addr='"
				+ addr_txf.getText().trim() + "'" + "where user_id='"
				+ id_txf.getText().trim() + "'";
		String update_info_user_stringSQL4 = "update t_users set user_tel='"
				+ tel_txf.getText().trim() + "'" + "where user_id='"
				+ id_txf.getText().trim() + "'";
		String update_info_user_stringSQL5 = "update t_users set user_mail='"
				+ mail_txf.getText().trim() + "'" + "where user_id='"
				+ id_txf.getText().trim() + "'";
		String update_info_user_stringSQL6 = "update t_users set user_intro='"
				+ info_ta.getText().trim() + "'" + "where user_id='"
				+ id_txf.getText().trim() + "'";
		// "set user_birth ="+birth_txf.getText().trim()+""+
		// "set user_tel ="+tel_txf.getText().trim()+"" +
		// ",set user_mail= "+mail_txf.getText().trim()+
		// ",set user_addr= "+addr_txf.getText().trim()+"" +
		// ",set user_intro ="+info_ta.getText().trim()+"";
		upinfo_db.updateSql(update_info_user_stringSQL1);
		upinfo_db.updateSql(update_info_user_stringSQL2);
		upinfo_db.updateSql(update_info_user_stringSQL3);
		upinfo_db.updateSql(update_info_user_stringSQL4);
		upinfo_db.updateSql(update_info_user_stringSQL5);
		upinfo_db.updateSql(update_info_user_stringSQL6);

		JOptionPane.showMessageDialog(null, "�޸ĳɹ�");
	}

	// �û���Ϣ��壬������ʾ��Ϣ�ĺ���
	public void userInfo() {
		DataBase user_db = new DataBase();
		ResultSet user_rs = null;
		String userinfo_StringSQL = null;
		if (this.login_type.equals("��ͨ�û�")) {
			userinfo_StringSQL = "select user_id,user_name,user_sex,user_birth,user_tel,"
					+ "user_mail,user_addr,user_intro from t_users where user_name='"
					+ user + "'";
			user_rs = user_db.getResult(userinfo_StringSQL);
			try {
				while (user_rs.next()) {
					id_txf.setText(user_rs.getString("user_id"));
					name_txf.setText(user_rs.getString("user_name"));
					sex_txf.setText(user_rs.getString("user_sex"));
					birth_txf.setText(user_rs.getString("user_birth"));
					tel_txf.setText(user_rs.getString("user_tel"));
					mail_txf.setText(user_rs.getString("user_mail"));
					addr_txf.setText(user_rs.getString("user_addr"));
					info_ta.setText(user_rs.getString("user_intro"));

				}

				user_db.closeConnection();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		} else if (this.login_type.equals("����Ա")) {
			userinfo_StringSQL = "select mg_id,mg_name,mg_sex,mg_birth,mg_tel,"
					+ "mg_mail,mg_addr,mg_intro from t_managers where mg_name='"
					+ user + "'";
			user_rs = user_db.getResult(userinfo_StringSQL);
			try {
				while (user_rs.next()) {
					id_txf.setText(user_rs.getString("mg_id"));
					name_txf.setText(user_rs.getString("mg_name"));
					sex_txf.setText(user_rs.getString("mg_sex"));
					birth_txf.setText(user_rs.getString("mg_birth"));
					tel_txf.setText(user_rs.getString("mg_tel"));
					mail_txf.setText(user_rs.getString("mg_mail"));
					addr_txf.setText(user_rs.getString("mg_addr"));
					info_ta.setText(user_rs.getString("mg_intro"));

				}

				user_db.closeConnection();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	// �鼮���
	public void bookSearch() {
		ResultSet rs, rs1, rs2 = null;
		DataBase bs_db = new DataBase();
		String press_SQL = "select book_press from press_view";
		String type_SQL = "select book_type from type_view";
		if (flag == 1) {
			// ----------------------������--------------------------------------------
			rs = bs_db.getResult(press_SQL);
			try {
				while (rs.next()) {
					press_com.addItem(makeObj(rs.getString("book_press")));

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// -----------------------�������----------------------------------------
			rs = bs_db.getResult(type_SQL);
			try {
				while (rs.next()) {
					type_com.addItem(makeObj(rs.getString("book_type")));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			flag = -1;
		}
		// ------------------------------------���---------------------------------------

		rs = bs_db.getResult(bookSearchString_SQL);
		try {
			// ����Ҫɾ��table�е������ȣ�
			int rowCount = defaultModel.getRowCount() - 1;// ȡ��table�е������У�
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModel.removeRow(j);// ɾ��rowCount�е����ݣ�
				defaultModel.setRowCount(j);// ��������������
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 7; s++)
					data.addElement(rs.getString(s));

				defaultModel.addRow(data);
			}
			bs_db.closeConnection();// �ر�����Դ
			book_search_table.revalidate();
			System.out.println("��ѯ���!!!");
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

	}

	// ������Ӧ
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(mi1)) {
			lendManagerTableInit();
			// updatemgInfo();
			// bookSearch();
			// updatemgInfo();
			userSearchRecord();

		} else if (e.getSource().equals(mi2)) {
			bm.startMusic();
		} else if (e.getSource().equals(mi3)) {
			bm.stopMusic();
		} else if (e.getSource().equals(mi4)) {

			JOptionPane.showMessageDialog(null,"����:½��\nѧУ:������\n�༶:1311101��\nѧ��:120350124");
		} else if (e.getSource().equals(mi5)) {
			this.dispose();
			WelcomeFrame wf = new WelcomeFrame();
			wf.setVisible(true);
		} else if (e.getSource().equals(mi6)) {
			this.dispose();

			new LoginFrame();

		} else if (e.getSource().equals(mi7)) {
			this.dispose();
			System.exit(0);
		} else if (e.getSource().equals(record_search_button)) {
			// ��ѯ�û���¼
			System.out.println("��ѯ�û������¼");
			userSearchRecord();
		} else if (e.getSource().equals(userbookbutton)) {
			UserId ui = new UserId();

			new UserBorrowedBook(ui.userId(user));
		} else if (e.getSource().equals(jmi1)) {
			// �����˵�
			// �Ȼ�ȡ��ѡ����û�
			int i = stable1.getSelectedRow();
			if (i == -1)
				return;
			String id_str;
			id_str = stable1.getValueAt(i, 0).toString();
			System.out.println(id_str);
			new UserBorrowedBook(id_str);
			System.out.println("�鿴�û������鼮");
		} else if (e.getSource().equals(add_book_button)) {
			// ����鼮
			System.out.println("����鼮");
			new AddBook();
			bookManage();
		} else if (e.getSource().equals(del_book_button)) {
			System.out.println("ɾ���鼮");
			new DeleteBook();
			bookManage();
			// ɾ���鼮
		} else if (e.getSource().equals(book_refresh_button)) {
			System.out.println("ˢ���鼮�������");
			bookManage();
			// ˢ���鼮�������
		} else if (e.getSource().equals(user_refresh_button1))
			allUserInfo();
		else if (e.getSource().equals(del_user_button)) {
			System.out.println("helllllll");
			new DeleteUser();
		} else if (e.getSource().equals(add_user_button)) {
			// ����û��Ի���
			new AddUser();
		} else if (e.getSource().equals(del_user_button)) {
			// ɾ���û��Ի���
		} else if (e.getSource().equals(record_search_button)) {
			userSearchRecord();
		} else if (e.getSource().equals(bl_sure_button)) {
			if (bl_com.getSelectedItem().equals("����")) {
				borrowBook();
				userSearchRecord();
			} else if (bl_com.getSelectedItem().equals("����")) {
				returnBook();
				userSearchRecord();
			}
		} else if (e.getSource().equals(modi_button)) {
			if (login_type.equals("��ͨ�û�"))
				updateUserInfo();
			else if (login_type.equals("����Ա"))
				updatemgInfo();
		} else if (e.getSource().equals(info_refresh_button)) {
			userInfo();
			System.out.println("ˢ��");
		} else if (e.getSource().equals(exit_jmi)) {
			System.exit(0);
		} else if (e.getSource().equals(logout_jmi)) {
			// this.setVisible(false);
			this.dispose();
			WelcomeFrame wf = new WelcomeFrame();
			wf.setVisible(true);
		} else if (e.getSource().equals(bs_button)) {
			String whereString = null;
			// 3����δѡ
			if (press_com.getSelectedItem().toString().equals("����")
					&& type_com.getSelectedItem().toString().equals("����")
					&& lend_com.getSelectedItem().toString().equals("����"))
				whereString = "";
			// 1
			else if (type_com.getSelectedItem().toString().equals("����")
					&& lend_com.getSelectedItem().toString().equals("����"))
				whereString = " where book_press='"
						+ press_com.getSelectedItem().toString() + "'";
			// 2
			else if (press_com.getSelectedItem().toString().equals("����")
					&& lend_com.getSelectedItem().toString().equals("����"))
				whereString = " where book_type='"
						+ type_com.getSelectedItem().toString() + "'";
			// 3
			else if (press_com.getSelectedItem().toString().equals("����")
					&& type_com.getSelectedItem().toString().equals("����"))
				whereString = " where bl_state='"
						+ lend_com.getSelectedItem().toString() + "'";
			// 1��2
			else if (lend_com.getSelectedItem().toString().equals("����"))
				whereString = " where book_press='"
						+ press_com.getSelectedItem().toString()
						+ "' and  book_type='"
						+ type_com.getSelectedItem().toString() + "'";
			// 2��3
			else if (press_com.getSelectedItem().toString().equals("����"))
				whereString = " where bl_state='"
						+ lend_com.getSelectedItem().toString()
						+ "' and  book_type='"
						+ type_com.getSelectedItem().toString() + "'";
			// 1��3
			else if (press_com.getSelectedItem().toString().equals("����"))
				whereString = " where book_press='"
						+ press_com.getSelectedItem().toString()
						+ "' and  book_lend='"
						+ lend_com.getSelectedItem().toString() + "'";
			// ȫ��
			else
				whereString = " where book_press='"
						+ press_com.getSelectedItem().toString()
						+ "' and bl_state='"
						+ lend_com.getSelectedItem().toString()
						+ "' and  book_type='"
						+ type_com.getSelectedItem().toString() + "'";

			bookSearchString_SQL += whereString;
			System.out.println(bookSearchString_SQL);
			bookSearch();
			bookSearchString_SQL = "select book_id,book_name,book_type,book_author,book_press,book_date,book_price "
					+ "from t_books";

		}

	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
