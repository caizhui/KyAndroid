package com.ky.kyandroid.bean;

import org.json.JSONObject;

import java.util.List;

/**
 * 
 * @author caizhui
 *
 */
public class PageBean {

	// 开始
	private int start;

	// 结束
	private int end;

	// 记录总数
	private int total;

	// 页数
	private int pageSize;

	// 当前页
	private int currentPage = 1;

	// 每页多少条数据
	private int limit;

	// 总记录数
	private int totalCount;

	// 数据
	private List<JSONObject> dataList;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<JSONObject> getDataList() {
		return dataList;
	}

	public void setDataList(List<JSONObject> dataList) {
		this.dataList = dataList;
	}
}
