import javax.swing.*;
import java.sql.*;
import java.io.*;
public class CountRecord {
	public int borrowedMount()
	{	
		int res=0;
		DataBase db=new DataBase();
		ResultSet rs=db.getResult("select max(record_id) from t_record group by record_id");
		try {
			while(rs.next())
			{
				res=rs.getInt(1);	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
