package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB {
	// �̱��� �������� ��� �ϱ��� �� �ڵ��
	private static ConnectDB instance = new ConnectDB();

	public static ConnectDB getInstance() {
		return instance;
	}

	public ConnectDB() {

	}

	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL ���� "jdbc:mysql://localhost:3306/DB�̸�"
	private String dbId = "root"; // MySQL ���� "������ ��� root"
	private String dbPw = "Lotte1234!"; // ��й�ȣ "mysql ��ġ �� ������ ��й�ȣ"
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
				String storeID=rs.getString("storeID"); // ���� �ڵ� ���� ����
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
						returns2 = "false"; // �α��� ����
					}
				}else { // ���۰������� ��쿡�� ������ ������ ����
					if (rs.getString("managerID").equals(id) && rs.getString("passwd").equals(pwd)) {
						if(rs.getString("kind").equals("S")) {
							returns2="S";
						}
					} else {
						returns2 = "false"; // �α��� ����
					}
				}
			} else {
				returns2 = "noId"; // ���̵� �Ǵ� ��й�ȣ ���� X
			}

		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns2;
	}
}


