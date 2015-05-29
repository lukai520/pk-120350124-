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

	// 背景音乐
	BackgroundMusic bm;
	// 弹出菜单
	JMenuItem mi1 = new JMenuItem("刷新");
	JMenuItem mi2 = new JMenuItem("播放音乐");
	JMenuItem mi3 = new JMenuItem("停止音乐");
	JMenuItem mi4 = new JMenuItem("作者");
	JMenuItem mi5 = new JMenuItem("注销");
	JMenuItem mi6 = new JMenuItem("登录");
	JMenuItem mi7 = new JMenuItem("退出");

	private JTable table;
	private JTextField user_record_id_txf;
	private JComboBox bl_com;
	private JTextField lb_bookid_txf;// 书号
	private JTextField lb_userid_txf;// 用户名
	private JButton bl_sure_button;// 确定
	private JComboBox press_com;
	private JComboBox type_com;
	private JComboBox lend_com;
	private JMenuItem jmi1;
	// 用户信息
	private JButton userbookbutton;
	private JButton info_refresh_button;// 刷新
	private JTextArea info_ta;
	private JTextField addr_txf;
	private JTextField mail_txf;
	private JTextField tel_txf;
	private JTextField birth_txf;
	private JTextField sex_txf;
	private JTextField name_txf;
	private JTextField id_txf;
	private int flag = 1;
	private int rm = 0;// 记录个数
	private JButton modi_button;
	// 图书一览上的查询按钮
	final JButton bs_button;
	private JMenuItem logout_jmi, exit_jmi;
	private JTable book_search_table = null;
	DefaultTableModel defaultModel = null;
	DefaultTableModel defaultModels1 = null;
	DefaultTableModel defaultModels2 = null;

	private JTable stable1 = null;
	private JTable stable2 = null;
	// 书籍管理
	private JTable book_table;
	DefaultTableModel defaultModelbook = null;
	private JButton add_book_button;
	private JButton del_book_button;
	private JButton book_refresh_button;
	// 借阅记录
	private JTable record_table = null;
	DefaultTableModel defaultModelr1 = null;
	private JButton record_search_button = null;
	// 用户管理
	private JButton user_refresh_button1;
	private JTable user_table = null;
	DefaultTableModel defaultModeluser = null;
	private JButton add_user_button, del_user_button;

	// 图书一览，书籍查找字符串
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
			JOptionPane.showMessageDialog(null, "请输入用户编号!");
			return;
		} else if (lb_bookid_txf.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "请输入图书编号!");
			return;
		}
		rs = borrow_db.getResult("select* from t_users where user_id='"
				+ lb_userid_txf.getText().trim() + "'");
		try {
			if (rs.next() == false) {
				JOptionPane.showMessageDialog(null, "该用户不存在!");
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
				JOptionPane.showMessageDialog(null, "不存在该图书或该图书已借出!");
				return;
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String update_user_strSQL, update_book_strSQL, update_borrow_strSQL, update_lend_strSQL;
		// 增加在已经借的记录
		borrow_db.updateSql("insert into t_borrow values('"
				+ lb_userid_txf.getText().trim() + "','"
				+ lb_bookid_txf.getText().trim() + "','" + dateString + "')");
		// 更新个人借书数目，加1
		borrow_db
				.updateSql("update t_users set user_bookMount=user_bookMount+1 where user_id='"
						+ lb_userid_txf.getText().trim() + "'");
		// t_record增加
		rm++;
		borrow_db.updateSql("insert into t_record values (" + rm + ",'"
				+ lb_userid_txf.getText().trim() + "','"
				+ lb_bookid_txf.getText().trim() + "','借书','" + dateString
				+ "')");
		// 更新数目表，设为借出
		borrow_db.updateSql("update t_books set bl_state='是' where book_id='"
				+ lb_bookid_txf.getText().trim() + "'");

		JOptionPane.showMessageDialog(null, "借书成功!");
		// 刷新库存书籍
		lendManagerTableInit();
	}

	public void returnBook() {
		DataBase borrow_db = new DataBase();
		ResultSet rs = null;
		String dateString = null;
		DateFormat df = new DateFormat();
		dateString = df.dformat();
		if (lb_userid_txf.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "请输入用户编号!");
			return;
		} else if (lb_bookid_txf.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null, "请输入图书编号!");
			return;
		}
		rs = borrow_db.getResult("select* from t_users where user_id='"
				+ lb_userid_txf.getText().trim() + "'");
		try {
			if (rs.next() == false) {
				JOptionPane.showMessageDialog(null, "该用户不存在!");
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
				JOptionPane.showMessageDialog(null, "该用户没有借这本书!");
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

		borrow_db.updateSql("update t_books set bl_state='否' where book_id='"
				+ lb_bookid_txf.getText().trim() + "'");

		borrow_db.updateSql("update t_users set user_bookMount=user_bookMount-1 where user_id='"
						+ lb_userid_txf.getText().trim() + "'");
		rm++;
		borrow_db.updateSql("insert into t_record values (" + rm + ",'"
				+ lb_userid_txf.getText().trim() + "','"
				+ lb_bookid_txf.getText().trim() + "','还书','" + dateString
				+ "')");
		// 更新数目表，设为借出
		JOptionPane.showMessageDialog(null, "还书成功!");
		rm++;
		// 刷新库存书籍
		lendManagerTableInit();
	}

	public void userSearchRecord() {
		DataBase rc_db = new DataBase();
		ResultSet rs = null;
		String userRecordSQL = null;
		if (login_type.equals("普通用户"))
			userRecordSQL = "select user_id,user_name,book_id,book_name,record_type,record_date from record_view1 where user_name='"
					+ user + "'";
		else if (login_type.equals("管理员")) {
			if (user_record_id_txf.getText().trim().equals(""))
				userRecordSQL = "select user_id,user_name,book_id,book_name,record_type,record_date from record_view1";
			else {
				rs = rc_db
						.getResult("select *from record_view1 where user_id='"
								+ user_record_id_txf.getText().trim() + "'");
				try {
					if (rs.next() == false) {
						JOptionPane.showMessageDialog(null, "没有该用户的记录!");
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
			// 首先要删除table中的数据先：
			int rowCount = defaultModelr1.getRowCount() - 1;// 取得table中的数据行；
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModelr1.removeRow(j);// 删除rowCount行的数据；
				defaultModelr1.setRowCount(j);// 重新设置行数；
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 6; s++)
					data.addElement(rs.getString(s));

				defaultModelr1.addRow(data);
			}
			// lm_db.closeConnection();// 关闭数据源
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
			// 首先要删除table中的数据先：
			int rowCount = defaultModelbook.getRowCount() - 1;// 取得table中的数据行；
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModelbook.removeRow(j);// 删除rowCount行的数据；
				defaultModelbook.setRowCount(j);// 重新设置行数；
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 9; s++)
					data.addElement(rs.getString(s));

				defaultModelbook.addRow(data);
			}
			// lm_db.closeConnection();// 关闭数据源
			book_table.revalidate();
			System.out.println("书籍管理");
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
		// 菜单栏区
		final JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout(new BorderLayout());

		// 系统菜单
		JMenu system_menu = new JMenu("系统");

		logout_jmi = new JMenuItem();
		logout_jmi.setText("注销");
		logout_jmi.addActionListener(this);
		system_menu.add(logout_jmi);

		exit_jmi = new JMenuItem("退出");
		exit_jmi.addActionListener(this);
		system_menu.add(exit_jmi);
		menuBar.add(system_menu);
		setJMenuBar(menuBar);
		// tabpane的面板
		final JPanel main_pane = new JPanel();
		main_pane.setLayout(null);
		main_pane.setAutoscrolls(false);
		JPanel user_info_pane = new JPanel();
		user_info_pane.setAutoscrolls(true);
		user_info_pane.setLayout(null);
		final JTabbedPane tp = new JTabbedPane();
		// 主面板

		tp.add(main_pane, "主页");

		final JPopupMenu mainpppMenu = new JPopupMenu();

		final JPanel pane2 = new JPanel();
		// 设置为透明
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
		// 主界面菜单
		final JLabel img_label = new JLabel(ico1);
		img_label.setBounds(0, 0, 850, 454);
		img_label.setAutoscrolls(true);
		img_label.setBackground(Color.BLUE);
		img_label.setText("");
		main_pane.add(img_label);
		tp.add(user_info_pane, "用户信息");

		final JLabel id_label = new JLabel();
		id_label.setText("编号");
		id_label.setBounds(35, 10, 66, 18);
		user_info_pane.add(id_label);

		final JLabel name_label = new JLabel();
		name_label.setText("姓名");
		name_label.setBounds(289, 10, 66, 18);
		user_info_pane.add(name_label);

		final JLabel sex_label = new JLabel();
		sex_label.setText("性别");
		sex_label.setBounds(35, 48, 52, 18);
		user_info_pane.add(sex_label);

		final JLabel birth_label = new JLabel();
		birth_label.setText("出生日期");
		birth_label.setBounds(289, 48, 66, 18);
		user_info_pane.add(birth_label);

		final JLabel tel_label = new JLabel();
		tel_label.setText("联系电话");
		tel_label.setBounds(35, 91, 52, 18);
		user_info_pane.add(tel_label);

		final JLabel mail_label = new JLabel();
		mail_label.setText("电子邮箱");
		mail_label.setBounds(289, 91, 66, 18);
		user_info_pane.add(mail_label);

		final JLabel addr_label = new JLabel();
		addr_label.setText("地址");
		addr_label.setBounds(35, 135, 66, 18);
		user_info_pane.add(addr_label);

		final JLabel info_label = new JLabel();
		info_label.setText("备注");
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
		modi_button.setText("修改信息");
		modi_button.addActionListener(this);
		modi_button.setBounds(422, 287, 106, 28);
		user_info_pane.add(modi_button);

		info_refresh_button = new JButton();
		info_refresh_button.setText("刷新");
		info_refresh_button.setBounds(243, 290, 106, 28);
		info_refresh_button.addActionListener(this);
		user_info_pane.add(info_refresh_button);

		userbookbutton = new JButton();
		userbookbutton.setText("查看已借书籍");
		userbookbutton.setBounds(60, 287, 112, 28);
		userbookbutton.addActionListener(this);

		String[] name = { "书号", "书名", "类型", "作者", "出版社", "出版日期", "价格" };

		String[][] data = new String[0][0];
		defaultModel = new DefaultTableModel(data, name);
		// tab面板区

		// tp.setEnabledAt(1, false);
		tp.setTabPlacement(JTabbedPane.LEFT);
		getContentPane().add(tp);
		JPanel book_search_pane = new JPanel();
		book_search_pane.setAutoscrolls(true);
		book_search_pane.setLayout(null);
		// 书籍一览面板
		tp.add(book_search_pane, "图书一览");
		book_search_table = new JTable(defaultModel);
		book_search_table.setPreferredScrollableViewportSize(new Dimension(400,
				80));

		final JScrollPane scrollPane = new JScrollPane(book_search_table);

		scrollPane.setBackground(Color.BLACK);
		scrollPane.setAutoscrolls(true);
		scrollPane.setBounds(0, 0, 756, 375);
		book_search_pane.add(scrollPane);

		lend_com = new JComboBox();
		lend_com.setModel(new DefaultComboBoxModel(new String[] { "不限", "是",
				"否" }));
		lend_com.setBounds(502, 404, 60, 27);
		book_search_pane.add(lend_com);

		type_com = new JComboBox();
		type_com.setModel(new DefaultComboBoxModel(new String[] { "不限" }));
		type_com.setBounds(252, 404, 103, 27);
		book_search_pane.add(type_com);

		final JLabel label = new JLabel();
		label.setText("类型");
		label.setBounds(197, 408, 34, 18);
		book_search_pane.add(label);

		final JLabel label_1 = new JLabel();
		label_1.setText("是否已借出");
		label_1.setBounds(403, 408, 71, 18);
		book_search_pane.add(label_1);

		final JLabel label_4 = new JLabel();
		label_4.setText("出版社");
		label_4.setBounds(0, 408, 39, 18);
		book_search_pane.add(label_4);

		press_com = new JComboBox();
		press_com.setName("");
		press_com.setMaximumRowCount(10);
		press_com.setModel(new DefaultComboBoxModel(new String[] { "不限" }));
		press_com.setBounds(45, 403, 116, 27);
		book_search_pane.add(press_com);

		bs_button = new JButton();
		bs_button.setText("查询");
		bs_button.setBounds(650, 403, 106, 28);
		bs_button.addActionListener(this);
		book_search_pane.add(bs_button);

		// 借阅书籍面板，借书和还书
		JPanel bl_pane = new JPanel();
		bl_pane.setAutoscrolls(true);
		bl_pane.setLayout(null);
		tp.add(bl_pane, "借阅管理");

		final JLabel label_2 = new JLabel();
		label_2.setText("用户编号");
		label_2.setBounds(142, 423, 66, 19);
		bl_pane.add(label_2);

		final JLabel label_3 = new JLabel();
		label_3.setText("书号");
		label_3.setBounds(401, 423, 26, 18);
		bl_pane.add(label_3);

		lb_userid_txf = new JTextField();
		lb_userid_txf.setBounds(227, 420, 105, 24);
		bl_pane.add(lb_userid_txf);

		lb_bookid_txf = new JTextField();
		lb_bookid_txf.setBounds(449, 421, 92, 23);
		bl_pane.add(lb_bookid_txf);

		bl_com = new JComboBox();
		bl_com.setModel(new DefaultComboBoxModel(new String[] { "借书", "还书" }));
		bl_com.setBounds(25, 420, 66, 24);
		bl_pane.add(bl_com);

		bl_sure_button = new JButton();
		bl_sure_button.setText("确定");
		bl_sure_button.addActionListener(this);
		bl_sure_button.setBounds(594, 418, 77, 28);
		bl_pane.add(bl_sure_button);
		// 借书时显示所有用户和书籍
		String[] name1 = { "编号", "用户名", "借书总数" };
		String[][] data1 = new String[0][0];
		defaultModels1 = new DefaultTableModel(data1, name1);
		stable1 = new JTable(defaultModels1);
		stable1.setPreferredScrollableViewportSize(new Dimension(400, 80));

		String[] name2 = { "书号", "书名" };
		String[][] data2 = new String[0][0];
		defaultModels2 = new DefaultTableModel(data2, name2);
		stable2 = new JTable(defaultModels2);
		stable2.setPreferredScrollableViewportSize(new Dimension(23, 34));

		final JScrollPane SPanel1 = new JScrollPane(stable1);
		SPanel1.setAutoscrolls(true);
		SPanel1.setBounds(0, 30, 383, 356);
		bl_pane.add(SPanel1);
		// 弹出菜单
		final JPopupMenu pppMenu1 = new JPopupMenu();
		pppMenu1.setLabel("");
		pppMenu1.setForeground(Color.LIGHT_GRAY);
		pppMenu1.setFont(new Font("", Font.BOLD, 15));
		jmi1 = new JMenuItem("查看");
		jmi1.addActionListener(this);
		pppMenu1.add(jmi1);
		addPopup(SPanel1, pppMenu1);

		final JScrollPane SPanel2 = new JScrollPane(stable2);
		SPanel2.setAutoscrolls(true);
		SPanel2.setBounds(389, 30, 369, 356);
		bl_pane.add(SPanel2);

		final JLabel label_5 = new JLabel();
		label_5.setText("所有用户：");
		label_5.setBounds(0, -2, 92, 24);
		bl_pane.add(label_5);

		final JLabel label_6 = new JLabel();
		label_6.setText("库存书籍：");
		label_6.setBounds(389, 6, 66, 18);
		bl_pane.add(label_6);

		final JLabel label_9 = new JLabel();
		label_9.setText("选中一个用户后，右键可查看借阅的书籍！");
		label_9.setBounds(0, 396, 272, 21);
		bl_pane.add(label_9);

		// 用户管理面板，包括用户添加和用户删除
		JPanel user_manage_pane = new JPanel();
		user_manage_pane.setAutoscrolls(true);
		user_manage_pane.setLayout(null);
		tp.add(user_manage_pane, "用户管理");

		String[] user_info_str = { "用户编号", "用户名", "性别", "出生年月", "电话", "邮箱",
				"班级", "简介", "借书数目" };
		String[][] user_data = new String[0][0];
		defaultModeluser = new DefaultTableModel(user_data, user_info_str);
		user_table = new JTable(defaultModeluser);
		user_table.setPreferredScrollableViewportSize(new Dimension(23, 34));
		// user_table= new JTable(defaultModeluser);

		final JScrollPane user_spane = new JScrollPane(user_table);
		user_spane.setBounds(0, 0, 766, 358);
		user_manage_pane.add(user_spane);

		add_user_button = new JButton();
		add_user_button.setText("添加用户");
		add_user_button.setBounds(76, 382, 106, 28);
		add_user_button.addActionListener(this);
		user_manage_pane.add(add_user_button);

		del_user_button = new JButton();
		del_user_button.setText("删除用户");
		del_user_button.setBounds(243, 382, 106, 28);
		del_user_button.addActionListener(this);
		user_manage_pane.add(del_user_button);

		user_refresh_button1 = new JButton();
		user_refresh_button1.setText("刷新");
		user_refresh_button1.addActionListener(this);
		user_refresh_button1.setBounds(407, 382, 106, 28);
		user_manage_pane.add(user_refresh_button1);

		// 借阅记录,管理员可以查看所有记录，用户可以查看自己的记录
		JPanel bl_record_pane = new JPanel();
		bl_record_pane.setAutoscrolls(true);
		bl_record_pane.setLayout(null);
		tp.add(bl_record_pane, "借阅记录");

		String[] record_head_str = { "用户编号", "用户名", "书号", "书名", "类型", "时间" };
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
		label_7.setText("用户名");

		user_record_id_txf = new JTextField();
		user_record_id_txf.setBounds(353, 40, 106, 24);
		record_sc_panel.add(user_record_id_txf);

		record_search_button = new JButton();
		record_search_button.setBounds(545, 39, 89, 27);
		record_sc_panel.add(record_search_button);
		record_search_button.addActionListener(this);
		record_search_button.setText("确定");
		// 书籍管理，管理员可以增加和删除书籍

		JPanel book_manage_pane = new JPanel();
		book_manage_pane.setAutoscrolls(true);
		book_manage_pane.setLayout(null);
		tp.add(book_manage_pane, "书籍管理");

		String[] book_str = { "书号", "书名", "类型", "作者", "出版社", "出版日期", "价格",
				"备注", "借出" };
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
		add_book_button.setText("添加书籍");
		add_book_button.setBounds(117, 405, 106, 28);
		add_book_button.addActionListener(this);
		book_manage_pane.add(add_book_button);

		del_book_button = new JButton();
		del_book_button.setText("删除书籍");
		del_book_button.addActionListener(this);
		del_book_button.setBounds(273, 405, 106, 28);
		book_manage_pane.add(del_book_button);

		book_refresh_button = new JButton();
		book_refresh_button.addActionListener(this);
		book_refresh_button.setText("刷新");
		book_refresh_button.setBounds(421, 405, 106, 28);
		book_manage_pane.add(book_refresh_button);

		// 权限管理
		if (login_type.equals("游客")) {
			tp.setEnabledAt(1, false);
			tp.setEnabledAt(3, false);
			tp.setEnabledAt(4, false);
			tp.setEnabledAt(5, false);
			tp.setEnabledAt(6, false);
		} else if (login_type.equals("普通用户")) {
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
		label_8.setText("查询指定用户的借阅记录：");
		bookSearch();
		bookManage();
		lendManagerTableInit();
		CountRecord cr = new CountRecord();
		rm = cr.borrowedMount();
		System.out.println("记录数:" + rm);
		// ---------------------------------------------------
		// JOptionPane.showMessageDialog(null, login_type);
		this.setVisible(true);
		allUserInfo();
		userSearchRecord();
		this.userInfo();
		setResizable(true);
	}

	// 向combox添加item的函数
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
			// 首先要删除table中的数据先：
			int rowCount = defaultModeluser.getRowCount() - 1;// 取得table中的数据行；
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModeluser.removeRow(j);// 删除rowCount行的数据；
				defaultModeluser.setRowCount(j);// 重新设置行数；
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

	// 初始化表
	public void lendManagerTableInit() {
		DataBase lm_db = new DataBase();
		ResultSet rs = null;
		String userStringSQL = "select user_id,user_name,user_bookMount from t_users";
		String remainBookStringSQL = "select book_id,book_name from remain_view";
		rs = lm_db.getResult(userStringSQL);
		try {
			// 首先要删除table中的数据先：
			int rowCount = defaultModels1.getRowCount() - 1;// 取得table中的数据行；
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModels1.removeRow(j);// 删除rowCount行的数据；
				defaultModels1.setRowCount(j);// 重新设置行数；
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 3; s++)
					data.addElement(rs.getString(s));

				defaultModels1.addRow(data);
			}
			// lm_db.closeConnection();// 关闭数据源
			stable1.revalidate();
			System.out.println("初始化表1完成!");
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// -------------------------
		rs = lm_db.getResult(remainBookStringSQL);
		try {
			// 首先要删除table中的数据先：
			int rowCount = defaultModels2.getRowCount() - 1;// 取得table中的数据行；
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModels2.removeRow(j);// 删除rowCount行的数据；
				defaultModels2.setRowCount(j);// 重新设置行数；
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 2; s++)
					data.addElement(rs.getString(s));

				defaultModels2.addRow(data);
			}
			lm_db.closeConnection();// 关闭数据源
			stable2.revalidate();
			System.out.println("初始化表2完成!");
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

	}

	// 更新管理员信息
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

		JOptionPane.showMessageDialog(null, "修改成功");
	}

	// 更新用户信息
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

		JOptionPane.showMessageDialog(null, "修改成功");
	}

	// 用户信息面板，用于显示信息的函数
	public void userInfo() {
		DataBase user_db = new DataBase();
		ResultSet user_rs = null;
		String userinfo_StringSQL = null;
		if (this.login_type.equals("普通用户")) {
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
		} else if (this.login_type.equals("管理员")) {
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

	// 书籍浏览
	public void bookSearch() {
		ResultSet rs, rs1, rs2 = null;
		DataBase bs_db = new DataBase();
		String press_SQL = "select book_press from press_view";
		String type_SQL = "select book_type from type_view";
		if (flag == 1) {
			// ----------------------出版社--------------------------------------------
			rs = bs_db.getResult(press_SQL);
			try {
				while (rs.next()) {
					press_com.addItem(makeObj(rs.getString("book_press")));

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// -----------------------书的类型----------------------------------------
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
		// ------------------------------------表格---------------------------------------

		rs = bs_db.getResult(bookSearchString_SQL);
		try {
			// 首先要删除table中的数据先：
			int rowCount = defaultModel.getRowCount() - 1;// 取得table中的数据行；
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModel.removeRow(j);// 删除rowCount行的数据；
				defaultModel.setRowCount(j);// 重新设置行数；
				j = j - 1;
			}
			while (rs.next()) {
				Vector data = new Vector();
				for (int s = 1; s <= 7; s++)
					data.addElement(rs.getString(s));

				defaultModel.addRow(data);
			}
			bs_db.closeConnection();// 关闭数据源
			book_search_table.revalidate();
			System.out.println("查询完成!!!");
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

	}

	// 动作响应
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

			JOptionPane.showMessageDialog(null,"作者:陆凯\n学校:哈工大\n班级:1311101班\n学号:120350124");
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
			// 查询用户记录
			System.out.println("查询用户借书记录");
			userSearchRecord();
		} else if (e.getSource().equals(userbookbutton)) {
			UserId ui = new UserId();

			new UserBorrowedBook(ui.userId(user));
		} else if (e.getSource().equals(jmi1)) {
			// 弹出菜单
			// 先获取被选择的用户
			int i = stable1.getSelectedRow();
			if (i == -1)
				return;
			String id_str;
			id_str = stable1.getValueAt(i, 0).toString();
			System.out.println(id_str);
			new UserBorrowedBook(id_str);
			System.out.println("查看用户所借书籍");
		} else if (e.getSource().equals(add_book_button)) {
			// 添加书籍
			System.out.println("添加书籍");
			new AddBook();
			bookManage();
		} else if (e.getSource().equals(del_book_button)) {
			System.out.println("删除书籍");
			new DeleteBook();
			bookManage();
			// 删除书籍
		} else if (e.getSource().equals(book_refresh_button)) {
			System.out.println("刷新书籍管理面板");
			bookManage();
			// 刷新书籍管理面板
		} else if (e.getSource().equals(user_refresh_button1))
			allUserInfo();
		else if (e.getSource().equals(del_user_button)) {
			System.out.println("helllllll");
			new DeleteUser();
		} else if (e.getSource().equals(add_user_button)) {
			// 添加用户对话框
			new AddUser();
		} else if (e.getSource().equals(del_user_button)) {
			// 删除用户对话框
		} else if (e.getSource().equals(record_search_button)) {
			userSearchRecord();
		} else if (e.getSource().equals(bl_sure_button)) {
			if (bl_com.getSelectedItem().equals("借书")) {
				borrowBook();
				userSearchRecord();
			} else if (bl_com.getSelectedItem().equals("还书")) {
				returnBook();
				userSearchRecord();
			}
		} else if (e.getSource().equals(modi_button)) {
			if (login_type.equals("普通用户"))
				updateUserInfo();
			else if (login_type.equals("管理员"))
				updatemgInfo();
		} else if (e.getSource().equals(info_refresh_button)) {
			userInfo();
			System.out.println("刷新");
		} else if (e.getSource().equals(exit_jmi)) {
			System.exit(0);
		} else if (e.getSource().equals(logout_jmi)) {
			// this.setVisible(false);
			this.dispose();
			WelcomeFrame wf = new WelcomeFrame();
			wf.setVisible(true);
		} else if (e.getSource().equals(bs_button)) {
			String whereString = null;
			// 3个均未选
			if (press_com.getSelectedItem().toString().equals("不限")
					&& type_com.getSelectedItem().toString().equals("不限")
					&& lend_com.getSelectedItem().toString().equals("不限"))
				whereString = "";
			// 1
			else if (type_com.getSelectedItem().toString().equals("不限")
					&& lend_com.getSelectedItem().toString().equals("不限"))
				whereString = " where book_press='"
						+ press_com.getSelectedItem().toString() + "'";
			// 2
			else if (press_com.getSelectedItem().toString().equals("不限")
					&& lend_com.getSelectedItem().toString().equals("不限"))
				whereString = " where book_type='"
						+ type_com.getSelectedItem().toString() + "'";
			// 3
			else if (press_com.getSelectedItem().toString().equals("不限")
					&& type_com.getSelectedItem().toString().equals("不限"))
				whereString = " where bl_state='"
						+ lend_com.getSelectedItem().toString() + "'";
			// 1和2
			else if (lend_com.getSelectedItem().toString().equals("不限"))
				whereString = " where book_press='"
						+ press_com.getSelectedItem().toString()
						+ "' and  book_type='"
						+ type_com.getSelectedItem().toString() + "'";
			// 2和3
			else if (press_com.getSelectedItem().toString().equals("不限"))
				whereString = " where bl_state='"
						+ lend_com.getSelectedItem().toString()
						+ "' and  book_type='"
						+ type_com.getSelectedItem().toString() + "'";
			// 1和3
			else if (press_com.getSelectedItem().toString().equals("不限"))
				whereString = " where book_press='"
						+ press_com.getSelectedItem().toString()
						+ "' and  book_lend='"
						+ lend_com.getSelectedItem().toString() + "'";
			// 全部
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
