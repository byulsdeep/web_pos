package com.web_pos.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.web_pos.beans.AuthBean;
import com.web_pos.beans.GoodsBean;
import com.web_pos.beans.MemberBean;
import com.web_pos.beans.OrdersBean;
import com.web_pos.beans.SalesBean;

/* 데이터 베이스 연동 */
public class DataAccessObject {

	public DataAccessObject() {}

	/* Oracle DBMS에 연결 */
	public Connection connectionOpen() {
		/* Connection 개체 선언 */
		Connection connection = null;

		/* ip, port, sid 정보 저장 */
		String url = "jdbc:oracle:thin:@localhost:1521:xe"; //41 
		String user = "BYUL"; //hoon
		String password = "BYUL"; //4321

		/* Driver의 유무 파악 */
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {e.printStackTrace();}
		catch (SQLException e) {e.printStackTrace();}

		return connection;
	}

	public void connectionClose(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	public int isEmployeeCode(Connection conn, AuthBean auth) {
		ResultSet rs = null;
		int result = -1;
		String sql = "SELECT COUNT(*) FROM EMP WHERE EMP_CODE = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, auth.getEmployeeCode());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				result = rs.getInt(1);
			}

		} catch (SQLException e) {e.printStackTrace();}

		return result;
	}

	/* Access 가능 여부 >> storeCode, employeeCode, emplyeePassword  */
	public int isAccess(Connection conn, AuthBean auth) {
		ResultSet rs = null;
		int result = -1;
		String sql = "SELECT COUNT(*) FROM EMP WHERE EMP_CODE = ? AND EMP_PASSWORD = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, auth.getEmployeeCode());
			pstmt.setNString(2, auth.getEmployeePassword());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				result = rs.getInt(1);
			}

		} catch (SQLException e) {e.printStackTrace();}

		return result;
	}

	/* 로그기록 생성 >> AccessHistory > ins :: storeCode, employeeCode, date, action */
	public int insAccessHistory(Connection conn, AuthBean auth) {
		int result = -1;
		String sql = "INSERT INTO AHT(AHT_EMPCODE, AHT_DATE, AHT_ACTION) "
				+ "VALUES(?, DEFAULT , ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, auth.getEmployeeCode());
			pstmt.setInt(2, auth.getAccessAction());
			result = pstmt.executeUpdate();

		} catch (SQLException e) {e.printStackTrace();}

		return result;
	}

	/* 계정의 정보 취합 : 매장이름, 직원이름, 로그인 시간, 직원등급 >> storeCode, employeeCode */
	public ArrayList<AuthBean> getUserInfo(Connection conn, AuthBean auth) {
		ArrayList<AuthBean> accessList = new ArrayList<AuthBean>();
		AuthBean ab = null;
		PreparedStatement pstmt;
		ResultSet rs;

		String sql = "SELECT * FROM ACCESSINFO WHERE EMPLOYEECODE = ?";
		try {

			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, auth.getEmployeeCode());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				ab = new AuthBean();			
				ab.setEmployeeCode(rs.getNString("EMPLOYEECODE"));
				ab.setEmployeeName(rs.getNString("EMPLOYEENAME"));
				ab.setAccessTime(rs.getNString("ACCESSTIME"));
				ab.setEmployeeLevel(rs.getNString("EMPLOYEELEVEL"));	

				accessList.add(ab);
			}
		} catch (SQLException e) {e.printStackTrace();}

		return accessList;
	}

	public int accessState(Connection conn, AuthBean auth) {
		int result = -1;
		ResultSet rs;
		PreparedStatement pstmt;
		String sql = "SELECT SUM(AHT_ACTION) FROM AHT WHERE AHT_EMPCODE = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, auth.getEmployeeCode());

			rs = pstmt.executeQuery();
			while(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {e.printStackTrace();}

		return result;
	}

	//StoreMgr
	public ArrayList<AuthBean> getEmployeeList(Connection conn, AuthBean auth){
		ArrayList<AuthBean> list = new ArrayList<AuthBean>();
		AuthBean employee = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT  EMP_CODE AS EMPLOYEECODE, EMP_NAME AS EMPLOYEENAME, EMP_LEVEL AS EMPLOYEELEVEL "
				+ "   FROM EMP";

		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while(rs.next()) {
				employee = new AuthBean();

				employee.setEmployeeCode(rs.getNString("EMPLOYEECODE"));
				employee.setEmployeeName(rs.getNString("EMPLOYEENAME"));
				employee.setEmployeeLevel(rs.getNString("EMPLOYEELEVEL"));

				list.add(employee);

			}
		} catch (SQLException e) {e.printStackTrace();}

		return list;
	}

	public ArrayList<MemberBean> getMemberList(Connection conn , MemberBean member){
		ArrayList<MemberBean> list = new ArrayList<MemberBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT  MEM_CODE AS MEMCODE, MEM_NAME AS MEMNAME, MEM_PHONE AS MEMPHONE "
				+ "FROM MEM";

		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while(rs.next()) {
				member = new MemberBean();

				member.setMemberCode(rs.getNString("MEMCODE"));
				member.setMemberName(rs.getNString("MEMNAME"));
				member.setMemberPhone(rs.getNString("MEMPHONE"));

				list.add(member);
			}
		} catch (SQLException e) {e.printStackTrace();}

		return list;
	}

	public ArrayList<GoodsBean> getGoodsList(Connection conn, AuthBean auth){
		ArrayList<GoodsBean> list = new ArrayList<GoodsBean>();
		GoodsBean goods = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM GOODSLIST";

		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while(rs.next()) {
				goods = new GoodsBean();			
				goods.setGoodsCode(rs.getNString("GOOCODE"));
				goods.setCategoryCode(rs.getNString("CGICODE"));
				goods.setCategoryName(rs.getNString("CGINAME"));
				goods.setGoodsName(rs.getNString("GOONAME"));
				goods.setGoodsCost(rs.getInt("GOOCOST"));
				goods.setGoodsPrice(rs.getInt("GOOPRICE"));
				goods.setGoodsStock(rs.getInt("GOOSTOCK"));

				list.add(goods);
			}
		} catch (SQLException e) {e.printStackTrace();}

		return list;
	}

	public int insEmployee(AuthBean auth, Connection conn) {
		int result = -1;
		PreparedStatement pstmt;

		String sql = "INSERT INTO EMP(EMP_CODE, EMP_PASSWORD, EMP_NAME, EMP_HIREDATE, EMP_LEVEL) VALUES (? ,'1111', ?, SYSDATE ,?)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, auth.getNewCode());
			pstmt.setNString(2, auth.getEmployeeName());
			pstmt.setNString(3, auth.getEmployeeLevel());

			if(pstmt.executeUpdate() == 0) {
				result = 0;
			} else {
				result = 1;
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return result;
	}

	public String getNewEmployeeCode(AuthBean auth, Connection conn) {
		PreparedStatement pstmt;
		String result = null;
		ResultSet rs;

		String sql = "SELECT LPAD((MAX(EMP_CODE)+1) , 5,'0') AS NEWEMPCODE \r\n"
				+ "FROM emp  WHERE EMP_LEVEL = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, auth.getEmployeeLevel());

			rs = pstmt.executeQuery();
			while(rs.next()) {
				auth.setNewCode(rs.getNString("NEWEMPCODE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		result = auth.getNewCode();

		return result;	
	}

	public int updEmployeePassword(Connection conn, AuthBean auth) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "UPDATE EMP SET EMP_PASSWORD = ? WHERE EMP_CODE = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, auth.getEmployeePassword());
			pstmt.setNString(2, auth.getEmployeeCode());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {e.printStackTrace();}

		return result;
	}

	public int updEmployeeLevel(Connection conn, AuthBean auth) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "UPDATE EMP SET EMP_LEVEL = ? WHERE EMP_CODE = ?";

		try {

			pstmt = conn.prepareStatement(sql);

			pstmt.setNString(1, auth.getEmployeeLevel());
			pstmt.setNString(2, auth.getEmployeeCode());


			result = pstmt.executeUpdate();

		} catch (SQLException e) {e.printStackTrace();}

		return result;
	}

	public int updMemberPhone(Connection conn , MemberBean memb) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "UPDATE MEM SET MEM_PHONE = ? WHERE MEM_CODE = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, memb.getMemberPhone());
			pstmt.setNString(2, memb.getMemberCode());

			result = pstmt.executeUpdate();

		} catch(SQLException e) {e.printStackTrace();}

		return result;	
	}

	public int updGoodsInfo(Connection conn , GoodsBean goods , AuthBean auth) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "UPDATE GOO SET GOO_NAME = ? , GOO_COST= ? , GOO_PRICE = ? , GOO_STOCK = ? WHERE GOO_CODE = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, goods.getGoodsName());
			pstmt.setInt(2, goods.getGoodsCost());
			pstmt.setLong(3, goods.getGoodsPrice());
			pstmt.setLong(4, goods.getGoodsStock());
			pstmt.setNString(5, goods.getGoodsCode());


			result = pstmt.executeUpdate();

		} catch(SQLException e) {e.printStackTrace();}

		return result;		
	}

	public int insGoodsInfo(Connection conn , GoodsBean goods) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "INSERT INTO GOO(GOO_CODE , GOO_NAME , GOO_COST , GOO_PRICE , GOO_STOCK , GOO_CGICODE)"
				+ "VALUES(?,?,?,?,?,?)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, goods.getGoodsCode());
			pstmt.setNString(2, goods.getGoodsName());
			pstmt.setInt(3, goods.getGoodsCost());
			pstmt.setInt(4, goods.getGoodsPrice());
			pstmt.setInt(5, goods.getGoodsStock());
			pstmt.setNString(6, goods.getCategoryCode());

			if(pstmt.executeUpdate() == 0) {
				result = 0;

			} else {
				result = 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;			
	}

	//판매부분
	public void search(Connection conn, GoodsBean goods){
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT GOO_CODE AS GOODSCODE, GOO_NAME AS GOODSNAME, GOO_PRICE AS GOODSPRICE FROM GOO WHERE GOO_CODE = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, goods.getGoodsCode());

			rs = pstmt.executeQuery();
			while(rs.next()) {	
				goods.setGoodsName(rs.getNString("GOODSNAME"));
				goods.setGoodsPrice(rs.getInt("GOODSPRICE"));						
			}
		} catch (SQLException e) {e.printStackTrace();}
	}

	public int insOrders(OrdersBean orders, Connection conn) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "INSERT INTO ORD(ORD_EMPCODE, ORD_DATE, ORD_STATE) "
				+ "VALUES(?, DEFAULT, ?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, orders.getEmployeeCode());
			pstmt.setNString(2, orders.getCaCode());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {e.printStackTrace();}
		return result;
	} 

	public void getOrderDate(OrdersBean orders, Connection conn) {
		PreparedStatement pstmt;
		ResultSet rs;
		String sql = "SELECT TO_CHAR(MAX(ORD_DATE), 'YYYYMMDDHH24MISS') AS ORDERDATE FROM ORD WHERE ORD_EMPCODE= ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, orders.getEmployeeCode());

			rs = pstmt.executeQuery();
			while(rs.next()) {
				orders.setOrderDate(rs.getNString("ORDERDATE"));
			}


		} catch (SQLException e) {e.printStackTrace();}

	}

	public int insOrdersDetail(OrdersBean orders, Connection conn) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "INSERT INTO ODT(ODT_ORDEMPCODE, ODT_ORDDATE, ODT_GOOCODE, ODT_QUANTITY) VALUES(?, TO_DATE(?, 'YYYYMMDDHH24MISS'), ?, ?)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, orders.getEmployeeCode());
			pstmt.setNString(2, orders.getOrderDate());
			for(int recordIdx=0; recordIdx<orders.getOrderList().size(); recordIdx++) {
				pstmt.setNString(3, orders.getOrderList().get(recordIdx).getGoodsCode());
				pstmt.setInt(4, orders.getOrderList().get(recordIdx).getGoodsQuantity());
				if(pstmt.executeUpdate() == 0) {
					result = 0;
					break;
				}else {result = 1;}
			}

		} catch (SQLException e) {e.printStackTrace();}

		return result;
	}

	public int updOrders(OrdersBean orders, Connection conn) {
		int result = -1;
		PreparedStatement pstmt;
		String sql = "UPDATE ORD SET ORD_STATE = ? WHERE ORD_EMPCODE = ? AND ORD_DATE = TO_DATE(?, 'YYYYMMDDHH24MISS')";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, orders.getCaCode());
			pstmt.setNString(2, orders.getEmployeeCode());
			pstmt.setNString(3, orders.getOrderDate());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {e.printStackTrace();}

		return result;
	}

	public int isMemberCode(Connection conn, MemberBean mem) {
		ResultSet rs = null;
		int result = -1;
		String sql = "SELECT MEM_NAME AS MEMBERNAME FROM MEM WHERE MEM_CODE = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, mem.getMemberCode());
			rs = pstmt.executeQuery();

			while(rs.next()) {
				mem.setMemberName(rs.getNString("MEMBERNAME"));
			}

		} catch (SQLException e) {e.printStackTrace();}

		if(mem.getMemberName() != null) {
			result = 1;
		} else {
			result = 0;
		}
		return result;
	}

	public int insPoints(OrdersBean od, Connection conn) {
		int result = -1;
		PreparedStatement pstmt;
		String sql ="INSERT INTO POI(POI_ORDEMPCODE, POI_ORDDATE, POI_MEMCODE, POI_AMOUNT, POI_ACTION) VALUES (?, TO_DATE(?, 'YYYYMMDDHH24MISS'), ?, ?, 1)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setNString(1, od.getEmployeeCode());
			pstmt.setNString(2, od.getOrderDate());
			pstmt.setNString(3, od.getMemberCode());
			pstmt.setInt(4, od.getAmount());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return result;
	}

	public ArrayList<AuthBean> getAccessHistory(Connection conn){
		ArrayList<AuthBean> list = new ArrayList<AuthBean>();
		AuthBean auth = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM ACCESSHISTORY";

		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while(rs.next()) {
				auth = new AuthBean();
				auth.setAccessDate(rs.getNString("DATE"));
				auth.setEmployeeName(rs.getNString("EMPLOYEENAME"));
				auth.setEmployeeLevel(rs.getNString("EMPLOYEELEVEL"));		
				auth.setAccessAction(rs.getInt("ACTION"));
				auth.setAccessTime(rs.getNString("TIME"));

				list.add(auth);								
			}
		} catch (SQLException e) {e.printStackTrace();}

		return list;
	}

	public ArrayList<SalesBean> getSales(Connection conn){
		ArrayList<SalesBean> list = new ArrayList<SalesBean>();
		SalesBean sales = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM SALESS";

		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while(rs.next()) {
				sales = new SalesBean();	
				sales.setDate(rs.getNString("DATE"));
				sales.setRevenue(rs.getInt("REVENUE"));
				sales.setProfit(rs.getInt("PROFIT"));

				list.add(sales);
			}
		} catch (SQLException e) {e.printStackTrace();}

		return list;
	}

}


