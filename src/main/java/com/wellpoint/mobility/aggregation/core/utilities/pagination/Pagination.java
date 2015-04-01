package com.wellpoint.mobility.aggregation.core.utilities.pagination;

public class Pagination
{
	/**
	 * requestPageNumber
	 */
	private Long pageNo;
	/**
	 * clientRowsPerPage
	 * 
	 */
	private Long rowsPerPage;
	/**
	 * Total rows - this is an out parameter set by the ESBContextSoapHandler
	 */
	private Long totalRows;
	/**
	 * Total pages - this is an out parameter set by the ESBContextSoapHandler
	 */
	private Long totalPages;

	/**
	 * @return the pageNo
	 */
	public Long getPageNo()
	{
		return pageNo;
	}

	/**
	 * @param pageNo
	 *            the pageNo to set
	 */
	public void setPageNo(Long pageNo)
	{
		this.pageNo = pageNo;
	}

	/**
	 * @return the rowsPerPage
	 */
	public Long getRowsPerPage()
	{
		return rowsPerPage;
	}

	/**
	 * @param rowsPerPage
	 *            the rowsPerPage to set
	 */
	public void setRowsPerPage(Long rowsPerPage)
	{
		this.rowsPerPage = rowsPerPage;
	}

	/**
	 * @return the totalRows
	 */
	public Long getTotalRows()
	{
		return totalRows;
	}

	/**
	 * @param totalRows
	 *            the totalRows to set
	 */
	public void setTotalRows(Long totalRows)
	{
		this.totalRows = totalRows;
	}

	/**
	 * @return the totalPages
	 */
	public Long getTotalPages()
	{
		return totalPages;
	}

	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(Long totalPages)
	{
		this.totalPages = totalPages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Pagination [pageNo=" + pageNo + ", rowsPerPage=" + rowsPerPage + ", totalRows=" + totalRows + ", totalPages=" + totalPages + "]";
	}

}
