package me.hope.asd.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUtil {
	
	@Deprecated
	private static void loadDriver()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private static final String CREATETABLES ="";

	/**public enum DataBaseType
	{
		NONE(""),MYSQL("com.mysql.jdbc.Driver"),ORACLE("oracle.jdbc.dirver.OracleDriver");
		private String drive;
		private DataBaseType(String drive)
		{
			this.drive = drive;
			
		}
		public String getDrive() {
			return drive;
		}
	}
	 * @throws SQLException */
	
	public static Connection getConnectionForMySQL(final String url,final String user,final String password) throws SQLException
	{
		loadDriver();
		java.sql.Connection result = null;
		result = DriverManager.getConnection(url,user,password);
		return result;
	}
	public static void createTable(Connection conn,String tableName)
	{
		
	}
	
}
