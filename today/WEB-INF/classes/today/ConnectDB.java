package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB instance = new ConnectDB();

	public static ConnectDB getInstance() {
		return instance;
	}

	public ConnectDB() {

	}

	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL 계정 "jdbc:mysql://localhost:3306/DB이름"
	private String dbId = "root"; // MySQL 계정 "로컬일 경우 root"
	private String dbPw = "Lotte1234!"; // 비밀번호 "mysql 설치 시 설정한 비밀번호"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null, rs1=null;
	private String sql = "";
	private String sql2 = "";
	String returns = "";
	String returns2 = "";


	public String logindb(String id, String pwd) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			//System.out.println(id+pwd);
			returns2="";
			sql = "select * from manager where managerID=? and passwd=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String storeID=rs.getString("storeID"); // 지점 코드 갖고 오기
				sql = "select storeName from store where storeID=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, storeID);
				rs1 = pstmt.executeQuery();
				if(rs1.next()) {
					String storeName=rs1.getString("storeName");
					returns2+=storeName;
					if (rs.getString("managerID").equals(id) && rs.getString("passwd").equals(pwd)) {
						if(rs.getString("kind").equals("N")) {
							returns2+=";"+"N";
						}
					} else {
						returns2 = "false"; // 로그인 실패
					}
				}else { // 슈퍼관리자의 경우에는 지정된 지점이 없음
					if (rs.getString("managerID").equals(id) && rs.getString("passwd").equals(pwd)) {
						if(rs.getString("kind").equals("S")) {
							returns2="S";
						}
					} else {
						returns2 = "false"; // 로그인 실패
					}
				}
			} else {
				returns2 = "noId"; // 아이디 또는 비밀번호 존재 X
			}

		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns2;
	}
}


