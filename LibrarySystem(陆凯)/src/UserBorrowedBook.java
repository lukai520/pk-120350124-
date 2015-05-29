import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.*;



public class UserBorrowedBook extends JDialog implements WindowListener{

	DataBase db=null;
	ResultSet rs=null;
	String strSQL=null;
	DefaultTableModel defaultModel = null;
	JTable table=null;
	public UserBorrowedBook(String id)
	{
		super();
		setTitle("【用户已借书籍】");
		setBounds(new Rectangle(200, 300, 450, 350));
		getContentPane().setLayout(null);
		
		strSQL="select book_id,book_name,br_in from borrowed_book_view where user_id='"+id+"'";
		db=new DataBase();
		String[] name = {"书籍编号","书名","借阅日期"};
		String[][] data = new String[0][0];
		defaultModel = new DefaultTableModel(data, name);
		table = new JTable(defaultModel);
		table.setPreferredScrollableViewportSize(new Dimension(400,	80));
		final JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 0, 492, 337);
		this.getContentPane().add(scrollPane);	
		rs=db.getResult(strSQL);		
		try {
			int rowCount = defaultModel.getRowCount() - 1;// 取得table中的数据行；
			int j = rowCount;
			for (int i = 0; i <= rowCount; i++) {
				defaultModel.removeRow(j);// 删除rowCount行的数据；
				defaultModel.setRowCount(j);// 重新设置行数；
				j = j - 1;
			}
			while (rs.next()) {
				Vector data1 = new Vector();
				for (int s = 1; s <= 3; s++)
					data1.addElement(rs.getString(s));
				defaultModel.addRow(data1);
			}
			db.closeConnection();
			table.revalidate();
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		this.setVisible(true);
		
	}
	public void windowClosing(WindowEvent e)
	{
		this.dispose();
	}

	public void windowDeactivated(WindowEvent arg0) {

	}

	public void windowDeiconified(WindowEvent arg0) {

	}
	public void windowIconified(WindowEvent arg0) {
		
	}
	public void windowOpened(WindowEvent arg0) {

	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
