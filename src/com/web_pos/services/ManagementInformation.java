package com.web_pos.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web_pos.beans.ActionBean;
import com.web_pos.beans.AuthBean;
import com.web_pos.beans.SalesBean;
import com.web_pos.dao.DataAccessObject;
import com.web_pos.services.Authentication;

public class ManagementInformation {
	private AuthBean auth;

	public ManagementInformation() {
	}

	public ActionBean backController(HttpServletRequest req) {
		ActionBean action = null;

		if(this.isSession(req)) {
			switch(req.getRequestURI().substring(req.getContextPath().length()+1)){
			case "StatMgr":
				action = this.statMgrCtl(req);
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
	
	private ActionBean statMgrCtl(HttpServletRequest req) {
		ActionBean action = new ActionBean();
		DataAccessObject dao = new DataAccessObject();
		Connection conn = null;

		conn = dao.connectionOpen();

		/* AccessInfo 가져오기*/
		Authentication authentication = new Authentication();
		authentication.getAccessInfo(req, auth, dao, conn);
		
		/* 출퇴근 기록 가져오기 */
		req.setAttribute("accessHistory", this.makeAccessHistory(dao.getAccessHistory(conn)));
		/* 매출 및 영업이익 가져오기 */
		req.setAttribute("sales", this.makeSales(dao.getSales(conn)));
		
		dao.connectionClose(conn);
		/* 응답 방식 및 페이지 설정*/
		action.setDispatcher(true);
		action.setPage("stats.jsp");

		return action;
	}
	
	private String makeAccessHistory(ArrayList<AuthBean> list) {
		StringBuffer sb = new StringBuffer();
		String levelName;
		
		sb.append("<table>");
		sb.append("<tr><th>&nbsp날짜&nbsp</th><th>&nbsp성함&nbsp</th><th>&nbsp직책&nbsp</th><th>&nbsp출근/퇴근&nbsp</th><th>시간</th></tr>");
		
		for(AuthBean auth : list) {
			
			sb.append("<tr onMouseOver=\"changeTrCss(this, true)\" onMouseOut=\"changeTrCss(this, false)\">");
			sb.append("<td>&nbsp" + auth.getAccessDate() + "&nbsp</td>");
			sb.append("<td>&nbsp" + auth.getEmployeeName() + "&nbsp</td>");
			sb.append("<td>&nbsp" + auth.getEmployeeLevel() + "&nbsp</td>");			
			sb.append("<td>&nbsp" + auth.getAccessAction() + "&nbsp</td>");	
			sb.append("<td>&nbsp" + auth.getAccessTime() + "&nbsp</td>");	
			sb.append("</tr>");
		}
	
		sb.append("</table>");
		
		return sb.toString();
	}
	
	private String makeSales(ArrayList<SalesBean> list) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<table>");
		sb.append("<tr><th>&nbsp날짜&nbsp</th><th>&nbsp매출&nbsp</th><th>&nbsp영업이익&nbsp</th></tr>");
		
		for(SalesBean sales : list) {
			sb.append("<tr onMouseOver=\"changeTrCss(this, true)\" onMouseOut=\"changeTrCss(this, false)\">");
			sb.append("<td>&nbsp" + sales.getDate() + "&nbsp</td>");
			sb.append("<td>&nbsp" + sales.getRevenue() + "&nbsp</td>");
			sb.append("<td>&nbsp" + sales.getProfit() + "&nbsp</td>");
			sb.append("</tr>");
		}
	
		sb.append("</table>");
		
		return sb.toString();
	}
	
	private boolean convertToBool(int value) {
		return (value == 1)? true : false;
	}
	
	
}
