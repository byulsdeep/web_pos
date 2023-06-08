package com.web_pos.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web_pos.beans.ActionBean;
import com.web_pos.services.Authentication;
import com.web_pos.services.ManagementInformation;
import com.web_pos.services.Sales;
import com.web_pos.services.StoreManagements;

@WebServlet({"/Entrance","/LogIn", "/LogOut", "/StoreMgr", "/EmpReg", "/UpdPassword", "/UpdLevel","/UpdPhone", "/UpdGoods", "/InsGoods", "/PosMgr", "/Search", "/Payment", "/CheckMember", "/AccruePoints", "/StatMgr", "/AccessHistory", "/Profit"})
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FrontController() {
		super();
	}

	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		ActionBean action = null;
		Authentication auth = null;
		StoreManagements sm = null;
		Sales pos = null;
		ManagementInformation mi = null;
		String jobCode = null;		

		/* 한글화 지원 --> request.setCharacterEncode(UTF-8)*/
		request.setCharacterEncoding("UTF-8");
		/* JobCode 분리 */
		jobCode = request.getRequestURI().substring(request.getContextPath().length()+1);

		/* JobCode에 따른 서비스(Class) 분기 */
		if(jobCode.equals("Entrance")) {
			auth = new Authentication();
			action = auth.backController(request);
		}else if(jobCode.equals("LogOut")) {
			auth = new Authentication();
			action = auth.backController(request);
		}else if(jobCode.equals("LogIn")) {
			auth = new Authentication();
			action = auth.backController(request);
		}else if(jobCode.equals("EmpReg")) {
			sm = new StoreManagements();
			action = sm.backController(request);
		}else if(jobCode.equals("StoreMgr")) {
			sm = new StoreManagements();
			action = sm.backController(request);	
		}else if(jobCode.equals("UpdPassword")) {
			sm = new StoreManagements();
			action = sm.backController(request);
		}else if(jobCode.equals("UpdLevel")) {
			sm = new StoreManagements();
			action = sm.backController(request);
		}else if(jobCode.equals("UpdPhone")) {
			sm = new StoreManagements();
			action = sm.backController(request);
		}else if(jobCode.equals("UpdGoods")) {
			sm = new StoreManagements();
			action = sm.backController(request);	
		}else if(jobCode.equals("InsGoods")) {
			sm = new StoreManagements();
			action = sm.backController(request);			
		}else if(jobCode.equals("PosMgr")) {
			pos = new Sales();
			action = pos.backController(request);
		}else if(jobCode.equals("Search")) {
			pos = new Sales();
			action = pos.backController(request);
		}else if(jobCode.equals("Payment")) {
			pos = new Sales();
			action = pos.backController(request);
		}else if(jobCode.equals("CheckMember")) {
			pos = new Sales();
			action = pos.backController(request);
		}else if(jobCode.equals("AccruePoints")) {
			pos = new Sales();
			action = pos.backController(request);
		}else if(jobCode.equals("StatMgr")) {
			mi = new ManagementInformation();
			action = mi.backController(request);
		}
		//3111:Xenoblade:44
		if(action.getAjaxData() != null) {
			/* Ajax에 대한 응답 처리 */
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.print(action.getAjaxData());
		} else {
			if(action.isDispatcher()) {
				/* 클라이언트에 대한 응답 지원 : HttpServletRequest >> Dispatcher */
				RequestDispatcher dispatcher = request.getRequestDispatcher(action.getPage());
				dispatcher.forward(request, response);
			}else {
				/* 클라이언트에 대한 응답 지원 : HttpServletResponse */
				response.sendRedirect(action.getPage());
			}
		}
	}

		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			/* 한글화 지원 --> server.xml : URIEncoding=UTF-8 */
			String page = null; 
			switch(request.getRequestURI().substring(request.getContextPath().length()+1)) {
			case "Entrance":
				this.doProcess(request, response);
				break;
			default:
				page = "logIn.jsp?mes=" + URLEncoder.encode("잘못된 경로로 접근하셨습니다.","UTF-8");
				response.sendRedirect(page);
			}
		}

		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
			this.doProcess(request, response);
		}
	}
