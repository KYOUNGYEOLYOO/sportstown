package com.bluecapsystem.cms.jincheon.sportstown.json.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.bcs.util.EmptyChecker;
import com.bluecapsystem.cms.core.data.condition.Paging;

public class JqGridParameterParser {

	/**
	 * http request 에서 paging 관련 parameter 를 꺼내온다
	 *
	 * @param request
	 * @return
	 */
	public static Paging getPaging(HttpServletRequest request) {
		Integer page = null, rowCount = null;
		String temp = null;
		temp = request.getParameter("page");
		if (EmptyChecker.isNotEmpty(temp)) {
			try {
				page = Integer.parseInt(temp);
			} catch (Exception ex) {
				page = -1;
			} finally {
				page = page > 0 ? page : 0;
			}
		}else {
			page = 0;
		}

		temp = request.getParameter("rows");
		if (EmptyChecker.isNotEmpty(temp)) {
			try {
				rowCount = Integer.parseInt(temp);
			} catch (Exception ex) {
				rowCount = -1;
			} finally {
				rowCount = rowCount > 0 ? rowCount : Integer.MAX_VALUE;
			}
		} else {
			rowCount = Integer.MAX_VALUE;
		}

		return new Paging(page, rowCount, true);
	}

	/**
	 * model 에 paging 데이터를 넣어 준다
	 *
	 * @param mnv
	 * @param paging
	 */
	public static void setPaging(ModelAndView mnv, Paging paging) {

		mnv.addObject("records", paging.getTotalCount());
		mnv.addObject("total", paging.getTotalPageCount());
		mnv.addObject("page", paging.getPage());
	}
}
