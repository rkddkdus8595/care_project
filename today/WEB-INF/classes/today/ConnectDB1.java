
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
	// �̱��� �������� ��� �ϱ��� �� �ڵ��
	private static ConnectDB1 instance = new ConnectDB1();

	public static ConnectDB1 getInstance() {
		return instance;
	}

	public ConnectDB1() {

	}
	
	private String jdbcUrl = "jdbc:mysql://13.125.14.62/care?serverTimezone=Asia/Seoul"; // MySQL ���� "jdbc:mysql://localhost:3306/DB�̸�"
	private String dbId = "root"; // MySQL ���� "������ ��� root"
	private String dbPw = "Lotte1234!"; // ��й�ȣ "mysql ��ġ �� ������ ��й�ȣ"
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
	
	// -------------�Ϲ� ������--------------
	public String searchDB1(String year1, String month1, String day1, String year2, String month2, String day2, String getID, String tabPosition) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			//select * from store where DATE(date) between DATE('2020-12-23') AND DATE('2020-12-31') 
			//AND storeID=(select storeID from manager where managerID='main123');
			//��ȸ�Ҷ� ��ó�� �̷��� ����� �ش� �Ŵ����� �����ִ� ���� ������ ���� !!!!!
			//select * from store where DATE(date) between DATE('2020-12-01') and DATE('2020-12-31') 
			//and process='Y' and storeID=(select storeID from manager where managerID='main123');

			if(tabPosition.equals("��ü")) {
				sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND storeID=(select storeID from manager where managerID=?)";
				returns2=tabPosition+"/��ü�̴�";
			}else if(tabPosition.equals("��ó��")) {
				sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='N' AND storeID=(select storeID from manager where managerID=?)";
				returns2=tabPosition+"/��ó����";
			}else if(tabPosition.equals("ó���Ϸ�")) {
				sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='Y' AND storeID=(select storeID from manager where managerID=?)";
				returns2=tabPosition+"/ó���Ϸ��";
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
				//returns+="����������";
				//returns+=rs.getString("date")+"|";
				//returns+=rs.getString("recept")+"|";
				//returns+=rs.getString("process")+"|";
				//returns+=rs.getString("processT")+"\n"; // �����ڷ� ����
				depth3Name="";
				String date=rs.getString("date");
				String process=rs.getString("process");
				String processT=rs.getString("processT");
				String phone=rs.getString("phone");
				String depth1=rs.getString("depth1");
				String depth2=rs.getString("depth2");
				String[] depth3=rs.getString("depth3").split(";"); // 3�ܰ� ������ �亯�� �������̹Ƿ� ;�� �߷��� ����
				storeID=rs.getString("storeID");
				jsonObject= new JSONObject(); // �̷��� �ϴϱ� �ߺ� �ż� �ȵ��� �ߵ� ,,,
				for(int j=0; j<depth3.length;j++) { // 3�ܰ� �̸� ã�� for��
					//str2=Arrays.asList(depth3[j].split(";")); // 3�ܰ� �߰������� �ɰ���
					sql2="select name from detail where uid=? and storeID=?";
					pstmt=conn.prepareStatement(sql2);
					pstmt.setString(1, depth3[j]);
					pstmt.setString(2, storeID);
					rs2=pstmt.executeQuery();
					returnTest+="3�ܰ� ã����"+pstmt.toString();
					if(rs2.next()) {
						depth3Name+=rs2.getString("name")+",";
					}else {
						depth3Name+="������ �����ϴ�."; // ������ ���°� ����ؾ��� !
					}
				}
				sql3="select name from question where uid=? and storeID=? and depth=1"; // 1���� �̸� ã�ƿ���
				pstmt = conn.prepareStatement(sql3);
				pstmt.setString(1, depth1);
				pstmt.setString(2, storeID);
				rs3 = pstmt.executeQuery();
				
				if(rs3.next()) {
					String oneName=rs3.getString("name");
					
					sql4="select name from question where uid=? and storeID=? and depth=2"; // 2���� �̸� ã�ƿ���\
					pstmt = conn.prepareStatement(sql4);
					pstmt.setString(1, depth2);
					pstmt.setString(2, storeID);
					rs4 = pstmt.executeQuery();
					returnTest+="2�ܰ� �̸� ã����"+pstmt.toString();
					if(rs4.next()) {
						String twoName=rs4.getString("name");
						
						depth3Name = depth3Name.substring(0, depth3Name.length()-1); // �ǵڿ� ,ǥ ���ְ� ��������
						returnTest+="�������ܰ� ���� ��?"+oneName+twoName+depth3Name;
						jsonObject=new JSONObject();
						jsonObject.put("date", date); 
						jsonObject.put("oneContent",oneName);
						jsonObject.put("twoContent", twoName);
						jsonObject.put("threeContent", depth3Name);
						jsonObject.put("process",process);
						jsonObject.put("processT",processT);
						jsonObject.put("phone",phone); // ó�� �Ϸ� �Ҷ� �̿��Ϸ��� ������ ��
						//returns+=jsonObject.toString()+"������"; //���ʴ�� �������� �ִ°� Ȯ����
						jsonArray.add(jsonObject);
					}
				}
				
				/*-------------------- ������ --------------
				jsonObject= new JSONObject(); // �̷��� �ϴϱ� �ߺ� �ż� �ȵ��� �ߵ� ,,,
				//returns+=jsonObject.put("date", rs.getString("date")).toString(); // ����,,,,��µǴµ�,,?
				jsonObject.put("date", rs.getString("date")); 
				jsonObject.put("content",rs.getString("content"));
				jsonObject.put("process",rs.getString("process"));
				jsonObject.put("processT",rs.getString("processT"));
				jsonObject.put("phone",rs.getString("phone")); // ó�� �Ϸ� �Ҷ� �̿��Ϸ��� ������ ��
				jsonArray.add(jsonObject);*/
				
			}
			jsonMain.put("receipt", jsonArray);
			returns+=jsonMain.toString(); //����� ���� �ִ°� Ȯ���� 
			//{"manager":[{"date":"2020-12-27 23:25:41","process":"N","processT":"2020-12-27 23:25:41","recept":"133133"}]} �׷��� ��Ʈ������ ��������
			// ��Ʈ������ �����ϱ� �� ! ! !
			//returns=pstmt.toString();
		} catch (Exception e) {

		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	// -------------���� ������--------------  // ���� ��� �� ���
		public String searchDB2(String year1, String month1, String day1, String year2, String month2, String day2, String getID, 
				String storeName, String tabPosition) { 
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

				returnTest="��ġ���2�� ���Ծ�";
				sql="select storeID from store where storeName=?";
				pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, storeName);
				rs=pstmt.executeQuery();
				if(tabPosition.equals("��ü")) {
					sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND storeID=(select storeID from store where storeName=?)";

				}else if(tabPosition.equals("��ó��")) {
					sql = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='N' AND storeID=(select storeID from store where storeName=?)";
				}else if(tabPosition.equals("ó���Ϸ�")) {
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
					String[] depth3=rs.getString("depth3").split(";"); // 3�ܰ� ������ �亯�� �������̹Ƿ� ;�� �߷��� ����
					storeID=rs.getString("storeID");
					jsonObject= new JSONObject(); // �̷��� �ϴϱ� �ߺ� �ż� �ȵ��� �ߵ� ,,,
					for(int j=0; j<depth3.length;j++) { // 3�ܰ� �̸� ã�� for��
						//str2=Arrays.asList(depth3[j].split(";")); // 3�ܰ� �߰������� �ɰ���
						sql2="select name from detail where uid=? and storeID=?";
						pstmt=conn.prepareStatement(sql2);
						pstmt.setString(1, depth3[j]);
						pstmt.setString(2, storeID);
						rs2=pstmt.executeQuery();
						returnTest+="3�ܰ� ã����"+pstmt.toString();
						if(rs2.next()) {
							depth3Name+=rs2.getString("name")+",";
						}else {
							depth3Name+="������ �����ϴ�."; // ������ ���°� ����ؾ��� !
						}
					}
					sql3="select name from question where uid=? and storeID=? and depth=1"; // 1���� �̸� ã�ƿ���
					pstmt = conn.prepareStatement(sql3);
					pstmt.setString(1, depth1);
					pstmt.setString(2, storeID);
					rs3 = pstmt.executeQuery();
					
					if(rs3.next()) {
						String oneName=rs3.getString("name");
						
						sql4="select name from question where uid=? and storeID=? and depth=2"; // 2���� �̸� ã�ƿ���\
						pstmt = conn.prepareStatement(sql4);
						pstmt.setString(1, depth2);
						pstmt.setString(2, storeID);
						rs4 = pstmt.executeQuery();
						returnTest+="2�ܰ� �̸� ã����"+pstmt.toString();
						if(rs4.next()) {
							String twoName=rs4.getString("name");
							
							depth3Name = depth3Name.substring(0, depth3Name.length()-1); // �ǵڿ� ,ǥ ���ְ� ��������
							returnTest+="�������ܰ� ���� ��?"+oneName+twoName+depth3Name;
							jsonObject=new JSONObject();
							jsonObject.put("date", date); 
							jsonObject.put("oneContent",oneName);
							jsonObject.put("twoContent", twoName);
							jsonObject.put("threeContent", depth3Name);
							jsonObject.put("process",process);
							jsonObject.put("processT",processT);
							jsonObject.put("phone",phone); // ó�� �Ϸ� �Ҷ� �̿��Ϸ��� ������ ��
							//returns+=jsonObject.toString()+"������"; //���ʴ�� �������� �ִ°� Ȯ����
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
		
		// -------------���� ������--------------  // ���� ��� �����
		public String searchDB3(String year1, String month1, String day1, String year2, String month2, String day2, String getID, 
				String storeName, String tabPosition) { // ���۰����ڴ� ��� ���� �� �� �� �ֱ� ������ !!!
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

				returnTest="��ġ���3�� ���԰�";
				if(tabPosition.equals("��ü")) {
					sql="select * from receipt where DATE(date) between DATE(?) AND DATE(?)";
				}else if(tabPosition.equals("��ó��")) {
					sql="select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='N'";
					
				}else if(tabPosition.equals("ó���Ϸ�")) {
					sql="select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND process='Y'";
				}
				returnTest+="�������� ����";

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
					String[] depth3=rs.getString("depth3").split(";"); // 3�ܰ� ������ �亯�� �������̹Ƿ� ;�� �߷��� ����
					storeID=rs.getString("storeID");
					
					sql2="select storeName from store where storeID=?";
					pstmt=conn.prepareStatement(sql2);
					pstmt.setString(1, storeID);
					rs1=pstmt.executeQuery();
					
					if(rs1.next()) {
						String storeName123=rs1.getString("storeName");
						
						jsonObject= new JSONObject(); // �̷��� �ϴϱ� �ߺ� �ż� �ȵ��� �ߵ� ,,,
						for(int j=0; j<depth3.length;j++) { // 3�ܰ� �̸� ã�� for��
							//str2=Arrays.asList(depth3[j].split(";")); // 3�ܰ� �߰������� �ɰ���
							sql2="select name from detail where uid=? and storeID=?";
							pstmt=conn.prepareStatement(sql2);
							pstmt.setString(1, depth3[j]);
							pstmt.setString(2, storeID);
							rs2=pstmt.executeQuery();
							returnTest+="3�ܰ� ã����"+pstmt.toString();
							if(rs2.next()) {
								depth3Name+=rs2.getString("name")+",";
							}else {
								depth3Name+="������ �����ϴ�."; // ������ ���°� ����ؾ��� !
							}
						}
						sql3="select name from question where uid=? and storeID=? and depth=1"; // 1���� �̸� ã�ƿ���
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, depth1);
						pstmt.setString(2, storeID);
						rs3 = pstmt.executeQuery();
						
						if(rs3.next()) {
							String oneName=rs3.getString("name");
							
							sql4="select name from question where uid=? and storeID=? and depth=2"; // 2���� �̸� ã�ƿ���\
							pstmt = conn.prepareStatement(sql4);
							pstmt.setString(1, depth2);
							pstmt.setString(2, storeID);
							rs4 = pstmt.executeQuery();
							returnTest+="2�ܰ� �̸� ã����"+pstmt.toString();
							if(rs4.next()) {
								String twoName=rs4.getString("name");
								
								depth3Name = depth3Name.substring(0, depth3Name.length()-1); // �ǵڿ� ,ǥ ���ְ� ��������
								returnTest+="�������ܰ� ���� ��?"+oneName+twoName+depth3Name;
								jsonObject=new JSONObject();
								jsonObject.put("storeName", storeName123);
								jsonObject.put("date", date); 
								jsonObject.put("oneContent",oneName);
								jsonObject.put("twoContent", twoName);
								jsonObject.put("threeContent", depth3Name);
								jsonObject.put("process",process);
								jsonObject.put("processT",processT);
								jsonObject.put("phone",phone); // ó�� �Ϸ� �Ҷ� �̿��Ϸ��� ������ ��
								//returns+=jsonObject.toString()+"������"; //���ʴ�� �������� �ִ°� Ȯ����
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
				
		
		
	
	// -------------�����--------------
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
					if(tabPosition.equals("��ü")) {
						sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=? AND"
								+ " storeID=(select storeID from store where storeName=?)";
						//returns=tabPosition+"/��ü�̴�";
					}else if(tabPosition.equals("��ó��")) {
						sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=? AND"
								+ " process='N' AND storeID=(select storeID from store where storeName=?)";
						//returns=tabPosition+"/��ó����";
					}else if(tabPosition.equals("ó���Ϸ�")) {
						sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=? AND"
								+ " process='Y' AND storeID=(select storeID from store where storeName=?)";					
						//returns=tabPosition+"/ó���Ϸ��";
					}
					//returns+="sql��"+sql1;

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
					returnTest+="�ι�° �ܰ�:"+pstmt.toString()+"/";
					//returns+="��������:"+pstmt.toString();
					int i=0;
					returns="";
					while(rs.next()) { 
						returnTest+="���Ϲ��� ����";
						depth3Name="";
						String date=rs.getString("date");
						String process=rs.getString("process");
						String processT=rs.getString("processT");
						String phone=rs.getString("phone");
						String depth1=rs.getString("depth1");
						String depth2=rs.getString("depth2");
						String[] depth3=rs.getString("depth3").split(";"); // 3�ܰ� ������ �亯�� �������̹Ƿ� ;�� �߷��� ����
						returnTest+="����� �´�";
						jsonObject= new JSONObject(); // �̷��� �ϴϱ� �ߺ� �ż� �ȵ��� �ߵ� ,,,
						returnTest+="�� �׽�Ʈ"+date+"/"+process+"/"+processT+"/"+phone+"/"+depth1+"/"+depth2+"/"+Arrays.toString(depth3);
						for(int j=0; j<depth3.length;j++) { // 3�ܰ� �̸� ã�� for��
							//str2=Arrays.asList(depth3[j].split(";")); // 3�ܰ� �߰������� �ɰ���
							sql2="select name from detail where uid=? and storeID=?";
							pstmt=conn.prepareStatement(sql2);
							pstmt.setString(1, depth3[j]);
							pstmt.setString(2, storeID);
							rs2=pstmt.executeQuery();
							returnTest+="3�ܰ� ã����"+pstmt.toString();
							if(rs2.next()) {
								depth3Name+=rs2.getString("name")+",";
							}else {
								depth3Name+="������ �����ϴ�."; // ������ ���°� ����ؾ��� !
							}
						}
						sql3="select name from question where uid=? and storeID=? and depth=1"; // 1���� �̸� ã�ƿ���
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, depth1);
						pstmt.setString(2, storeID);
						rs3 = pstmt.executeQuery();
						returnTest+="1�ܰ� �̸� ã����"+pstmt.toString();
						if(rs3.next()) {
							String oneName=rs3.getString("name");
							
							sql4="select name from question where uid=? and storeID=? and depth=2"; // 2���� �̸� ã�ƿ���\
							pstmt = conn.prepareStatement(sql4);
							pstmt.setString(1, depth2);
							pstmt.setString(2, storeID);
							rs4 = pstmt.executeQuery();
							returnTest+="2�ܰ� �̸� ã����"+pstmt.toString();
							if(rs4.next()) {
								String twoName=rs4.getString("name");
								
								depth3Name = depth3Name.substring(0, depth3Name.length()-1); // �ǵڿ� ,ǥ ���ְ� ��������
								returnTest+="�������ܰ� ���� ��?"+oneName+twoName+depth3Name;
								jsonObject=new JSONObject();
								jsonObject.put("date", date); 
								jsonObject.put("oneContent",oneName);
								jsonObject.put("twoContent", twoName);
								jsonObject.put("threeContent", depth3Name);
								jsonObject.put("process",process);
								jsonObject.put("processT",processT);
								jsonObject.put("phone",phone); // ó�� �Ϸ� �Ҷ� �̿��Ϸ��� ������ ��
								//returns+=jsonObject.toString()+"������"; //���ʴ�� �������� �ִ°� Ȯ����
								jsonArray.add(jsonObject);
							}
						}
						
						
					}
					jsonMain.put("receipt", jsonArray);
					returns+=jsonMain.toString(); //����� ���� �ִ°� Ȯ���� 
					//{"manager":[{"date":"2020-12-27 23:25:41","process":"N","processT":"2020-12-27 23:25:41","recept":"133133"}]} �׷��� ��Ʈ������ ��������
					// ��Ʈ������ �����ϱ� �� ! ! !
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
		
		// -------------�����--------------  // ���� ��� �����
		public String searchDB5(String year1, String month1, String day1, String year2, String month2, String day2, String getID, 
				String storeName, String tabPosition) { 
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

				returnTest="��ġ���3�� ���԰�";
				if(tabPosition.equals("��ü")) {
					sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=?";
					//returns=tabPosition+"/��ü�̴�";
				}else if(tabPosition.equals("��ó��")) {
					sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=? AND"
							+ " process='N'";
					//returns=tabPosition+"/��ó����";
				}else if(tabPosition.equals("ó���Ϸ�")) {
					sql1 = "select * from receipt where DATE(date) between DATE(?) AND DATE(?) AND phone=? AND"
							+ " process='Y'";					
					//returns=tabPosition+"/ó���Ϸ��";
				}
				returnTest+="�������� ����";

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
					String[] depth3=rs.getString("depth3").split(";"); // 3�ܰ� ������ �亯�� �������̹Ƿ� ;�� �߷��� ����
					storeID=rs.getString("storeID");
					
					sql2="select storeName from store where storeID=?";
					pstmt=conn.prepareStatement(sql2);
					pstmt.setString(1, storeID);
					rs1=pstmt.executeQuery();
					
					if(rs1.next()) {
						String storeName123=rs1.getString("storeName");
						jsonObject= new JSONObject(); // �̷��� �ϴϱ� �ߺ� �ż� �ȵ��� �ߵ� ,,,
						for(int j=0; j<depth3.length;j++) { // 3�ܰ� �̸� ã�� for��
							//str2=Arrays.asList(depth3[j].split(";")); // 3�ܰ� �߰������� �ɰ���
							sql2="select name from detail where uid=? and storeID=?";
							pstmt=conn.prepareStatement(sql2);
							pstmt.setString(1, depth3[j]);
							pstmt.setString(2, storeID);
							rs2=pstmt.executeQuery();
							//returnTest+="3�ܰ� ã����"+pstmt.toString();
							if(rs2.next()) {
								depth3Name+=rs2.getString("name")+",";
							}else {
								depth3Name+="������ �����ϴ�."; // ������ ���°� ����ؾ��� !
							}
						}
						sql3="select name from question where uid=? and storeID=? and depth=1"; // 1���� �̸� ã�ƿ���
						pstmt = conn.prepareStatement(sql3);
						pstmt.setString(1, depth1);
						pstmt.setString(2, storeID);
						rs3 = pstmt.executeQuery();
						
						if(rs3.next()) {
							String oneName=rs3.getString("name");
							
							sql4="select name from question where uid=? and storeID=? and depth=2"; // 2���� �̸� ã�ƿ���\
							pstmt = conn.prepareStatement(sql4);
							pstmt.setString(1, depth2);
							pstmt.setString(2, storeID);
							rs4 = pstmt.executeQuery();
							//returnTest+="2�ܰ� �̸� ã�����ε� ��ġ55555"+pstmt.toString();
							
							if(rs4.next()) {
								String twoName=rs4.getString("name");
								depth3Name = depth3Name.substring(0, depth3Name.length()-1); // �ǵڿ� ,ǥ ���ְ� ��������
								//returnTest+="�������ܰ� ���� ��?"+oneName+twoName+depth3Name;
								jsonObject=new JSONObject();
								jsonObject.put("storeName", storeName123);
								jsonObject.put("date", date); 
								jsonObject.put("oneContent",oneName);
								jsonObject.put("twoContent", twoName);
								jsonObject.put("threeContent", depth3Name);
								jsonObject.put("process",process);
								jsonObject.put("processT",processT);
								jsonObject.put("phone",phone); // ó�� �Ϸ� �Ҷ� �̿��Ϸ��� ������ ��
								//returns+=jsonObject.toString()+"������"; //���ʴ�� �������� �ִ°� Ȯ����
								jsonArray.add(jsonObject);
							}
						}				
					}
							
				}
				jsonMain.put("receipt", jsonArray);
				returns+=jsonMain.toString(); //����� ���� �ִ°� Ȯ���� 
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



