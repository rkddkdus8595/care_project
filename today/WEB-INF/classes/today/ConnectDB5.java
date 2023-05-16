
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
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB5 instance = new ConnectDB5();

	public static ConnectDB5 getInstance() {
		return instance;
	}

	public ConnectDB5() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL 계정 "jdbc:mysql://localhost:3306/DB이름"
	private String dbId = "root"; // MySQL 계정 "로컬일 경우 root"
	private String dbPw = "Lotte1234!"; // 비밀번호 "mysql 설치 시 설정한 비밀번호"
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
	
	// -------------추가--------------
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
				//returns="여기까진 오나? storeID : "+storeID; 여기는 출력됨
				sql2="select uid from question where storeID=? and depth=? and name=?";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, storeID);
				pstmt.setInt(2, 1); // 1단계의 uid를 가져오는거기때문에 !
				pstmt.setString(3, depthName);
				rs = pstmt.executeQuery();
				//returns="실행되냐 ?"+pstmt.toString();
				if(rs.next()) {
					//returns="여기는 들어오나 ?"+pstmt.toString();
					pid=rs.getString("uid");
					// insert into question(pid, name, storeID, depth) values('107','너무더러워요','1',2);
					sql3="insert into question(pid, name, storeID, depth) values(?,?,?,?)";
					//select * from question where name='너무더러워요' and storeID='1' and depth=2;
					sql4="select * from question where name=? and storeID=? and depth=? and pid=?"; // 중복된 값인지 확인하려고
					
					pstmt = conn.prepareStatement(sql4);
					pstmt.setString(1, content);
					pstmt.setString(2, storeID);
					pstmt.setInt(3, depth1);
					pstmt.setString(4, pid);
					rs = pstmt.executeQuery();
					
					returns=pstmt.toString();
					if(rs.next()) {
						result1="실패";
					}else {
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, pid);
						pstmt.setString(2, content);
						pstmt.setString(3, storeID);
						pstmt.setInt(4, depth1);
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
	public String updateDB(String depth, String getID, String oldContent, String newContent, String depthName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select storeID from manager where managerID='main'; 이렇게 스토어 아이디를 먼저 찾아준 후에 insert 진행
			// Integer.valueOf() 이걸로 스트링 -> 인트
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			//result1=pstmt.toString();
			//returns="여기까진 오나?";
			if(rs.next()) {
				storeID=rs.getString("storeID"); 
				//returns="여기까진 오나? depth : "+depth;
				Integer depth1=Integer.valueOf(depth);

				sql2="select uid from question where storeID=? and depth=? and name=?";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, storeID);
				pstmt.setInt(2, 1); // 1단계의 uid를 가져오는거기때문에 !
				pstmt.setString(3, depthName);
				rs = pstmt.executeQuery();
				//result1="실행되냐 ?"+pstmt.toString();
				
				if(rs.next()) {
					//returns="여기는 들어오나 ?"+pstmt.toString();
					pid=rs.getString("uid");
					
					//select * from question where name='너무더러워요' and storeID='1' and depth=2;
					sql4="select * from question where name=? and storeID=? and depth=? and pid=?"; // 중복된 값인지 확인하려고
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
						returns="실패"; // 중복된 값임
					}else {
						// update question set name='너무추워요' where name='너무 더워요' and storeID='1' and depth=2 and pid=107;
						sql3="update question set name=? where name=? and storeID=? and depth=? and pid=?";
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, newContent);
						pstmt.setString(2, oldContent);
						pstmt.setString(3, storeID);
						pstmt.setInt(4, depth1);
						pstmt.setString(5, pid);
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
	public String deleteDB(String depth, String getID, String oldContent, String depthName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			// select storeID from manager where managerID='main'; 이렇게 스토어 아이디를 먼저 찾아준 후에 insert 진행
			// Integer.valueOf() 이걸로 스트링 -> 인트
			sql="select storeID from manager where managerID=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			//returns=pstmt.toString();
			//returns="여기까진 오나?";
			if(rs.next()) {
				storeID=rs.getString("storeID"); 
				//returns="여기까진 오나? depth : "+depth;
				Integer depth1=Integer.valueOf(depth);

				sql2="select uid from question where storeID=? and depth=? and name=?";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, storeID);
				pstmt.setInt(2, 1); // 1단계의 uid를 가져오는거기때문에 !
				pstmt.setString(3, depthName);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					pid=rs.getString("uid"); // 1단계 uid 가지고 옴
					sql3="select uid from question where storeID=? and depth=? and name=? and pid=?"; // 2단계의 uid를 찾기위해서
					pstmt=conn.prepareStatement(sql3);
					pstmt.setString(1, storeID);
					pstmt.setInt(2, 2);
					pstmt.setString(3, oldContent);
					pstmt.setString(4, pid); // 
					rs=pstmt.executeQuery();
					
					if(rs.next()) {
						pid2=rs.getString("uid"); // 2단계 uid 가지고옴 3단계 삭제에 쓰려고
						// delete from question where name='너무 미끄러워요' and storeID='1' and depth=2 and pid=111;
						sql4="delete from question where name=? and storeID=? and depth=? and pid=?";
						pstmt = conn.prepareStatement(sql4);
						pstmt.setString(1, oldContent);
						pstmt.setString(2, storeID);
						pstmt.setInt(3, depth1);
						pstmt.setString(4, pid);
						result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
						if(result>=1) { // 2단계 삭제에 성공하면 그에 해당하는 3단계도 단계적으로 지워줘야함
							//returns="성공";
							sql4="delete from question where storeID=? and depth=? and pid=?"; // pid가 지워졌다면 자식도 다 지워져야함
							pstmt=conn.prepareStatement(sql4);
							pstmt.setString(1, storeID);
							pstmt.setInt(2, 3);
							pstmt.setString(3, pid2);
							result=pstmt.executeUpdate();
							if(result>=1) {
								returns="성공";
							}else {
								returns="성공"; // 3단계가 먼저 지워졌을수도 있음
							}
						}else {
							returns="실패";
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



