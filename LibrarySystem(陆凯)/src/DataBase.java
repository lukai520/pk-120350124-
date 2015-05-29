import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//�������ݿ���࣬������ѯ���޸�ģ��
public class DataBase {
	Connection con;
	ResultSet rs;
	Statement stmt;
	//���ݿ����ӹ��캯��
	public DataBase() {
		String driverName = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		try {
			try {
				Class.forName(driverName);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			con = DriverManager.getConnection("jdbc:sqlserver://172.16.88.39:1433;DatabaseName=LSystem","sa", "110");
			
//					String url = "jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb)};DBQ=D:\\123.mdb";
//					con=DriverManager.getConnection(url, "", "");
		
		
					stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,	ResultSet.CONCUR_UPDATABLE);

		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		}
	}
	// ���ز�ѯ�ṹ
	public ResultSet getResult(String strSQL) {
		try {
			rs = stmt.executeQuery(strSQL);
			return rs;
		} catch (SQLException sqle) {
	
			System.out.println(sqle.toString());
			return null;
		}

	}
	//�������ݿ�
	public boolean updateSql(String strSQL) {
		try {
			stmt.executeUpdate(strSQL);
			con.commit();
			return true;

		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
			return false;
		}

	}
	//�ر�����Դ
	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		}
	}

}
