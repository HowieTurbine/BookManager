import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBase {

	private Connection conn;
	private java.sql.Statement stmt;
	
		DataBase()
		{
			try {
				Class.forName("com.mysql.jdbc.Driver");// ��һ��,������
				//long start=System.currentTimeMillis();
				conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/bookmanager?characterEncoding=utf8&useSSL=true","root","Zhy19960102");//�ڶ�������������
				//long end=System.currentTimeMillis();
				stmt=conn.createStatement();
				//System.out.println(conn);
				//System.out.println("��ʱ"+(end-start)+"ms");//���������Ǻܺ�ʱ���,���ڻ�������ӳ�����
	
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public Connection getConn() {
			return conn;
		}
		public java.sql.Statement  getStmt() {
			return  stmt;
		}
		
}