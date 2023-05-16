
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB4 {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB4 instance = new ConnectDB4();

	public static ConnectDB4 getInstance() {
		return instance;
	}

	public ConnectDB4() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL 계정 "jdbc:mysql://localhost:3306/DB이름"
	private String dbId = "root"; // MySQL 계정 "로컬일 경우 root"
	private String dbPw = "Lotte1234!"; // 비밀번호 "mysql 설치 시 설정한 비밀번호"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;
	private String sql = "";
	private String sql2 = "", sql3="", sql4="", sql5="", sql6="";
	String returns = "";
	String returns2 = "";
	String result1, storeID;
	Integer result;
	
	// -------------추가--------------
	public String insertDB(String depth, String getID, String content) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select storeID from manager where managerID='main'; 이렇게 스토어 아이디를 먼저 찾아준 후에 insert 진행
			// Integer.valueOf() 이걸로 스트링 -> 인트
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			returns=pstmt.toString();
			//returns="여기까진 오나?";
			if(rs.next()) {
				storeID=rs.getString("storeID"); 
				returns="여기까진 오나? depth : "+depth;

				if(depth.equals("1")) {
					Integer depth1=Integer.valueOf(depth);
					// insert into question(name, storeID, depth) values('주차','1',1);
					sql2="insert into question(name, storeID, depth) values(?,?,?)";
					//select * from question where name='siseol' and storeID='2';
					sql3="select * from question where name=? and storeID=?"; // 중복된 값인지 확인하려고
					pstmt = conn.prepareStatement(sql3);
					pstmt.setString(1, content);
					pstmt.setString(2, storeID);
					rs = pstmt.executeQuery();
					//returns=pstmt.toString();
					if(rs.next()) {
						result1="실패";
					}else {
						pstmt = conn.prepareStatement(sql2);
						pstmt.setString(1, content);
						pstmt.setString(2, storeID);
						pstmt.setInt(3, depth1);
						result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
						//returns=pstmt.toString();
						if(result>=1) {
							result1="성공";
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
	
	// -------------수정--------------
	public String updateDB(String depth, String getID, String oldContent, String newContent) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select storeID from manager where managerID='main'; 이렇게 스토어 아이디를 먼저 찾아준 후에 insert 진행
			// Integer.valueOf() 이걸로 스트링 -> 인트
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				storeID=rs.getString("storeID"); 

				if(depth.equals("1")) {
					Integer depth1=Integer.valueOf(depth);
					// update question set name='안전' where name='시설' and storeID='1' and depth=1;
					sql2="update question set name=? where name=? and storeID=? and depth=?";
					//select * from question where name='siseol' and storeID='2';
					sql3="select * from question where name=? and storeID=?"; // 중복된 값인지 확인하려고
					pstmt = conn.prepareStatement(sql3);
					pstmt.setString(1, newContent);
					pstmt.setString(2, storeID);
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
						returns="실패"; // 중복된 값임
					}else {
						pstmt = conn.prepareStatement(sql2);
						pstmt.setString(1, newContent);
						pstmt.setString(2, oldContent);
						pstmt.setString(3, storeID);
						pstmt.setInt(4, depth1);
						result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
						returns=String.valueOf(result);
						if(result>=1) {
							returns="성공";
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
	
	// -------------삭제--------------
	public String deleteDB(String depth, String getID, String oldContent) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select storeID from manager where managerID='main'; 이렇게 스토어 아이디를 먼저 찾아준 후에 insert 진행
			// Integer.valueOf() 이걸로 스트링 -> 인트
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			result1+="1단계"+pstmt.toString();
			if(rs.next()) {
				storeID=rs.getString("storeID"); 
				if(depth.equals("1")) {
					Integer depth1=Integer.valueOf(depth);
					// select uid from question where name='시설' and storeID='1' and depth=1;
					sql2="select uid from question where name=? and storeID=? and depth=?"; // 해당하는 1단계에 계층적으로 딸려있는애들 다 지우기 위해서 !!!
					pstmt=conn.prepareStatement(sql2);
					pstmt.setString(1, oldContent);
					pstmt.setString(2, storeID);
					pstmt.setInt(3, 1);
					rs=pstmt.executeQuery();
					result1+="2단계"+pstmt.toString();
					if(rs.next()) { // 1단계에 해당하는 uid의 결과는 하나일것이기 때문에 이걸로 돌리고
						String pid=rs.getString("uid");
						// delete from question where name='시설' and storeID='1' and depth=1;
						sql4="delete from question where name=? and storeID=? and depth=?"; // 1단계 먼저지우기
						pstmt = conn.prepareStatement(sql4);
						pstmt.setString(1, oldContent);
						pstmt.setString(2, storeID);
						pstmt.setInt(3, 1);
						result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
						result1+="3단계"+pstmt.toString();
						if(result>=1) { 
							// select uid from question where pid='107' and storeID='1' and depth=2; // 1단계의 자식인 2단계의 uid 찾기
							sql3="select uid from question where pid=? and storeID=? and depth=?";
							pstmt=conn.prepareStatement(sql3);
							pstmt.setString(1, pid);
							pstmt.setString(2, storeID);
							pstmt.setInt(3, 2);
							rs=pstmt.executeQuery();

							result1+="4단계"+pstmt.toString();
							while(rs.next()) { // 2단계뿌터는 여러개일수도 있으니 while로 돌린다.
								
								String pid2=rs.getString("uid");
								// delete from question where name='시설' and storeID='1' and depth=1;
								sql4="delete from question where pid=? and storeID=? and depth=?";
								pstmt = conn.prepareStatement(sql4);
								pstmt.setString(1, pid);
								pstmt.setString(2, storeID);
								pstmt.setInt(3, 2);
								result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기

								result1+="5단계"+pstmt.toString();
								if(result>=1) {
									// select uid from question where pid='118' and storeID='1' and depth=3;
									sql5="select uid from question where pid=? and storeID=? and depth=?"; // 2단계의 자식들 uid 찾기
									pstmt=conn.prepareStatement(sql5);
									pstmt.setString(1, pid2);
									pstmt.setString(2, storeID);
									pstmt.setInt(3, 3);
									rs=pstmt.executeQuery();
									result1+="6단계"+pstmt.toString();
									while(rs.next()) {
										String pid3=rs.getString("uid");
										// delete from question where name='시설' and storeID='1' and depth=1;
										sql6="delete from question where pid=? and storeID=? and depth=?";
										pstmt = conn.prepareStatement(sql6);
										pstmt.setString(1, pid2);
										pstmt.setString(2, storeID);
										pstmt.setInt(3, 3);
										result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기

										result1+="7단계"+pstmt.toString();
										if(result>=1) {
											returns="성공";
										}else {
											returns="성공";
										}
									}
								}else {
									returns="성공"; //2단계가 이미 지워졌을수도 있음
								}
								
								
							}
						}
						
					}else {
						returns="실패";
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



