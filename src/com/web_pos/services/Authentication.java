package com.web_pos.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web_pos.beans.ActionBean;
import com.web_pos.beans.AuthBean;
import com.web_pos.dao.DataAccessObject;

/* 로그인(Access), 로그아웃(AccessOut), 매장가입(RegStore), 직원등록(RegEmployee), 액세스상태 */
public class Authentication {

	public Authentication(){}

	public ActionBean backController(HttpServletRequest req) {
		ActionBean action = null;
		/* jobCode 분리 */
		/* jobCode에 따른 제어메서드 분기 */
		switch(req.getRequestURI().substring(req.getContextPath().length()+1)){
		case "LogIn":
			action = this.entrance(req);
			break;
		case "LogOut":
			action = this.logOutCtl(req);
			break;
		case "Entrance":
			action = entrance(req);
			break;
		}
		
		return action;
	}

	/* 현재 로그인 상태에 따라 분기 
	 *  1. 첫 페이지 호출 & Access 호출 
	 * */
	private ActionBean entrance(HttpServletRequest req) {
		ActionBean action = null;
		AuthBean auth = null;
		DataAccessObject dao = null;
		Connection conn = null;

		/* Session 정보 확인 */
		HttpSession session = req.getSession();
		auth = new AuthBean();

		/* DataAccessObject */
		dao = new DataAccessObject();
		conn = dao.connectionOpen();
		if(session.getAttribute("employeeCode") != null) {
			auth.setEmployeeCode((String)session.getAttribute("employeeCode"));
			System.out.println("employeeCode in session");
			this.getAccessInfo(req, auth, dao, conn);
			req.setAttribute("menu", this.makeHtml((String)req.getAttribute("levelName")));

			action = new ActionBean();
			action.setPage("success.jsp");
			action.setDispatcher(true);
		}else {
			if(req.getParameter("employeeCode") != null) {
				auth.setEmployeeCode(req.getParameter("employeeCode"));
				auth.setEmployeePassword(req.getParameter("employeePassword"));
				auth.setAccessAction(1);
				action = this.logInCtl(req, auth, dao, conn);

				if(action.isDispatcher()) {
					this.getAccessInfo(req, auth, dao, conn);
					req.setAttribute("menu", this.makeHtml((String)req.getAttribute("levelName")));
				}

			}else {
				action = new ActionBean();
				action.setPage("logIn.jsp");
				action.setDispatcher(false);
			}
		}
		dao.connectionClose(conn);
		return action;
	}

	void getAccessInfo(HttpServletRequest req, AuthBean auth, DataAccessObject dao, Connection conn) {
		ArrayList<AuthBean> list = null;

		/* 매장이름 + 직원이름 + AccessTime + 직원등급 >> (SELECT) */
		list = dao.getUserInfo(conn, auth);
		
		
		req.setAttribute("employeeName", list.get(0).getEmployeeName());
		req.setAttribute("accessTime", list.get(0).getAccessTime());
		req.setAttribute("employeeLevel", list.get(0).getEmployeeLevel());
		
		if(list.get(0).getEmployeeLevel().equals("PR")) {
			req.setAttribute("levelName", "대표");
		} else if (list.get(0).getEmployeeLevel().equals("MA")) {
			req.setAttribute("levelName", "매니저");
		} else {
			req.setAttribute("levelName", "아르바이트");
		}

	}

	/* 로그인 : 매장코드(C), 직원코드(C), 패스워드(C) >> request 
	 * storeCode=1111111111&employeeCode=101&employeePassword=4321 
	 * String storeCode = 1111111111 */
	private ActionBean logInCtl(HttpServletRequest req, AuthBean auth, DataAccessObject dao, Connection conn) {
		HttpSession session = null;
		ActionBean action = new ActionBean();
		String page = "logIn.jsp";
		boolean isDispatcher = false;
		String message = null;
		ArrayList<AuthBean> list = null;
		String levelName = null;
		
		/* StoreCode 검증 */
		if(this.convertToBool(dao.isEmployeeCode(conn, auth))){
			/* Employee 검증 */
			if(this.convertToBool(dao.isAccess(conn, auth))) {
				if(dao.accessState(conn, auth) == 1) {
					auth.setAccessAction(-1);
					dao.insAccessHistory(conn, auth);
					auth.setAccessAction(1);
				}
				/* Log */
				if(this.convertToBool(dao.insAccessHistory(conn, auth))) {
					/* Session 저장 */
					session = req.getSession();
					session.setAttribute("employeeCode", auth.getEmployeeCode());
					session.setMaxInactiveInterval(99999);
					
					page = "success.jsp";
					isDispatcher = true;
					
					list = dao.getUserInfo(conn, auth);
					if(list.get(0).getEmployeeLevel().equals("PR")) {
						levelName = "대표";
					} else if(list.get(0).getEmployeeLevel().equals("MA")) {
						levelName = "매니저";
					} else {
						levelName = "아르바이트";
					}
					
					message = "환영합니다" + list.get(0).getEmployeeName() + "(" + levelName + ")" + "님";

				}else {
					message = "이전 불안정한 네트워크로 로그아웃 처리가 되었습니다. 다시 시도해 주세요"; 
				}
			}else {
				message = "로그인 정보를 확인해 주세요~"; 
			}
		}else {
			message ="매장 가입을 해주신 후 로그인 해 주세요~"; 
		}

		if(message != null) {
			try {page += "?mes=" + URLEncoder.encode(message, "UTF-8");} 
			catch (UnsupportedEncodingException e) {e.printStackTrace();}
		}

		action.setPage(page);
		action.setDispatcher(isDispatcher);
		return action;
	}

	private String makeHtml(String level) {
		/* 상품판매와 매장관리로 분기 */
		StringBuffer sb = new StringBuffer();
		//sb.append("<div id=\"outline\">");
		sb.append("<i id=\"PosMgr\" class=\"fa-solid fa-cash-register\" onClick=\"movePage(\'" + level + "\', \'PosMgr\')\"　\">상품판매</i>");
		//sb.append("<div id=\"space\"></div>");
		sb.append("<i id=\"StoreMgr\" class=\"fa-solid fa-store\" onClick=\"movePage(\'" + level + "\', \'StoreMgr\')\" \">매장관리</i>");
		//sb.append("<div id=\"space\"></div>");
		sb.append("<i id=\"StatMgr\" class=\"fa-solid fa-file-lines\" onClick=\"movePage(\'" + level + "\', \'StatMgr\')\" \">관리정보</i>");
		//sb.append("</div>");
		return sb.toString();
	}
	/* 로그아웃 : 매장코드(C), 직원코드(C) >> request */
	private ActionBean logOutCtl(HttpServletRequest req) {
		HttpSession session = null;
		/* 응답과 관련된 데이터 타입*/
		ActionBean action = new ActionBean();
		boolean isDispatcher = false;
		String page = "logIn.jsp";
		String message = null;

		/* DAO 관련 데이터타입*/
		Connection conn = null;
		AuthBean auth = new AuthBean();

		session = req.getSession();
		auth.setEmployeeCode((String)session.getAttribute("employeeCode"));
		
		auth.setAccessAction(-1);

		/* DAO */
		DataAccessObject dao = new DataAccessObject();
		conn = dao.connectionOpen();

		/* Detail Process */
		/* 로그아웃 계정의 현재 상태 확인*/
		if(this.convertToBool(dao.accessState(conn, auth))) {
			if(this.convertToBool(dao.insAccessHistory(conn, auth))) {
				session.invalidate();
				message = "정상적으로 로그아웃 되었습니다.";
			}else {
				message = "로그아웃은 되었지만 데이터베이스에는 반영되지 않았습니다.";
			}
		}else {
			message = "이미 로그아웃 되었습니다."; 
		}
		dao.connectionClose(conn);

		try {
			if(message != null)	page += "?mes=" + URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}

		action.setDispatcher(isDispatcher);
		action.setPage(page);

		return action;
	}

	private boolean convertToBool(int value) {
		return (value == 1)? true : false;
	}
}
