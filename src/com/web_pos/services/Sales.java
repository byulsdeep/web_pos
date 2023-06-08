package com.web_pos.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web_pos.beans.ActionBean;
import com.web_pos.beans.AuthBean;
import com.web_pos.beans.GoodsBean;
import com.web_pos.beans.MemberBean;
import com.web_pos.beans.OrdersBean;
import com.web_pos.beans.OrdersDetailBean;
import com.web_pos.dao.DataAccessObject;

/* 상품판매 관련 업무 */
public class Sales {
	private AuthBean auth;

	public Sales() {}

	public ActionBean backController(HttpServletRequest req) {
		ActionBean action = null;

		if(this.isSession(req)) {
			switch(req.getRequestURI().substring(req.getContextPath().length()+1)){
			case "PosMgr":
				action = this.mainCtl(req);
				break;
			case "Search":
				action = this.search(req);
				break;
			case "Payment":
				action = this.payment(req);
				break;
			case "CheckMember":
				action = this.checkMember(req);
				break;	
			case "AccruePoints":
				action = this.accruePoints(req);
				break;		
			}
		}else {
			try {
				action = new ActionBean();
				action.setDispatcher(false);
				action.setPage("logIn.jsp?mes=" + URLEncoder.encode("로그인을 하셔야 서비스를 이용하실 수 있습니다.", "UTF-8"));
			} catch (UnsupportedEncodingException e) {e.printStackTrace();}
		}
		return action;
	}

	/* 주문 완료 */
	private ActionBean payment(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		HttpSession session = req.getSession();
		DataAccessObject dao = new DataAccessObject();
		Connection conn;
		boolean tran = false;
		OrdersBean orders = new OrdersBean();
		ArrayList<OrdersDetailBean> orderList = new ArrayList<OrdersDetailBean>();
		OrdersDetailBean ordersDetail = null; 
		
		/* Client Data --> Bean */
		// OrdersDetail 
		for(int recordIdx = 0; recordIdx < req.getParameterValues("goodsCode").length; recordIdx++ ) {
			ordersDetail = new OrdersDetailBean();
			ordersDetail.setGoodsCode(req.getParameterValues("goodsCode")[recordIdx]);
			ordersDetail.setGoodsQuantity(Integer.parseInt(req.getParameterValues("quantity")[recordIdx]));
			orderList.add(ordersDetail);
		}
		
		// Orders
		orders.setEmployeeCode((String)session.getAttribute("employeeCode"));
		orders.setCaCode("OS");
		orders.setOrderList(orderList);
		
		conn = dao.connectionOpen();
		try {
			conn.setAutoCommit(false);
			if(this.convertToBool(dao.insOrders(orders, conn))) {
				dao.getOrderDate(orders, conn);
				if(orders.getOrderDate() != null) {
					if(this.convertToBool(dao.insOrdersDetail(orders, conn))) {
						orders.setCaCode("OP");
						if(this.convertToBool(dao.updOrders(orders, conn))) {	
							tran = true;	
							
						}
					}
				}
			}		
		} catch (SQLException e) {e.printStackTrace();}
		finally {
			try {
				if(!conn.isClosed()) {
					if(tran) conn.commit();	else conn.rollback();
				}
			} catch (SQLException e) {e.printStackTrace();}			
		}
		
		dao.connectionClose(conn);
		
		action.setAjaxData(tran?"success":"error");
		return action;
	}
	
	private ActionBean checkMember(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		MemberBean mem = new MemberBean();
		DataAccessObject dao = new DataAccessObject();
		Connection conn;
		
		mem.setMemberCode(req.getParameterValues("memberCode")[0]);
		conn = dao.connectionOpen();
		
		if(this.convertToBool(dao.isMemberCode(conn, mem))) {
			action.setAjaxData(mem.getMemberName());
		} else {
			action.setAjaxData("no");
		}
		
		dao.connectionClose(conn);
		return action;
	}
	
	private ActionBean accruePoints(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		HttpSession session = req.getSession();
		DataAccessObject dao = new DataAccessObject();
		Connection conn;
		OrdersBean orders = new OrdersBean();
		OrdersDetailBean ordersDetail = null; 
		
		orders.setMemberCode(req.getParameterValues("memberCode")[0]);
		orders.setEmployeeCode((String)session.getAttribute("employeeCode"));
		orders.setCaCode("OC");

		if(orders.getMemberCode().equals("99999")) {
			orders.setAmount(0);
		} else {
			orders.setAmount(Integer.parseInt(req.getParameterValues("amount")[0])); 
		}
		
		conn = dao.connectionOpen();
		
		dao.getOrderDate(orders, conn);
		
		if(this.convertToBool(dao.insPoints(orders, conn))) {
			if(this.convertToBool(dao.updOrders(orders, conn))) {
				action.setAjaxData(String.valueOf(orders.getAmount()));
			} else {
				action.setAjaxData("error");
			}
		} else {
			action.setAjaxData("error");
		}
		
		dao.connectionClose(conn);
		
		return action;
	}
	
	
	
	/* 상품코드에 따른 상품정보 가져오기 */
	private ActionBean search(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		GoodsBean goods = new GoodsBean();
		DataAccessObject dao = new DataAccessObject();
		Connection conn = null;
		
		goods.setGoodsCode(req.getParameter("goodsCode"));
		
		conn = dao.connectionOpen();
		dao.search(conn, goods);		
		dao.connectionClose(conn);
		
		if(goods.getGoodsName() != null) {
			action.setAjaxData(goods.getGoodsCode() + ":" + goods.getGoodsName() + ":" + goods.getGoodsPrice());
		} else {
			action.setAjaxData("none");
		}
		return action;
	}
	
	/* 세션의 storeCode와 employeeCode의 유무 확인*/
	private boolean isSession(HttpServletRequest req) {
		boolean result = false;
		HttpSession session = req.getSession();

		DataAccessObject dao = new DataAccessObject();
		Connection conn = null;

		if(session.getAttribute("employeeCode") != null) {
			auth = new AuthBean();
			auth.setEmployeeCode((String)session.getAttribute("employeeCode"));

			conn = dao.connectionOpen();
			result = this.convertToBool(dao.accessState(conn, auth));
			dao.connectionClose(conn);
		}

		return result;
	}

	private ActionBean mainCtl(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		DataAccessObject dao = new DataAccessObject();
		Connection conn = null;

		conn = dao.connectionOpen();

		/* AccessInfo 가져오기*/
		Authentication authentication = new Authentication();
		authentication.getAccessInfo(req, auth, dao, conn);
		
		dao.connectionClose(conn);
		/* 응답 방식 및 페이지 설정*/
		action.setDispatcher(true);
		action.setPage("pos.jsp");

		return action;
	}


	private boolean convertToBool(int value) {
		return (value == 1)? true : false;
	}
}
