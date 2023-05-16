
package today;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ConnectDB1 {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB1 instance = new ConnectDB1();

	public static ConnectDB1 getInstance() {
		return instance;
	}

	public ConnectDB1() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL 계정 "jdbc:mysql://localhost:3306/DB이름"
	private String dbId = "root"; // MySQL 계정 "로컬일 경우 root"
	private String dbPw = "Lotte1234!"; // 비밀번호 "mysql 설치 시 설정한 비밀번호"
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null, rs1=null, rs2=null, rs3=null, rs4=null;
	private String sql = "";
	private String sql1="", sql2 = "", sql3="", sql4="", sql5="";
	String returns = "";
	String returns2 = "", storeID="", returnTest="", depth1Name="", depth2Name="", depth3Name="";
	JSONObject jsonMain=new JSONObject();
	String sum1,sum2;
	JSONArray jArray;
	JSONObject jObject;
	
	// -------------일반 관리자--------------
	public String searchDB1(String year1, String month1, String day1, String year2, String month2, String day2, String getID, String tabPosition) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			//select * from store where DATE(date) between DATE('2020-12-23') AND DATE('2020-12-31') 
			//AND storeID=(select storeID from manager where managerID='main123');
			//조회할때 위처럼 이렇게 해줘야 해당 매니저가 속해있는 곳의 정보만 나옴 !!!!!
			//select * from store where DATE(date) between DATE('2020-12-01') and DATE('2020-12-31') 
			//and process='Y' and storeID=(select storeID from manager where managerID='main123');

			if(tabPosition.equals("전체")) {
				sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND storeID=(select storeID from manager where managerID=?)";
				returns2=tabPosition+"/전체이다";
			}else if(tabPosition.equals("미처리")) {
				sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='N' AND storeID=(select storeID from manager where managerID=?)";
				returns2=tabPosition+"/미처리다";
			}else if(tabPosition.equals("처리완료")) {
				sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='Y' AND storeID=(select storeID from manager where managerID=?)";
				returns2=tabPosition+"/처리완료다";
			}
			pstmt = conn.prepareStatement(sql);
			sum1=year1+"-"+month1+"-"+day1;
			sum2=year2+"-"+month2+"-"+day2;
			//System.out.println(sum1+" "+sum2);
			pstmt.setString(1, sum1);
			pstmt.setString(2, sum2);
			pstmt.setString(3, getID);
			rs = pstmt.executeQuery();
			int i=0;
			//returns=Integer.toString(rs.getRow());
			JSONObject jsonMain = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject;
			returns="";
			while(rs.next()) { 
				//returns+="들어오긴했음";
				//returns+=rs.getString("date")+"|";
				//returns+=rs.getString("recept")+"|";
				//returns+=rs.getString("process")+"|";
				//returns+=rs.getString("processT")+"\n"; // 구분자로 구분
				depth3Name="";
				String date=rs.getString("date");
				String process=rs.getString("process");
				String processT=rs.getString("processT");
				String phone=rs.getString("phone");
				String depth1=rs.getString("depth1");
				String depth2=rs.getString("depth2");
				String[] depth3=rs.getString("depth3").split(";"); // 3단계 내에는 답변이 여러개이므로 ;로 잘려져 있음
				storeID=rs.getString("storeID");
				jsonObject= new JSONObject(); // 이렇게 하니까 중복 돼서 안들어가고 잘됨 ,,,
				for(int j=0; j<depth3.length;j++) { // 3단계 이름 찾는 for문
					//str2=Arrays.asList(depth3[j].split(";")); // 3단계 발견했으니 쪼개기
					sql2="select name from detail where uid=? and storeID=?";
					pstmt=conn.prepareStatement(sql2);
					pstmt.setString(1, depth3[j]);
					pstmt.setString(2, storeID);
					rs2=pstmt.executeQuery();
					returnTest+="3단계 찾는쪽"+pstmt.toString();
					if(rs2.next()) {
						depth3Name+=rs2.getString("name")+",";
					}else {
						depth3Name+="내용이 없습니다."; // 내용이 없는걸 대비해야함 !
					}
				}
				sql3="select name from question where uid=? and storeID=? and depth=1"; // 1뎁스 이름 찾아오기
				pstmt = conn.prepareStatement(sql3);
				pstmt.setString(1, depth1);
				pstmt.setString(2, storeID);
				rs3 = pstmt.executeQuery();
				
				if(rs3.next()) {
					String oneName=rs3.getString("name");
					
					sql4="select name from question where uid=? and storeID=? and depth=2"; // 2뎁스 이름 찾아오기\
					pstmt = conn.prepareStatement(sql4);
					pstmt.setString(1, depth2);
					pstmt.setString(2, storeID);
					rs4 = pstmt.executeQuery();
					returnTest+="2단계 이름 찾는쪽"+pstmt.toString();
					if(rs4.next()) {
						String twoName=rs4.getString("name");
						
						depth3Name = depth3Name.substring(0, depth3Name.length()-1); // 맨뒤에 ,표 없애고 넣으려고
						returnTest+="마지막단계 들어오 ㅁ?"+oneName+twoName+depth3Name;
						jsonObject=new JSONObject();
						jsonObject.put("date", date); 
						jsonObject.put("oneContent",oneName);
						jsonObject.put("twoContent", twoName);
						jsonObject.put("threeContent", depth3Name);
						jsonObject.put("process",process);
						jsonObject.put("processT",processT);
						jsonObject.put("phone",phone); // 처리 완료 할때 이용하려고 가지고 감
						//returns+=jsonObject.toString()+"다음거"; //차례대로 가져오고 있는것 확인함
						jsonArray.add(jsonObject);
					}
				}
				
				/*-------------------- 원래거 --------------
				jsonObject= new JSONObject(); // 이렇게 하니까 중복 돼서 안들어가고 잘됨 ,,,
				//returns+=jsonObject.put("date", rs.getString("date")).toString(); // 으음,,,,출력되는데,,?
				jsonObject.put("date", rs.getString("date")); 
				jsonObject.put("content",rs.getString("content"));
				jsonObject.put("process",rs.getString("process"));
				jsonObject.put("processT",rs.getString("processT"));
				jsonObject.put("phone",rs.getString("phone")); // 처리 완료 할때 이용하려고 가지고 감
				jsonArray.add(jsonObject);*/
				
			}
			jsonMain.put("receipt", jsonArray);
			returns+=jsonMain.toString(); //제대로 들어가고 있는것 확인함 
			//{"manager":[{"date":"2020-12-27 23:25:41","process":"N","processT":"2020-12-27 23:25:41","recept":"133133"}]} 그러면 스트링으로 보내보쟝
			// 스트링으로 보내니까 됨 ! ! !
			//returns=pstmt.toString();
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	// -------------슈퍼 관리자--------------  // 점을 골라서 볼 경우
		public String searchDB2(String year1, String month1, String day1, String year2, String month2, String day2, String getID, 
				String storeName, String tabPosition) { 
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

				returnTest="서치디비2로 들어왔어";
				sql="select storeID from store where storeName=?";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, storeName);
				rs=pstmt.executeQuery();
				if(tabPosition.equals("전체")) {
					sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND storeID=(select storeID from store where storeName=?)";

				}else if(tabPosition.equals("미처리")) {
					sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='N' AND storeID=(select storeID from store where storeName=?)";
				}else if(tabPosition.equals("처리완료")) {
					sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='Y' AND storeID=(select storeID from store where storeName=?)";

				}
				pstmt = conn.prepareStatement(sql);
				sum1=year1+"-"+month1+"-"+day1;
				sum2=year2+"-"+month2+"-"+day2;
				//System.out.println(sum1+" "+sum2);
				pstmt.setString(1, sum1);
				pstmt.setString(2, sum2);
				pstmt.setString(3, storeName);
				rs = pstmt.executeQuery();
				returnTest+=pstmt.toString();

				int i=0;
				//returns=Integer.toString(rs.getRow());
				JSONObject jsonMain = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject;
				returns="";
				while(rs.next()) { 
					depth3Name="";
					String date=rs.getString("date");
					String process=rs.getString("process");
					String processT=rs.getString("processT");
					String phone=rs.getString("phone");
					String depth1=rs.getString("depth1");
					String depth2=rs.getString("depth2");
					String[] depth3=rs.getString("depth3").split(";"); // 3단계 내에는 답변이 여러개이므로 ;로 잘려져 있음
					storeID=rs.getString("storeID");
					jsonObject= new JSONObject(); // 이렇게 하니까 중복 돼서 안들어가고 잘됨 ,,,
					for(int j=0; j<depth3.length;j++) { // 3단계 이름 찾는 for문
						//str2=Arrays.asList(depth3[j].split(";")); // 3단계 발견했으니 쪼개기
						sql2="select name from detail where uid=? and storeID=?";
						pstmt=conn.prepareStatement(sql2);
						pstmt.setString(1, depth3[j]);
						pstmt.setString(2, storeID);
						rs2=pstmt.executeQuery();
						returnTest+="3단계 찾는쪽"+pstmt.toString();
						if(rs2.next()) {
							depth3Name+=rs2.getString("name")+",";
						}else {
							depth3Name+="내용이 없습니다."; // 내용이 없는걸 대비해야함 !
						}
					}
					sql3="select name from question where uid=? and storeID=? and depth=1"; // 1뎁스 이름 찾아오기
					pstmt = conn.prepareStatement(sql3);
					pstmt.setString(1, depth1);
					pstmt.setString(2, storeID);
					rs3 = pstmt.executeQuery();
					
					if(rs3.next()) {
						String oneName=rs3.getString("name");
						
						sql4="select name from question where uid=? and storeID=? and depth=2"; // 2뎁스 이름 찾아오기\
						pstmt = conn.prepareStatement(sql4);
						pstmt.setString(1, depth2);
						pstmt.setString(2, storeID);
						rs4 = pstmt.executeQuery();
						returnTest+="2단계 이름 찾는쪽"+pstmt.toString();
						if(rs4.next()) {
							String twoName=rs4.getString("name");
							
							depth3Name = depth3Name.substring(0, depth3Name.length()-1); // 맨뒤에 ,표 없애고 넣으려고
							returnTest+="마지막단계 들어오 ㅁ?"+oneName+twoName+depth3Name;
							jsonObject=new JSONObject();
							jsonObject.put("date", date); 
							jsonObject.put("oneContent",oneName);
							jsonObject.put("twoContent", twoName);
							jsonObject.put("threeContent", depth3Name);
							jsonObject.put("process",process);
							jsonObject.put("processT",processT);
							jsonObject.put("phone",phone); // 처리 완료 할때 이용하려고 가지고 감
							//returns+=jsonObject.toString()+"다음거"; //차례대로 가져오고 있는것 확인함
							jsonArray.add(jsonObject);
						}
					}						
				}
				jsonMain.put("receipt", jsonArray);
				returns+=jsonMain.toString();
			} catch (Exception e) {

			} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
					if (rs2 != null)try {rs2.close();} catch (SQLException ex) {}
					if (rs3 != null)try {rs3.close();} catch (SQLException ex) {}
					if (rs4 != null)try {rs4.close();} catch (SQLException ex) {}
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
				if (conn != null)try {conn.close();} catch (SQLException ex) {}
			}
			return returns;
		}
		
		// -------------슈퍼 관리자--------------  // 점을 모두 볼경우
		public String searchDB3(String year1, String month1, String day1, String year2, String month2, String day2, String getID, 
				String storeName, String tabPosition) { // 슈퍼관리자는 모든 점을 다 볼 수 있기 때문에 !!!
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

				returnTest="서치디비3로 들어왔고";
				if(tabPosition.equals("전체")) {
					sql="select * from receipt where DATE(date) between DATE(?) AND DATE(?)";
				}else if(tabPosition.equals("미처리")) {
					sql="select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='N'";
					
				}else if(tabPosition.equals("처리완료")) {
					sql="select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='Y'";
				}
				returnTest+="탭포지션 다음";

				pstmt = conn.prepareStatement(sql);
				sum1=year1+"-"+month1+"-"+day1;
				sum2=year2+"-"+month2+"-"+day2;
				
				pstmt.setString(1, sum1);
				pstmt.setString(2, sum2);
				rs = pstmt.executeQuery();
				returnTest+=pstmt.toString();
				
				int i=0;
				
				JSONObject jsonMain = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject;
				returns="";
				while(rs.next()) { 
					depth3Name="";
					String date=rs.getString("date");
					String process=rs.getString("process");
					String processT=rs.getString("processT");
					String phone=rs.getString("phone");
					String depth1=rs.getString("depth1");
					String depth2=rs.getString("depth2");
					String[] depth3=rs.getString("depth3").split(";"); // 3단계 내에는 답변이 여러개이므로 ;로 잘려져 있음
					storeID=rs.getString("storeID");
					
					sql2="select storeName from store where storeID=?";
					pstmt=conn.prepareStatement(sql2);
					pstmt.setString(1, storeID);
					rs1=pstmt.executeQuery();
					
					if(rs1.next()) {
						String storeName123=rs1.getString("storeName");
						
						jsonObject= new JSONObject(); // 이렇게 하니까 중복 돼서 안들어가고 잘됨 ,,,
						for(int j=0; j<depth3.length;j++) { // 3단계 이름 찾는 for문
							//str2=Arrays.asList(depth3[j].split(";")); // 3단계 발견했으니 쪼개기
							sql2="select name from detail where uid=? and storeID=?";
							pstmt=conn.prepareStatement(sql2);
							pstmt.setString(1, depth3[j]);
							pstmt.setString(2, storeID);
							rs2=pstmt.executeQuery();
							returnTest+="3단계 찾는쪽"+pstmt.toString();
							if(rs2.next()) {
								depth3Name+=rs2.getString("name")+",";
							}else {
								depth3Name+="내용이 없습니다."; // 내용이 없는걸 대비해야함 !
							}
						}
						sql3="select name from question where uid=? and storeID=? and depth=1"; // 1뎁스 이름 찾아오기
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, depth1);
						pstmt.setString(2, storeID);
						rs3 = pstmt.executeQuery();
						
						if(rs3.next()) {
							String oneName=rs3.getString("name");
							
							sql4="select name from question where uid=? and storeID=? and depth=2"; // 2뎁스 이름 찾아오기\
							pstmt = conn.prepareStatement(sql4);
							pstmt.setString(1, depth2);
							pstmt.setString(2, storeID);
							rs4 = pstmt.executeQuery();
							returnTest+="2단계 이름 찾는쪽"+pstmt.toString();
							if(rs4.next()) {
								String twoName=rs4.getString("name");
								
								depth3Name = depth3Name.substring(0, depth3Name.length()-1); // 맨뒤에 ,표 없애고 넣으려고
								returnTest+="마지막단계 들어오 ㅁ?"+oneName+twoName+depth3Name;
								jsonObject=new JSONObject();
								jsonObject.put("storeName", storeName123);
								jsonObject.put("date", date); 
								jsonObject.put("oneContent",oneName);
								jsonObject.put("twoContent", twoName);
								jsonObject.put("threeContent", depth3Name);
								jsonObject.put("process",process);
								jsonObject.put("processT",processT);
								jsonObject.put("phone",phone); // 처리 완료 할때 이용하려고 가지고 감
								//returns+=jsonObject.toString()+"다음거"; //차례대로 가져오고 있는것 확인함
								jsonArray.add(jsonObject);
							}
						}
					}
											
				}
				jsonMain.put("receipt", jsonArray);
				returns2+=jsonMain.toString();
				returns+=jsonMain.toString();
			} catch (Exception e) {

			} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
					if (rs2 != null)try {rs2.close();} catch (SQLException ex) {}
					if (rs3 != null)try {rs3.close();} catch (SQLException ex) {}
					if (rs4 != null)try {rs4.close();} catch (SQLException ex) {}
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
				if (conn != null)try {conn.close();} catch (SQLException ex) {}
			}
			return returns;
		}
				
		
		
	
	// -------------사용자--------------
		public String searchDB4(String year1, String month1, String day1, String year2, String month2, String day2, 
				String getID, String storeName, String tabPosition) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
				returns2="";
				
				sql="select storeID from store where storeName=?";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, storeName);
				rs=pstmt.executeQuery();
				
				if(rs.next()) {
					storeID=rs.getString("storeID");
					if(tabPosition.equals("전체")) {
						sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=? AND"
								+ " storeID=(select storeID from store where storeName=?)";
						//returns=tabPosition+"/전체이다";
					}else if(tabPosition.equals("미처리")) {
						sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=? AND"
								+ " process='N' AND storeID=(select storeID from store where storeName=?)";
						//returns=tabPosition+"/미처리다";
					}else if(tabPosition.equals("처리완료")) {
						sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=? AND"
								+ " process='Y' AND storeID=(select storeID from store where storeName=?)";					
						//returns=tabPosition+"/처리완료다";
					}
					//returns+="sql문"+sql1;

					JSONObject jsonMain = new JSONObject();
					JSONArray jsonArray = new JSONArray();
					JSONObject jsonObject;
					
					pstmt = conn.prepareStatement(sql1);
					sum1=year1+"-"+month1+"-"+day1;
					sum2=year2+"-"+month2+"-"+day2;
					pstmt.setString(1, sum1);
					pstmt.setString(2, sum2);
					pstmt.setString(3, getID);
					pstmt.setString(4, storeName);
					rs = pstmt.executeQuery();
					returnTest+="두번째 단계:"+pstmt.toString()+"/";
					//returns+="탭포지션:"+pstmt.toString();
					int i=0;
					returns="";
					while(rs.next()) { 
						returnTest+="와일문은 들어옴";
						depth3Name="";
						String date=rs.getString("date");
						String process=rs.getString("process");
						String processT=rs.getString("processT");
						String phone=rs.getString("phone");
						String depth1=rs.getString("depth1");
						String depth2=rs.getString("depth2");
						String[] depth3=rs.getString("depth3").split(";"); // 3단계 내에는 답변이 여러개이므로 ;로 잘려져 있음
						returnTest+="여기는 온다";
						jsonObject= new JSONObject(); // 이렇게 하니까 중복 돼서 안들어가고 잘됨 ,,,
						returnTest+="값 테스트"+date+"/"+process+"/"+processT+"/"+phone+"/"+depth1+"/"+depth2+"/"+Arrays.toString(depth3);
						for(int j=0; j<depth3.length;j++) { // 3단계 이름 찾는 for문
							//str2=Arrays.asList(depth3[j].split(";")); // 3단계 발견했으니 쪼개기
							sql2="select name from detail where uid=? and storeID=?";
							pstmt=conn.prepareStatement(sql2);
							pstmt.setString(1, depth3[j]);
							pstmt.setString(2, storeID);
							rs2=pstmt.executeQuery();
							returnTest+="3단계 찾는쪽"+pstmt.toString();
							if(rs2.next()) {
								depth3Name+=rs2.getString("name")+",";
							}else {
								depth3Name+="내용이 없습니다."; // 내용이 없는걸 대비해야함 !
							}
						}
						sql3="select name from question where uid=? and storeID=? and depth=1"; // 1뎁스 이름 찾아오기
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, depth1);
						pstmt.setString(2, storeID);
						rs3 = pstmt.executeQuery();
						returnTest+="1단계 이름 찾는쪽"+pstmt.toString();
						if(rs3.next()) {
							String oneName=rs3.getString("name");
							
							sql4="select name from question where uid=? and storeID=? and depth=2"; // 2뎁스 이름 찾아오기\
							pstmt = conn.prepareStatement(sql4);
							pstmt.setString(1, depth2);
							pstmt.setString(2, storeID);
							rs4 = pstmt.executeQuery();
							returnTest+="2단계 이름 찾는쪽"+pstmt.toString();
							if(rs4.next()) {
								String twoName=rs4.getString("name");
								
								depth3Name = depth3Name.substring(0, depth3Name.length()-1); // 맨뒤에 ,표 없애고 넣으려고
								returnTest+="마지막단계 들어오 ㅁ?"+oneName+twoName+depth3Name;
								jsonObject=new JSONObject();
								jsonObject.put("date", date); 
								jsonObject.put("oneContent",oneName);
								jsonObject.put("twoContent", twoName);
								jsonObject.put("threeContent", depth3Name);
								jsonObject.put("process",process);
								jsonObject.put("processT",processT);
								jsonObject.put("phone",phone); // 처리 완료 할때 이용하려고 가지고 감
								//returns+=jsonObject.toString()+"다음거"; //차례대로 가져오고 있는것 확인함
								jsonArray.add(jsonObject);
							}
						}
						
						
					}
					jsonMain.put("receipt", jsonArray);
					returns+=jsonMain.toString(); //제대로 들어가고 있는것 확인함 
					//{"manager":[{"date":"2020-12-27 23:25:41","process":"N","processT":"2020-12-27 23:25:41","recept":"133133"}]} 그러면 스트링으로 보내보쟝
					// 스트링으로 보내니까 됨 ! ! !
					//returns=pstmt.toString();
				}
				
			} catch (Exception e) {

			} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
					if (rs2 != null)try {rs2.close();} catch (SQLException ex) {}
					if (rs3 != null)try {rs3.close();} catch (SQLException ex) {}
					if (rs4 != null)try {rs4.close();} catch (SQLException ex) {}
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
				if (conn != null)try {conn.close();} catch (SQLException ex) {}
			}
			return returns;
		}
		
		// -------------사용자--------------  // 점을 모두 볼경우
		public String searchDB5(String year1, String month1, String day1, String year2, String month2, String day2, String getID, 
				String storeName, String tabPosition) { 
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

				returnTest="서치디비3로 들어왔고";
				if(tabPosition.equals("전체")) {
					sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=?";
					//returns=tabPosition+"/전체이다";
				}else if(tabPosition.equals("미처리")) {
					sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=? AND"
							+ " process='N'";
					//returns=tabPosition+"/미처리다";
				}else if(tabPosition.equals("처리완료")) {
					sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=? AND"
							+ " process='Y'";					
					//returns=tabPosition+"/처리완료다";
				}
				returnTest+="탭포지션 다음";

				pstmt = conn.prepareStatement(sql1);
				sum1=year1+"-"+month1+"-"+day1;
				sum2=year2+"-"+month2+"-"+day2;
				
				pstmt.setString(1, sum1);
				pstmt.setString(2, sum2);
				pstmt.setString(3, getID);
				rs = pstmt.executeQuery();
				returnTest+=pstmt.toString();
				
				int i=0;
				
				JSONObject jsonMain = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject;
				returns="";
				while(rs.next()) { 
					depth3Name="";
					String date=rs.getString("date");
					String process=rs.getString("process");
					String processT=rs.getString("processT");
					String phone=rs.getString("phone");
					String depth1=rs.getString("depth1");
					String depth2=rs.getString("depth2");
					String[] depth3=rs.getString("depth3").split(";"); // 3단계 내에는 답변이 여러개이므로 ;로 잘려져 있음
					storeID=rs.getString("storeID");
					
					sql2="select storeName from store where storeID=?";
					pstmt=conn.prepareStatement(sql2);
					pstmt.setString(1, storeID);
					rs1=pstmt.executeQuery();
					
					if(rs1.next()) {
						String storeName123=rs1.getString("storeName");
						jsonObject= new JSONObject(); // 이렇게 하니까 중복 돼서 안들어가고 잘됨 ,,,
						for(int j=0; j<depth3.length;j++) { // 3단계 이름 찾는 for문
							//str2=Arrays.asList(depth3[j].split(";")); // 3단계 발견했으니 쪼개기
							sql2="select name from detail where uid=? and storeID=?";
							pstmt=conn.prepareStatement(sql2);
							pstmt.setString(1, depth3[j]);
							pstmt.setString(2, storeID);
							rs2=pstmt.executeQuery();
							//returnTest+="3단계 찾는쪽"+pstmt.toString();
							if(rs2.next()) {
								depth3Name+=rs2.getString("name")+",";
							}else {
								depth3Name+="내용이 없습니다."; // 내용이 없는걸 대비해야함 !
							}
						}
						sql3="select name from question where uid=? and storeID=? and depth=1"; // 1뎁스 이름 찾아오기
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, depth1);
						pstmt.setString(2, storeID);
						rs3 = pstmt.executeQuery();
						
						if(rs3.next()) {
							String oneName=rs3.getString("name");
							
							sql4="select name from question where uid=? and storeID=? and depth=2"; // 2뎁스 이름 찾아오기\
							pstmt = conn.prepareStatement(sql4);
							pstmt.setString(1, depth2);
							pstmt.setString(2, storeID);
							rs4 = pstmt.executeQuery();
							//returnTest+="2단계 이름 찾는쪽인데 서치55555"+pstmt.toString();
							
							if(rs4.next()) {
								String twoName=rs4.getString("name");
								depth3Name = depth3Name.substring(0, depth3Name.length()-1); // 맨뒤에 ,표 없애고 넣으려고
								//returnTest+="마지막단계 들어오 ㅁ?"+oneName+twoName+depth3Name;
								jsonObject=new JSONObject();
								jsonObject.put("storeName", storeName123);
								jsonObject.put("date", date); 
								jsonObject.put("oneContent",oneName);
								jsonObject.put("twoContent", twoName);
								jsonObject.put("threeContent", depth3Name);
								jsonObject.put("process",process);
								jsonObject.put("processT",processT);
								jsonObject.put("phone",phone); // 처리 완료 할때 이용하려고 가지고 감
								//returns+=jsonObject.toString()+"다음거"; //차례대로 가져오고 있는것 확인함
								jsonArray.add(jsonObject);
							}
						}				
					}
							
				}
				jsonMain.put("receipt", jsonArray);
				returns+=jsonMain.toString(); //제대로 들어가고 있는것 확인함 
			} catch (Exception e) {

			} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
					if (rs2 != null)try {rs2.close();} catch (SQLException ex) {}
					if (rs3 != null)try {rs3.close();} catch (SQLException ex) {}
					if (rs4 != null)try {rs4.close();} catch (SQLException ex) {}
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
				if (conn != null)try {conn.close();} catch (SQLException ex) {}
			}
			return returns;
		}
	
}



