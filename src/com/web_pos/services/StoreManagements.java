package com.web_pos.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web_pos.beans.ActionBean;
import com.web_pos.beans.AuthBean;
import com.web_pos.beans.GoodsBean;
import com.web_pos.beans.MemberBean;
import com.web_pos.dao.DataAccessObject;

public class StoreManagements {
	//매장관리 업무
	private AuthBean auth;
	public StoreManagements() {

	}

	public ActionBean backController(HttpServletRequest req) {

		ActionBean action = null;
		if(this.isSession(req)) {
			switch(req.getRequestURI().substring(req.getContextPath().length()+1)) {
			case "StoreMgr" :
				action = this.mainCtl(req);
				break;
			case "UpdPassword":
				action = changePassword(req);
				break;	
			case "EmpReg":
				action = regEmployeeCtl(req);
				break;
			case "UpdLevel":
				action = this.changeLevel(req);
				break;
			case "UpdPhone":
				action = this.modifyPhone(req);
				break;
			case "UpdGoods":
				action = this.modifyGoods(req);
				break;
			case "InsGoods":
				action = this.addGoods(req);
				break;			
			}
		}else {
			try {
				action = new ActionBean();
				action.setDispatcher(false);
				action.setPage("logIn.jsp?mes="+ URLEncoder.encode("로그인을 하셔야 서비스를 이용하실 수 있습니다.", "UTF-8"));
			} catch (UnsupportedEncodingException e) {e.printStackTrace();}
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
		MemberBean member = new MemberBean();
		Connection conn = null;

		conn = dao.connectionOpen();

		//AccessInfo 가져오기
		Authentication authentication = new Authentication();
		authentication.getAccessInfo(req, auth, dao, conn);

		/* 직원정보 가져오기 */
		req.setAttribute("empList", this.makeHtmlEmpList(dao.getEmployeeList(conn, auth)));
		/* 회원정보 가져오기 */
		req.setAttribute("memberList", this.makeHtmlMemberList(dao.getMemberList(conn, member)));
		/* 상품정보 가져오기 */
		req.setAttribute("goodsList", this.makeHtmlGoodsList(dao.getGoodsList(conn, auth)));
		dao.connectionClose(conn);
		/* 응답 방식 및 페이지 설정*/
		action.setDispatcher(true);
		action.setPage("store.jsp");

		return action;
	}

	private String makeHtmlEmpList(ArrayList<AuthBean> list) {
		StringBuffer sb = new StringBuffer();

		sb.append("<table>");
		sb.append("<tr><th>직원코드</th><th>직원이름</th><th>직원등급</th><th>비번변경</th><th>등급변경</th></tr>");

		for(AuthBean emp : list) {
			String data = emp.getEmployeeCode() + ":" + emp.getEmployeeName() + ":" + emp.getEmployeeLevel() +":";
			sb.append("<tr onMouseOver=\"changeTrCss(this, true)\" onMouseOut=\"changeTrCss(this, false)\">");
			sb.append("<td>" + emp.getEmployeeCode() + "</td>");
			sb.append("<td>" + emp.getEmployeeName() + "</td>");
			sb.append("<td>" + emp.getEmployeeLevel() + "</td>");
			sb.append("<td>" + "<input type=\"button\" value=\"변경\" class=\"dml-btn\" onMouseOver=\"changeBtnCss(this, \'dml-btn-over\')\" onMouseOut=\"changeBtnCss(this, \'dml-btn\')\" onClick=\"changePassword(\'직원비밀번호변경\', \'" + data +"\')\" />" + "</td>");
			sb.append("<td>" + "<input type=\"button\" value=\"변경\" class=\"dml-btn\" onMouseOver=\"changeBtnCss(this, \'dml-btn-over\')\" onMouseOut=\"changeBtnCss(this, \'dml-btn\')\" onClick=\"changeLevel(\'직원등급변경\', \'"+ data +"\')\" />" + "</td>");
			sb.append("</tr>");
		}

		sb.append("</table>");

		return sb.toString();
	}

	private String makeHtmlMemberList(ArrayList<MemberBean> list) {
		StringBuffer sb = new StringBuffer();

		sb.append("<table>");
		sb.append("<tr><th>고객코드</th><th>고객이름</th><th>고객전화</th><th>전번변경</th></tr>");

		for(MemberBean member : list) {
			String data = member.getMemberCode() + ":" + member.getMemberName() + ":" + member.getMemberPhone() + ":";
			sb.append("<tr onMouseOver=\"changeTrCss(this, true)\" onMouseOut=\"changeTrCss(this, false)\">");
			sb.append("<td>" + member.getMemberCode() + "</td>");
			sb.append("<td>" + member.getMemberName() + "</td>");
			sb.append("<td>" + member.getMemberPhone() + "</td>");
			sb.append("<td>" + "<input type=\"button\" value=\"변경\" class=\"dml-btn\" onMouseOver=\"changeBtnCss(this, \'dml-btn-over\')\" onMouseOut=\"changeBtnCss(this, \'dml-btn\')\" onClick=\"changePhone(\'회원정보변경\', \'"+ data +"\')\" />" + "</td>");
			sb.append("</tr>");
		}

		sb.append("</table>");

		return sb.toString();
	}

	private String makeHtmlGoodsList(ArrayList<GoodsBean> list) {
		StringBuffer sb = new StringBuffer();

		sb.append("<table>");
		sb.append("<tr><th>상품코드</th><th>상품이름</th><th>매입비용</th><th>판매가격</th><th>상품재고</th><th>분류항목</th><th>정보변경</th></tr>");

		for(GoodsBean goods : list) {
			String data = goods.getGoodsCode() +":"+ goods.getGoodsName() +":"+ goods.getGoodsCost()+":"+ goods.getGoodsPrice() +":"+ goods.getGoodsStock()+":" + goods.getCategoryName();
			sb.append("<tr onMouseOver=\"changeTrCss(this, true)\" onMouseOut=\"changeTrCss(this, false)\">");
			sb.append("<td>" + goods.getGoodsCode() + "</td>");
			sb.append("<td>" + goods.getGoodsName() + "</td>");
			sb.append("<td>" + goods.getGoodsCost() + "</td>");
			sb.append("<td>" + goods.getGoodsPrice() + "</td>");
			sb.append("<td>" + goods.getGoodsStock() + "</td>");
			sb.append("<td>" + goods.getCategoryName() + "</td>");
			sb.append("<td>" + "<input type=\"button\" value=\"변경\" class=\"dml-btn\" onMouseOver=\"changeBtnCss(this, \'dml-btn-over\')\" onMouseOut=\"changeBtnCss(this, \'dml-btn\')\" onClick=\"changeGoods(\'상품정보변경\', \'"+ data +"\')\" />" + "</td>");
			sb.append("</tr>");
		}

		sb.append("</table>");

		return sb.toString();
	}

	private ActionBean regEmployeeCtl(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		AuthBean auth = null;
		DataAccessObject dao = null;
		Connection conn = null;
		String page = "logIn.jsp";
		boolean isDispatcher = false;

		// Message 처리
		try {
			page = "?mes=" + URLEncoder.encode("로그인 후 사용하실 수 있습니다.", "UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}
		/* Session 정보 확인 */
		HttpSession session = req.getSession();

		if(session.getAttribute("employeeCode") != null) {
			auth = new AuthBean();

			auth.setEmployeeName(req.getParameterValues("employeeName")[0]);
			auth.setEmployeeLevel(req.getParameterValues("employeeLevel")[0]);


			/* DataAccessObject */	
			dao = new DataAccessObject();		
			conn = dao.connectionOpen();

			auth.setNewCode(dao.getNewEmployeeCode(auth, conn));

			if(this.convertToBool(dao.insEmployee(auth, conn))) {
				page = "StoreMgr";
				isDispatcher = true;
			}
			System.out.println(isDispatcher);
			dao.connectionClose(conn);
		}

		action.setPage(page);
		action.setDispatcher(isDispatcher);

		return action;
	}

	/* 비번 변경 */
	private ActionBean changePassword(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		AuthBean auth = null;
		DataAccessObject dao = null;
		Connection conn = null;
		String page = "logIn.jsp";
		boolean isDispatcher = false;

		// Message 처리
		try {
			page = "?mes=" + URLEncoder.encode("로그인 후 사용하실 수 있습니다.", "UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}

		HttpSession session = req.getSession();

		if(session.getAttribute("employeeCode") != null) {

			auth = new AuthBean();
			auth.setEmployeeCode(req.getParameter("employeeCode"));
			auth.setEmployeePassword(req.getParameter("employeePassword"));

			dao = new DataAccessObject();
			conn = dao.connectionOpen();

			if(this.convertToBool(dao.updEmployeePassword(conn, auth))) {
				page = "StoreMgr";
				isDispatcher = true;
			}

			dao.connectionClose(conn);
		}

		action.setPage(page);
		action.setDispatcher(isDispatcher);
		return action;	
	}

	private ActionBean changeLevel(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		AuthBean auth = null;
		DataAccessObject dao = null;
		Connection conn = null;
		String page = "logIn.jsp";
		boolean isDispatcher = false;

		try {
			page = "?mes=" + URLEncoder.encode("로그인을 하셔야 서비스를 이용하실 수 있습니다.", "UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}

		HttpSession session = req.getSession();

		if(session.getAttribute("employeeCode") != null) {

			auth = new AuthBean();
			auth.setEmployeeCode(req.getParameter("employeeCode")); //GET PARAM으로 바꾸어줘야 지정한걸로 바뀐다
			auth.setEmployeeLevel(req.getParameter("employeeLevel"));

			dao = new DataAccessObject();
			conn = dao.connectionOpen();

			if(this.convertToBool(dao.updEmployeeLevel(conn, auth))) {
				page = "StoreMgr";
				isDispatcher = true;
			}

			dao.connectionClose(conn);
		}

		action.setPage(page);
		action.setDispatcher(isDispatcher);
		return action;
	}

	private ActionBean modifyPhone(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		MemberBean memb = new MemberBean();
		DataAccessObject dao = new DataAccessObject();
		Connection conn;
		String page = "logIn.jsp";
		boolean isDispatcher = false;

		try {
			page = "?mes=" + URLEncoder.encode("로그인 하신후 사용해 주세요." , "UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}
		//세션이 살아있나 없나 유무를 통해 로그인 중인지 확인
		HttpSession session = req.getSession();

		if(session.getAttribute("employeeCode") != null) {
			memb = new MemberBean();
			memb.setMemberCode(req.getParameter("memberCode"));
			memb.setMemberPhone(req.getParameter("memberPhone"));

			dao = new DataAccessObject();
			conn = dao.connectionOpen();

			if(this.convertToBool(dao.updMemberPhone(conn, memb))) {
				page = "StoreMgr";
				isDispatcher = true;
			}		
			dao.connectionClose(conn);
		}

		action.setPage(page);
		action.setDispatcher(isDispatcher);
		return action;
	}

	private ActionBean modifyGoods(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		GoodsBean goods = new GoodsBean();
		AuthBean auth = new AuthBean();
		DataAccessObject dao = null;
		Connection conn = null;
		String page = "logIn.jsp";
		boolean isDispatcher = false;

		// Message 처리
		try {
			page = "?mes=" + URLEncoder.encode("로그인 후 사용하실 수 있습니다.", "UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}

		HttpSession session = req.getSession();

		if(session.getAttribute("employeeCode") != null) {
			goods.setGoodsCode(req.getParameter("goodsCode"));
			goods.setGoodsName(req.getParameter("goodsName"));
			goods.setGoodsCost(Integer.parseInt(req.getParameter("goodsCost")));
			goods.setGoodsPrice(Integer.parseInt(req.getParameter("goodsPrice")));
			goods.setGoodsStock(Integer.parseInt(req.getParameter("goodsStock")));
			goods.setCategoryName(req.getParameter("categoryName"));

			dao = new DataAccessObject();
			conn = dao.connectionOpen();

			if(this.convertToBool(dao.updGoodsInfo(conn, goods, auth))) {
				page = "StoreMgr";
				isDispatcher = true;
			}

			dao.connectionClose(conn);
		}

		action.setPage(page);
		action.setDispatcher(isDispatcher);
		return action;
	}

	private ActionBean addGoods(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		GoodsBean goods = new GoodsBean();
		AuthBean auth = new AuthBean();
		DataAccessObject dao = null;
		Connection conn = null;
		String page = "logIn.jsp";
		boolean isDispatcher = false;

		// Message 처리
		try {
			page = "?mes=" + URLEncoder.encode("로그인 후 사용하실 수 있습니다.", "UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}

		HttpSession session = req.getSession();

		if(session.getAttribute("employeeCode") != null) {
			goods.setGoodsCode(req.getParameter("goodsCode"));
			goods.setGoodsName(req.getParameter("goodsName"));
			goods.setGoodsCost(Integer.parseInt(req.getParameter("goodsCost")));
			goods.setGoodsPrice(Integer.parseInt(req.getParameter("goodsPrice")));
			goods.setGoodsStock(Integer.parseInt(req.getParameter("goodsStock")));
			goods.setCategoryCode(req.getParameter("categoryCode"));

			dao = new DataAccessObject();
			conn = dao.connectionOpen();

			if(this.convertToBool(dao.insGoodsInfo(conn, goods))) {
				page = "StoreMgr";
				isDispatcher = true;
			}

			dao.connectionClose(conn);
		}

		action.setPage(page);
		action.setDispatcher(isDispatcher);
		return action;
	}	
	private boolean convertToBool(int value) {
		return (value == 1)? true : false;
	}

}
