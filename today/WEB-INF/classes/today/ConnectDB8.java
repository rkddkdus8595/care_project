
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB8 {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB8 instance = new ConnectDB8();

	public static ConnectDB8 getInstance() {
		return instance;
	}

	public ConnectDB8() {

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
	public String insertDB(String phoneNum, String depthNameOne, String depthNameTwo, String subjectName, String depthName3, String storeName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql="select storeID from store where storeName=?"; // 먼저 해당 점이름의 점코드를 조회
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, storeName);
			rs=pstmt.executeQuery();
			returns+="0단계로 들어왔음"+pstmt.toString();

			if(rs.next()) {
				String storeID="";
				storeID=rs.getString("storeID"); // 점코드 저장
				
				String[] subject=subjectName.split(","); // 3단계의 값은 여러개일수 있기 때문에 !
				String[] depth3=depthName3.split(",");
				contentStr="";
				sql1="select uid from question where name=? and storeID=? and depth=1"; // 1뎁스의 uid 갖고오기
				pstmt=conn.prepareStatement(sql1);
				pstmt.setString(1, depthNameOne);
				pstmt.setString(2,  storeID);
				rs=pstmt.executeQuery();
				returns+="2:"+pstmt.toString();
				if(rs.next()) {
					String oneUID=rs.getString("uid"); // 1단계의 UID
					
					sql1="select uid from question where name=? and storeID=? and pid=? and depth=2"; // 2뎁스의 uid
					pstmt=conn.prepareStatement(sql1);
					pstmt.setString(1, depthNameTwo);
					pstmt.setString(2, storeID);
					pstmt.setString(3, oneUID);
					rs=pstmt.executeQuery();
					returns+="2:"+pstmt.toString();
					if(rs.next()) {
						String twoUID=rs.getString("uid"); // 2단계의 UID
						//contentStr+=oneUID+","+twoUID+","; // 단계는 ','로 구분하고 3단계 내에서는 ;로 구분한다
						for(int i=0; i<subject.length;i++) {
							//returns+="for 단계로 들어왔음"+depth3[i];
							sql2="select * from detail where storeID=? and name=?"; // 주제만 같은걸 찾아서 uid를 먼저 저장 !!!
							pstmt=conn.prepareStatement(sql2);
							pstmt.setString(1, storeID);
							pstmt.setString(2, depth3[i]);
							rs=pstmt.executeQuery();
							if(rs.next()) {
								contentStr+=rs.getString("uid")+";"; // 코드로 저장하기 위해 uid로 저장해줌
							}
						}
						contentStr = contentStr.substring(0, contentStr.length()-1);
						//returns+=contentStr;
						sql3="insert into receipt(phone, depth1, depth2, depth3, process, processT, date, storeID) values(?,?,?,?,'N',null,sysdate(),?)";
						
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, phoneNum);
						pstmt.setString(2, oneUID);
						pstmt.setString(3, twoUID);
						pstmt.setString(4, contentStr);
						pstmt.setString(5, storeID);
						result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
						returns+=pstmt.toString();
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
	
	
}



