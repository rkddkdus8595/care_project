
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB6 {
	// �̱��� �������� ��� �ϱ��� �� �ڵ��
	private static ConnectDB6 instance = new ConnectDB6();

	public static ConnectDB6 getInstance() {
		return instance;
	}

	public ConnectDB6() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL ���� "jdbc:mysql://localhost:3306/DB�̸�"
	private String dbId = "root"; // MySQL ���� "������ ��� root"
	private String dbPw = "Lotte1234!"; // ��й�ȣ "mysql ��ġ �� ������ ��й�ȣ"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null, rs1=null, rs2=null, rs3=null;
	private String sql = "";
	private String sql2 = "", sql3="", sql4="", sql5="";
	String returns = "";
	String returns2 = "";
	String result1, storeID, pid;
	Integer result;
	int cnt=0;
	
	// -------------�߰�--------------
	public String insertDB(String depth, String getID, String content, String depthName, String depthNameTwo, String subject) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql="select storeID from manager where managerID=?"; // �ϴ� �ش� ���̵� ���� �Ŵ����� �ð� �ִ� ���� ���̵���� ����´�.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Integer depth1=Integer.valueOf(depth); // ������ �� ������ ���������� ��ȯ
				storeID=rs.getString("storeID"); 
				//select uid from question where storeID='1' and depth=1 and name='�ü�'; 1�ܰ��� uid ���� ã���ֱ� 2�ܰ� uid ã���� ����ϱ� ���� !
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
					//select uid from question where storeID='1' and depth=2 and name='�ʹ� �߿���' and pid='107';
					sql2="select uid from question where storeID=? and depth=? and name=? and pid=?";
					pstmt = conn.prepareStatement(sql2);
					pstmt.setString(1, storeID);
					pstmt.setInt(2, 2); // 2�ܰ��� uid�� �������°ű⶧���� !
					pstmt.setString(3, depthNameTwo);
					pstmt.setString(4, pid);
					rs = pstmt.executeQuery();
					if(rs.next()) {
						pid=rs.getString("uid");
						
						// select * from question where name='3��' and subject='����' and storeID='1' and depth=3 and pid='111';
						//sql4="select * from question where name=? and subject=? and storeID=? and depth=? and pid=?"; // �ߺ��� ������ Ȯ���Ϸ���
						sql4="select * from question where subject=? and storeID=? and depth=? and pid=?"; // �ߺ��� ������ Ȯ���Ϸ���
						pstmt = conn.prepareStatement(sql4);
						//pstmt.setString(1, content);
						pstmt.setString(1, subject);
						pstmt.setString(2, storeID);
						pstmt.setInt(3, depth1);
						pstmt.setString(4, pid);
						rs = pstmt.executeQuery();
						
						//returns2=pstmt.toString();
						if(rs.next()) {
							result1="����";
						}else {
							sql3="insert into question(pid, subject, name, storeID, depth) values(?,?,?,?,?)";
							pstmt = conn.prepareStatement(sql3);
							pstmt.setString(1, pid);
							pstmt.setString(2, subject);
							pstmt.setString(3, content);
							pstmt.setString(4, storeID);
							pstmt.setInt(5, depth1);
							result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
							if(result>=1) {
								sql4="select uid from question where storeID=? and depth=3 and subject=? and name=?";
								pstmt=conn.prepareStatement(sql4);
								pstmt.setString(1, storeID);
								pstmt.setString(2, subject);
								pstmt.setString(3, content);
								rs=pstmt.executeQuery();
								if(rs.next()) {
									//returns="����� ������ ?"+pid;
									pid=rs.getString("uid");
									String[] str=content.split(";"); // ������ �ɰ��� ��������
									for(int i=0; i<str.length;i++) { // �ϳ��ϳ� �ڵ�ȭ�� ������ֱ� ���� ���� ������ ���̺� �� �־��ش�.
										// insert into question(pid, subject, name, storeID, depth) values('111','����','3��','1',3);
										sql5="insert into detail(pid, name, storeID) values(?,?,?)";
										pstmt = conn.prepareStatement(sql5);
										pstmt.setString(1, pid);
										pstmt.setString(2, str[i]);
										pstmt.setString(3, storeID);
										result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
										returns+=pstmt.toString();
										if(result>=1) {
											result1="����";
										}
									}
								}
							}
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
	public String updateDB(String depth, String getID, String oldTitle, String newTitle, String oldContent, 
			String depthName, String depthNameTwo, String newContent) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql="select storeID from manager where managerID=?"; // �ϴ� �ش� ���̵� ���� �Ŵ����� �ð� �ִ� ���� ���̵���� ����´�.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Integer depth1=Integer.valueOf(depth); // ������ �� ������ ���������� ��ȯ
				storeID=rs.getString("storeID"); 
				//select uid from question where storeID='1' and depth=1 and name='�ü�'; 1�ܰ��� uid ���� ã���ֱ� 2�ܰ� uid ã���� ����ϱ� ���� !
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
					//select uid from question where storeID='1' and depth=2 and name='�ʹ� �߿���' and pid='107';
					sql2="select uid from question where storeID=? and depth=? and name=? and pid=?";
					pstmt = conn.prepareStatement(sql2);
					pstmt.setString(1, storeID);
					pstmt.setInt(2, 2); // 2�ܰ��� uid�� �������°ű⶧���� !
					pstmt.setString(3, depthNameTwo);
					pstmt.setString(4, pid);
					rs = pstmt.executeQuery();
					if(rs.next()) {
						pid=rs.getString("uid");
						
						// update question set subject='����', name='1��;2��;3��;4��' where subject='����' and name='1��;2��;3��' and storeID='1' 
						//and depth=3 and pid='114';

						
						//sql4="select * from question where name=? and subject=? and storeID=? and depth=? and pid=?"; // �ߺ��� ������ Ȯ���Ϸ���
						sql4="select * from question where subject=? and name=? and storeID=? and depth=? and pid=?"; // �ߺ��� ������ Ȯ���Ϸ���
						pstmt = conn.prepareStatement(sql4);
						//pstmt.setString(1, content);
						pstmt.setString(1, newTitle);
						pstmt.setString(2, newContent);
						pstmt.setString(3, storeID);
						pstmt.setInt(4, depth1);
						pstmt.setString(5, pid);
						rs = pstmt.executeQuery();
						
						if(rs.next()) {
							result1="����";
						}else {							
							sql3="update question set subject=?, name=? where subject=? and name=? and storeID=? and depth=? and pid=?";
							pstmt = conn.prepareStatement(sql3);
							pstmt.setString(1, newTitle);
							pstmt.setString(2, newContent);
							pstmt.setString(3, oldTitle);
							pstmt.setString(4, oldContent);
							pstmt.setString(5, storeID);
							pstmt.setInt(6, 3);
							pstmt.setString(7, pid);
							result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
							if(result>=1) {// ������Ʈ ���� �� 3�ܰ� �ڵ�ȭ ���̺��� ���� ��������ֱ� ���� !!!
								sql5="select * from question where storeID=? and depth=3 and subject=? and name=?";
								pstmt=conn.prepareStatement(sql5);
								pstmt.setString(1, storeID);
								pstmt.setString(2, newTitle);
								pstmt.setString(3, newContent);
								rs=pstmt.executeQuery();
								//returns+="�ڵ�ȭ ���̺� ���� uid ã��:"+pstmt.toString();
								if(rs.next()) { 
									pid=rs.getString("uid"); // 3�ܰ��� UID	
									HashMap<Integer,String> map2 = new HashMap<>();// 3�ܰ� split ������ �ֱ� ���� ��
									
									List<String> oldList = Arrays.asList(oldContent.split(";")); // ������ 3�ܰ� ���� split
									
									/*for(int j=0; j<oldList.size();j++) { // ���� ���� ���� ��� uid�� ����;ߵ�
										//select * from detail where storeID='J1' and pid='233';
										sql2="select * from detail where storeID=? and pid=? and name=?"; // 
										pstmt=conn.prepareStatement(sql2);
										pstmt.setString(1, storeID);
										pstmt.setString(2, pid);
										pstmt.setString(3, oldList.get(j));
										rs1=pstmt.executeQuery();
										if(rs1.next()) {
											map2.put(rs1.getInt("uid"),oldList.get(j)); // uid���� �׿� ���� ������ ���� �־��ش�.
										}
									}*/
									List<String> newlst = Arrays.asList(rs.getString("name").split(";"));
									cnt=0;
									for(int k=0; k<newlst.size();k++) { 
										sql3="select * from detail where storeID=? and pid=? and name=?"; // ���Ӱ� �־��� ���� name���ν� �����ϴ��� Ȯ���ϱ�
										pstmt=conn.prepareStatement(sql3);
										pstmt.setString(1, storeID);
										pstmt.setString(2, pid);
										pstmt.setString(3, newlst.get(k));
										rs2=pstmt.executeQuery(); // ���� �ְԵǸ� �ִ� ����
										/*if(rs2.next()) {
											for(int s=0; s<newlst.size();s++) { // ���� ���� ������ ���� �� ���뿡 �ȵ��ִ°͵� ������ �־ �ȵ��ִ°͸� �����
												if(!oldList.contains(newlst.get(s))) { // ���ԵǾ����� �ʴٸ�
													sql4="delete from detail where storeID=? and name=? and pid=?";
													pstmt=conn.prepareStatement(sql4);
													pstmt.setString(1,storeID);
													pstmt.setString(2, oldList.get(s));
													pstmt.setString(3, pid);
													rs3=pstmt.executeQuery();
												}
											}
										}*/
										if(!rs2.next()) { // ���� ���� ���ٸ� �־��ִ°� !
											sql5="insert into detail(pid, name, storeID) values(?,?,?)";
											pstmt = conn.prepareStatement(sql5);
											pstmt.setString(1, pid);
											pstmt.setString(2, newlst.get(k));
											pstmt.setString(3, storeID);
											result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
											//returns+=pstmt.toString();
											if(result>=1){
												returns="����";
											}else {
												returns="����";
												
											}
										}else {
											cnt++; // �����Ϸ��� ���� ��� �ִٸ� �� �ֱ⶧���� �߰��� �ʿ䰡 ����
										}
									}
									if(cnt==newlst.size()) { // �� ���Ҵµ� ���Ӱ� ���� ����Ʈ�� ���� �Ȱ��ٸ� ��δ� �ִ°��̿��⶧���� ������
										returns="����";
									}
									
									//sql3="delete from detail where storeID=? and pid=?"; // ������ �ٸ��� �߰��� �� �־ �� ����� �߰�
									//pstmt = conn.prepareStatement(sql3);
									//pstmt.setString(1, storeID);
									//pstmt.setString(2, pid);
									//result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
									
									/*
									if(result>=1) {
										
										for(int i=0; i<newlst.size();i++) {
											sql5="insert into detail(pid, name, storeID) values(?,?,?)";
											pstmt = conn.prepareStatement(sql5);
											pstmt.setString(1, pid);
											pstmt.setString(2, newlst.get(i));
											pstmt.setString(3, storeID);
											result = pstmt.executeUpdate(); // ���� ���� ��, ó���� ���� Ȯ���ϱ�
											//returns+=pstmt.toString();
										}
										if(result>=1){
											returns="����";
										}else {
											returns="����";
										}
									}*/
									
								}
							}
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
	public String deleteDB(String depth, String getID, String title, String content, String depthName, String depthNameTwo) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql="select storeID from manager where managerID=?"; // �ϴ� �ش� ���̵� ���� �Ŵ����� �ð� �ִ� ���� ���̵���� ����´�.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Integer depth1=Integer.valueOf(depth); // ������ �� ������ ���������� ��ȯ
				storeID=rs.getString("storeID"); 
				//select uid from question where storeID='1' and depth=1 and name='�ü�'; 1�ܰ��� uid ���� ã���ֱ� 2�ܰ� uid ã���� ����ϱ� ���� !
				sql2="select uid from question where storeID=? and depth=? and name=?";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, storeID);
				pstmt.setInt(2, 1); // 1�ܰ��� uid�� �������°ű⶧���� !
				pstmt.setString(3, depthName);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					pid=rs.getString("uid"); // 1�ܰ� uid ã��
					//select uid from question where storeID='1' and depth=2 and name='�ʹ� �߿���' and pid='107';
					sql2="select uid from question where storeID=? and depth=? and name=? and pid=?";
					pstmt = conn.prepareStatement(sql2);
					pstmt.setString(1, storeID);
					pstmt.setInt(2, 2); // 2�ܰ��� uid�� �������°ű⶧���� !
					pstmt.setString(3, depthNameTwo);
					pstmt.setString(4, pid);
					rs = pstmt.executeQuery();
					if(rs.next()) {
						pid=rs.getString("uid"); // 2�ܰ� uid ã��
						// delete from question where subject='�����̴�' and name='1��;2��;3��;4��' and storeID='1' and depth=3 and pid='118';
						sql3="delete from question where subject=? and name=? and storeID=? and depth=? and pid=?";
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, title);
						pstmt.setString(2, content);
						pstmt.setString(3, storeID);
						pstmt.setInt(4, depth1);
						pstmt.setString(5, pid);
						result = pstmt.executeUpdate();
						// returns=pstmt.toString();
						// select uid from question where pid='118' and storeID='1' and depth=3;
						

						if(result>=1) {
							returns="����";
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



