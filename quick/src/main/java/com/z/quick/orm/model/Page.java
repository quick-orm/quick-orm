package com.z.quick.orm.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page<E> {
	private static final ThreadLocal<Map<String,Integer>> pageProps = new ThreadLocal<>();
	private Integer pageNum;
	private Integer pageSize;
	private Integer total;
	private Integer pages;
	private Integer nextPage;
	private List<E> result;
	
	public static void page(Integer pageNum,Integer pageSize){
		Map<String,Integer> pageInfo = new HashMap<String, Integer>();
		pageInfo.put("pageNum", pageNum);
		pageInfo.put("pageSize", pageSize);
		pageProps.set(pageInfo);
	}
	public static Map<String,Integer> getPageInfo(){
		return pageProps.get();
	}
	
	public Page(Integer pageNum, Integer pageSize, Integer total, List<E> result) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.total = total;
		this.result = result;
		this.nextPage = pageNum+1;
		pages = total%pageSize==0?total/pageSize:total/pageSize+1;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getPages() {
		return pages;
	}
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	public Integer getNextPage() {
		return nextPage;
	}
	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}
	public List<E> getResult() {
		return result;
	}
	public void setResult(List<E> result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "Page [pageNum=" + pageNum + ", pageSize=" + pageSize + ", total=" + total + ", pages=" + pages
				+ ", nextPage=" + nextPage + ", result=" + result + "]";
	}
	
	
}
