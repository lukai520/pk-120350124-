import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;
public class DateFormat 
{

	public String dformat() 
	{
		String str = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// �������ڸ�ʽ
		Date dd = new Date();
		str = df.format(dd);// new Date()Ϊ��ȡ��ǰϵͳʱ��
		System.out.println(str);
		return str;
	}
}