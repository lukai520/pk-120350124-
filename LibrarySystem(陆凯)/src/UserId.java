import java.io.*;
import java.awt.*;
import java.sql.*;
public class UserId {
	DataBase db=new DataBase();
	ResultSet rs=null;
	String str=null;
	public String userId(String name)
	{
		str="select user_id from t_users where user_name='name'";
		rs=db.getResult(str);
		try {
			if(rs.next())
			{
				return rs.getString(1).toString().trim();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
