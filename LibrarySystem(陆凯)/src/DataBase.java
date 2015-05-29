import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//连接数据库的类，包含查询和修改模块
public class DataBase {
	Connection con;
	ResultSet rs;
	Statement stmt;
	//数据库连接构造函数
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
	// 返回查询结构
	public ResultSet getResult(String strSQL) {
		try {
			rs = stmt.executeQuery(strSQL);
			return rs;
		} catch (SQLException sqle) {
	
			System.out.println(sqle.toString());
			return null;
		}

	}
	//更新数据库
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
	//关闭数据源
	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException sqle) {
			System.out.println(sqle.toString());
		}
	}

}
