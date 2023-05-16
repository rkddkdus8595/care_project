
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB2 {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB2 instance = new ConnectDB2();

	public static ConnectDB2 getInstance() {
		return instance;
	}

	public ConnectDB2() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL 계정 "jdbc:mysql://localhost:3306/DB이름"
	private String dbId = "root"; // MySQL 계정 "로컬일 경우 root"
	private String dbPw = "Lotte1234!"; // 비밀번호 "mysql 설치 시 설정한 비밀번호"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = "", sql1="", sql2="", sql3="", sql4="";
	int result=0;
	String result1;
	// -------------관리자--------------
	public String searchDB(String phone, String date, String oneContent, String twoContent, String threeContent) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			result1="";
			// update store set process='Y' where phone='123123' and recept='123123' and date='2020-12-22 16:11:20';
			sql = "update receipt set process='Y', processT=sysdate() where phone=? and date=?"; // 
			// 처리 시간도 적어준다 !
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			pstmt.setString(2, date);
			result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
			
			sql="select processT from receipt where phone=? and date=?"; // 폰번호랑 접수시간 초까지 똑같은 사람이 있을수는 없기 대문에 !!! 내용까지 비교하지는 않음
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			pstmt.setString(2, date);
			rs=pstmt.executeQuery();
			if(result>=1) {
				while(rs.next()) {
					result1+=rs.getString("processT");
				}
			}
			//result1= pstmt.toString();
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return result1;
	}
}



