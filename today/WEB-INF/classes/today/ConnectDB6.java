
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
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB6 instance = new ConnectDB6();

	public static ConnectDB6 getInstance() {
		return instance;
	}

	public ConnectDB6() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL 계정 "jdbc:mysql://localhost:3306/DB이름"
	private String dbId = "root"; // MySQL 계정 "로컬일 경우 root"
	private String dbPw = "Lotte1234!"; // 비밀번호 "mysql 설치 시 설정한 비밀번호"
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
	
	// -------------추가--------------
	public String insertDB(String depth, String getID, String content, String depthName, String depthNameTwo, String subject) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql="select storeID from manager where managerID=?"; // 일단 해당 아이디를 가진 매니저가 맡고 있는 점의 아이디부터 갖고온다.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Integer depth1=Integer.valueOf(depth); // 가지고 온 뎁스를 정수형으로 변환
				storeID=rs.getString("storeID"); 
				//select uid from question where storeID='1' and depth=1 and name='시설'; 1단계의 uid 먼저 찾아주기 2단계 uid 찾을때 사용하기 위해 !
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
					//select uid from question where storeID='1' and depth=2 and name='너무 추워요' and pid='107';
					sql2="select uid from question where storeID=? and depth=? and name=? and pid=?";
					pstmt = conn.prepareStatement(sql2);
					pstmt.setString(1, storeID);
					pstmt.setInt(2, 2); // 2단계의 uid를 가져오는거기때문에 !
					pstmt.setString(3, depthNameTwo);
					pstmt.setString(4, pid);
					rs = pstmt.executeQuery();
					if(rs.next()) {
						pid=rs.getString("uid");
						
						// select * from question where name='3층' and subject='층수' and storeID='1' and depth=3 and pid='111';
						//sql4="select * from question where name=? and subject=? and storeID=? and depth=? and pid=?"; // 중복된 값인지 확인하려고
						sql4="select * from question where subject=? and storeID=? and depth=? and pid=?"; // 중복된 값인지 확인하려고
						pstmt = conn.prepareStatement(sql4);
						//pstmt.setString(1, content);
						pstmt.setString(1, subject);
						pstmt.setString(2, storeID);
						pstmt.setInt(3, depth1);
						pstmt.setString(4, pid);
						rs = pstmt.executeQuery();
						
						//returns2=pstmt.toString();
						if(rs.next()) {
							result1="실패";
						}else {
							sql3="insert into question(pid, subject, name, storeID, depth) values(?,?,?,?,?)";
							pstmt = conn.prepareStatement(sql3);
							pstmt.setString(1, pid);
							pstmt.setString(2, subject);
							pstmt.setString(3, content);
							pstmt.setString(4, storeID);
							pstmt.setInt(5, depth1);
							result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
							if(result>=1) {
								sql4="select uid from question where storeID=? and depth=3 and subject=? and name=?";
								pstmt=conn.prepareStatement(sql4);
								pstmt.setString(1, storeID);
								pstmt.setString(2, subject);
								pstmt.setString(3, content);
								rs=pstmt.executeQuery();
								if(rs.next()) {
									//returns="여기는 들어오나 ?"+pid;
									pid=rs.getString("uid");
									String[] str=content.split(";"); // 넣을때 쪼개서 넣으려고
									for(int i=0; i<str.length;i++) { // 하나하나 코드화로 만들어주기 위해 따로 만들어둔 테이블에 또 넣어준다.
										// insert into question(pid, subject, name, storeID, depth) values('111','층수','3층','1',3);
										sql5="insert into detail(pid, name, storeID) values(?,?,?)";
										pstmt = conn.prepareStatement(sql5);
										pstmt.setString(1, pid);
										pstmt.setString(2, str[i]);
										pstmt.setString(3, storeID);
										result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
										returns+=pstmt.toString();
										if(result>=1) {
											result1="성공";
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
	
	// -------------수정--------------
	public String updateDB(String depth, String getID, String oldTitle, String newTitle, String oldContent, 
			String depthName, String depthNameTwo, String newContent) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql="select storeID from manager where managerID=?"; // 일단 해당 아이디를 가진 매니저가 맡고 있는 점의 아이디부터 갖고온다.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Integer depth1=Integer.valueOf(depth); // 가지고 온 뎁스를 정수형으로 변환
				storeID=rs.getString("storeID"); 
				//select uid from question where storeID='1' and depth=1 and name='시설'; 1단계의 uid 먼저 찾아주기 2단계 uid 찾을때 사용하기 위해 !
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
					//select uid from question where storeID='1' and depth=2 and name='너무 추워요' and pid='107';
					sql2="select uid from question where storeID=? and depth=? and name=? and pid=?";
					pstmt = conn.prepareStatement(sql2);
					pstmt.setString(1, storeID);
					pstmt.setInt(2, 2); // 2단계의 uid를 가져오는거기때문에 !
					pstmt.setString(3, depthNameTwo);
					pstmt.setString(4, pid);
					rs = pstmt.executeQuery();
					if(rs.next()) {
						pid=rs.getString("uid");
						
						// update question set subject='층수', name='1층;2층;3층;4층' where subject='층수' and name='1층;2층;3층' and storeID='1' 
						//and depth=3 and pid='114';

						
						//sql4="select * from question where name=? and subject=? and storeID=? and depth=? and pid=?"; // 중복된 값인지 확인하려고
						sql4="select * from question where subject=? and name=? and storeID=? and depth=? and pid=?"; // 중복된 값인지 확인하려고
						pstmt = conn.prepareStatement(sql4);
						//pstmt.setString(1, content);
						pstmt.setString(1, newTitle);
						pstmt.setString(2, newContent);
						pstmt.setString(3, storeID);
						pstmt.setInt(4, depth1);
						pstmt.setString(5, pid);
						rs = pstmt.executeQuery();
						
						if(rs.next()) {
							result1="실패";
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
							result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
							if(result>=1) {// 업데이트 성공 후 3단계 코드화 테이블의 값도 변경시켜주기 위해 !!!
								sql5="select * from question where storeID=? and depth=3 and subject=? and name=?";
								pstmt=conn.prepareStatement(sql5);
								pstmt.setString(1, storeID);
								pstmt.setString(2, newTitle);
								pstmt.setString(3, newContent);
								rs=pstmt.executeQuery();
								//returns+="코드화 테이블 위한 uid 찾기:"+pstmt.toString();
								if(rs.next()) { 
									pid=rs.getString("uid"); // 3단계의 UID	
									HashMap<Integer,String> map2 = new HashMap<>();// 3단계 split 내용을 넣기 위한 곳
									
									List<String> oldList = Arrays.asList(oldContent.split(";")); // 수정전 3단계 내용 split
									
									/*for(int j=0; j<oldList.size();j++) { // 삭제 전에 먼저 얘네 uid를 갖고와야됨
										//select * from detail where storeID='J1' and pid='233';
										sql2="select * from detail where storeID=? and pid=? and name=?"; // 
										pstmt=conn.prepareStatement(sql2);
										pstmt.setString(1, storeID);
										pstmt.setString(2, pid);
										pstmt.setString(3, oldList.get(j));
										rs1=pstmt.executeQuery();
										if(rs1.next()) {
											map2.put(rs1.getInt("uid"),oldList.get(j)); // uid값과 그에 대한 내용을 같이 넣어준다.
										}
									}*/
									List<String> newlst = Arrays.asList(rs.getString("name").split(";"));
									cnt=0;
									for(int k=0; k<newlst.size();k++) { 
										sql3="select * from detail where storeID=? and pid=? and name=?"; // 새롭게 넣어줄 값이 name으로써 존재하는지 확인하기
										pstmt=conn.prepareStatement(sql3);
										pstmt.setString(1, storeID);
										pstmt.setString(2, pid);
										pstmt.setString(3, newlst.get(k));
										rs2=pstmt.executeQuery(); // 값이 있게되면 있는 거임
										/*if(rs2.next()) {
											for(int s=0; s<newlst.size();s++) { // 만약 원래 내용이 새로 들어갈 내용에 안들어가있는것도 있을수 있어서 안들어가있는것만 지우기
												if(!oldList.contains(newlst.get(s))) { // 포함되어있지 않다면
													sql4="delete from detail where storeID=? and name=? and pid=?";
													pstmt=conn.prepareStatement(sql4);
													pstmt.setString(1,storeID);
													pstmt.setString(2, oldList.get(s));
													pstmt.setString(3, pid);
													rs3=pstmt.executeQuery();
												}
											}
										}*/
										if(!rs2.next()) { // 만약 값이 없다면 넣어주는거 !
											sql5="insert into detail(pid, name, storeID) values(?,?,?)";
											pstmt = conn.prepareStatement(sql5);
											pstmt.setString(1, pid);
											pstmt.setString(2, newlst.get(k));
											pstmt.setString(3, storeID);
											result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
											//returns+=pstmt.toString();
											if(result>=1){
												returns="성공";
											}else {
												returns="실패";
												
											}
										}else {
											cnt++; // 수정하려는 값이 모두 있다면 다 있기때문에 추가할 필요가 없음
										}
									}
									if(cnt==newlst.size()) { // 다 돌았는데 새롭게 넣은 리스트랑 값이 똑같다면 모두다 있는값이엿기때문에 성공임
										returns="성공";
									}
									
									//sql3="delete from detail where storeID=? and pid=?"; // 개수가 다른게 추가될 수 있어서 싹 지우고 추가
									//pstmt = conn.prepareStatement(sql3);
									//pstmt.setString(1, storeID);
									//pstmt.setString(2, pid);
									//result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
									
									/*
									if(result>=1) {
										
										for(int i=0; i<newlst.size();i++) {
											sql5="insert into detail(pid, name, storeID) values(?,?,?)";
											pstmt = conn.prepareStatement(sql5);
											pstmt.setString(1, pid);
											pstmt.setString(2, newlst.get(i));
											pstmt.setString(3, storeID);
											result = pstmt.executeUpdate(); // 쿼리 실행 후, 처리된 개수 확인하기
											//returns+=pstmt.toString();
										}
										if(result>=1){
											returns="성공";
										}else {
											returns="실패";
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
	
	// -------------삭제--------------
	public String deleteDB(String depth, String getID, String title, String content, String depthName, String depthNameTwo) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql="select storeID from manager where managerID=?"; // 일단 해당 아이디를 가진 매니저가 맡고 있는 점의 아이디부터 갖고온다.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Integer depth1=Integer.valueOf(depth); // 가지고 온 뎁스를 정수형으로 변환
				storeID=rs.getString("storeID"); 
				//select uid from question where storeID='1' and depth=1 and name='시설'; 1단계의 uid 먼저 찾아주기 2단계 uid 찾을때 사용하기 위해 !
				sql2="select uid from question where storeID=? and depth=? and name=?";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setString(1, storeID);
				pstmt.setInt(2, 1); // 1단계의 uid를 가져오는거기때문에 !
				pstmt.setString(3, depthName);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					pid=rs.getString("uid"); // 1단계 uid 찾음
					//select uid from question where storeID='1' and depth=2 and name='너무 추워요' and pid='107';
					sql2="select uid from question where storeID=? and depth=? and name=? and pid=?";
					pstmt = conn.prepareStatement(sql2);
					pstmt.setString(1, storeID);
					pstmt.setInt(2, 2); // 2단계의 uid를 가져오는거기때문에 !
					pstmt.setString(3, depthNameTwo);
					pstmt.setString(4, pid);
					rs = pstmt.executeQuery();
					if(rs.next()) {
						pid=rs.getString("uid"); // 2단계 uid 찾음
						// delete from question where subject='층수이다' and name='1층;2층;3층;4층' and storeID='1' and depth=3 and pid='118';
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
							returns="성공";
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



