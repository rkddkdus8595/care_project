
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB9 {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB9 instance = new ConnectDB9();

	public static ConnectDB9 getInstance() {
		return instance;
	}

	public ConnectDB9() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL 계정 "jdbc:mysql://localhost:3306/DB이름"
	private String dbId = "root"; // MySQL 계정 "로컬일 경우 root"
	private String dbPw = "Lotte1234!"; // 비밀번호 "mysql 설치 시 설정한 비밀번호"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;
	private String sql = "";
	private String sql1="", sql2 = "", sql3="", sql4="", sql5="";
	String returns = "";
	String returns2 = "";
	String result1, storeID, pid;
	Integer result;
	String content="", contentStr="";
	
	// -------------추가--------------
	public String insertDB(String groupName, String groupName2, String storeID, String storeName, String id,  String passwd) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql="select * from store where storeName=?"; // 중복된 점이름인지 확인
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, storeName);
			rs=pstmt.executeQuery();
			returns=pstmt.toString();
			if(!rs.next()) { // 중복된 경우가 아닐경우
				sql1="insert into store values(?,?,?,?)";
				// storeID | storeName | groupName
				pstmt = conn.prepareStatement(sql1);
				pstmt.setString(1, storeID);
				pstmt.setString(2, storeName);
				pstmt.setString(3, groupName);
				pstmt.setString(4, groupName2);
				result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
				returns+=pstmt.toString();
				if(result>=1) { // 점추가에 성공시 매니저 추가를 해준다
					// managerID | passwd   | kind | storeID 
					sql2="insert into manager values(?,?,'N',?)";
					pstmt=conn.prepareStatement(sql2);
					pstmt.setString(1, id);
					pstmt.setString(2, passwd);
					pstmt.setString(3, storeID);
					result=pstmt.executeUpdate();
					returns+=pstmt.toString();
					if(result>=1) {
						result1="성공";
					}
				}
			}else {
				result1="점이름중복";
			}
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return result1;
	}
	
	
}



