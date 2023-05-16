
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB5 {
	// �̱��� �������� ��� �ϱ��� �� �ڵ��
	private static ConnectDB5 instance = new ConnectDB5();

	public static ConnectDB5 getInstance() {
		return instance;
	}

	public ConnectDB5() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL ���� "jdbc:mysql://localhost:3306/DB�̸�"
	private String dbId = "root"; // MySQL ���� "������ ��� root"
	private String dbPw = "Lotte1234!"; // ��й�ȣ "mysql ��ġ �� ������ ��й�ȣ"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;
	private String sql = "";
	private String sql2 = "", sql3="", sql4="";
	String returns = "";
	String returns2 = "";
	String result1, storeID, pid, pid2;
	Integer result;
	
	// -------------�߰�--------------
	public String insertDB(String depth, String getID, String content, String depthName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			//returns=pstmt.toString();
			if(rs.next()) {
				Integer depth1=Integer.valueOf(depth);
				storeID=rs.getString("storeID"); 
				//returns="������� ����? storeID : "+storeID; ����� ��µ�
				sql2="select uid from question where storeID=? and depth=? and name=?";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, storeID);
				pstmt.setInt(2, 1); // 1�ܰ��� uid�� �������°ű⶧���� !
				pstmt.setString(3, depthName);
				rs = pstmt.executeQuery();
				//returns="����ǳ� ?"+pstmt.toString();
				if(rs.next()) {
					//returns="����� ������ ?"+pstmt.toString();
					pid=rs.getString("uid");
					// insert into question(pid, name, storeID, depth) values('107','�ʹ���������','1',2);
					sql3="insert into question(pid, name, storeID, depth) values(?,?,?,?)";
					//select * from question where name='�ʹ���������' and storeID='1' and depth=2;
					sql4="select * from question where name=? and storeID=? and depth=? and pid=?"; // �ߺ��� ������ Ȯ���Ϸ���
					
					pstmt = conn.prepareStatement(sql4);
					pstmt.setString(1, content);
					pstmt.setString(2, storeID);
					pstmt.setInt(3, depth1);
					pstmt.setString(4, pid);
					rs = pstmt.executeQuery();
					
					returns=pstmt.toString();
					if(rs.next()) {
						result1="����";
					}else {
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, pid);
						pstmt.setString(2, content);
						pstmt.setString(3, storeID);
						pstmt.setInt(4, depth1);
						result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
						//returns=pstmt.toString();
						if(result>=1) {
							result1="����";
						}
					}
				}
			}
			
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return result1;
	}
	
	// -------------����--------------
	public String updateDB(String depth, String getID, String oldContent, String newContent, String depthName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select storeID from manager where managerID='main'; �̷��� ����� ���̵� ���� ã���� �Ŀ� insert ����
			// Integer.valueOf() �̰ɷ� ��Ʈ�� -> ��Ʈ
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			//result1=pstmt.toString();
			//returns="������� ����?";
			if(rs.next()) {
				storeID=rs.getString("storeID"); 
				//returns="������� ����? depth : "+depth;
				Integer depth1=Integer.valueOf(depth);

				sql2="select uid from question where storeID=? and depth=? and name=?";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, storeID);
				pstmt.setInt(2, 1); // 1�ܰ��� uid�� �������°ű⶧���� !
				pstmt.setString(3, depthName);
				rs = pstmt.executeQuery();
				//result1="����ǳ� ?"+pstmt.toString();
				
				if(rs.next()) {
					//returns="����� ������ ?"+pstmt.toString();
					pid=rs.getString("uid");
					
					//select * from question where name='�ʹ���������' and storeID='1' and depth=2;
					sql4="select * from question where name=? and storeID=? and depth=? and pid=?"; // �ߺ��� ������ Ȯ���Ϸ���
					pstmt = conn.prepareStatement(sql4);
					pstmt.setString(1, newContent);
					pstmt.setString(2, storeID);
					pstmt.setInt(3, depth1);
					pstmt.setString(4, pid);
					rs = pstmt.executeQuery();
					//rs.last();
					//int row = rs.getRow(); 
					//rs.beforeFirst();
					if(rs.next()) {
						returns="����"; // �ߺ��� ����
					}else {
						// update question set name='�ʹ��߿���' where name='�ʹ� ������' and storeID='1' and depth=2 and pid=107;
						sql3="update question set name=? where name=? and storeID=? and depth=? and pid=?";
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, newContent);
						pstmt.setString(2, oldContent);
						pstmt.setString(3, storeID);
						pstmt.setInt(4, depth1);
						pstmt.setString(5, pid);
						result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
						returns=String.valueOf(result);
						if(result>=1) {
							returns="����";
						}
					}
				}
			}
			
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	// -------------����--------------
	public String deleteDB(String depth, String getID, String oldContent, String depthName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select storeID from manager where managerID='main'; �̷��� ����� ���̵� ���� ã���� �Ŀ� insert ����
			// Integer.valueOf() �̰ɷ� ��Ʈ�� -> ��Ʈ
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			//returns=pstmt.toString();
			//returns="������� ����?";
			if(rs.next()) {
				storeID=rs.getString("storeID"); 
				//returns="������� ����? depth : "+depth;
				Integer depth1=Integer.valueOf(depth);

				sql2="select uid from question where storeID=? and depth=? and name=?";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, storeID);
				pstmt.setInt(2, 1); // 1�ܰ��� uid�� �������°ű⶧���� !
				pstmt.setString(3, depthName);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					pid=rs.getString("uid"); // 1�ܰ� uid ������ ��
					sql3="select uid from question where storeID=? and depth=? and name=? and pid=?"; // 2�ܰ��� uid�� ã�����ؼ�
					pstmt=conn.prepareStatement(sql3);
					pstmt.setString(1, storeID);
					pstmt.setInt(2, 2);
					pstmt.setString(3, oldContent);
					pstmt.setString(4, pid); // 
					rs=pstmt.executeQuery();
					
					if(rs.next()) {
						pid2=rs.getString("uid"); // 2�ܰ� uid ������� 3�ܰ� ������ ������
						// delete from question where name='�ʹ� �̲�������' and storeID='1' and depth=2 and pid=111;
						sql4="delete from question where name=? and storeID=? and depth=? and pid=?";
						pstmt = conn.prepareStatement(sql4);
						pstmt.setString(1, oldContent);
						pstmt.setString(2, storeID);
						pstmt.setInt(3, depth1);
						pstmt.setString(4, pid);
						result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
						if(result>=1) { // 2�ܰ� ������ �����ϸ� �׿� �ش��ϴ� 3�ܰ赵 �ܰ������� ���������
							//returns="����";
							sql4="delete from question where storeID=? and depth=? and pid=?"; // pid�� �������ٸ� �ڽĵ� �� ����������
							pstmt=conn.prepareStatement(sql4);
							pstmt.setString(1, storeID);
							pstmt.setInt(2, 3);
							pstmt.setString(3, pid2);
							result=pstmt.executeUpdate();
							if(result>=1) {
								returns="����";
							}else {
								returns="����"; // 3�ܰ谡 ���� ������������ ����
							}
						}else {
							returns="����";
						}
					}
						
					
				}
			}
			
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
}



