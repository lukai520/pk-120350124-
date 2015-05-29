import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;
public class DateFormat 
{

	public String dformat() 
	{
		String str = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// 设置日期格式
		Date dd = new Date();
		str = df.format(dd);// new Date()为获取当前系统时间
		System.out.println(str);
		return str;
	}
}