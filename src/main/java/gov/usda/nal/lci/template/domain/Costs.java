package gov.usda.nal.lci.template.domain;
/** ===========================================================================
*
*                            PUBLIC DOMAIN NOTICE
*               		National Agriculture Library
*
*  This software/database is a "United States Government Work" under the
*  terms of the United States Copyright Act.  It was written as part of
*  the author's official duties as a United States Government employee and
*  thus cannot be copyrighted.  This software/database is freely available
*  to the public for use. The National Agriculture Library and the U.S.
*  Government have not placed any restriction on its use or reproduction.
*
*  Although all reasonable efforts have been taken to ensure the accuracy
*  and reliability of the software and data, the NAL and the U.S.
*  Government do not and cannot warrant the performance or results that
*  may be obtained by using this software or data. The NAL and the U.S.
*  Government disclaim all warranties, express or implied, including
*  warranties of performance, merchantability or fitness for any particular
*  purpose.
*
*  Please cite the author in any work or product based on this material.
*
*===========================================================================
*/
import java.io.Serializable;

public class Costs implements Serializable {

    private final static long serialVersionUID = 1L;
	
	private long id;
	private String costCategory;  
	private Double amount;
	private Boolean costFixed;    
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * represent tbl_cost_category.name
	 */
	public String getCostCategory() {
		return costCategory;
	}
	public void setCostCategory(String costCategory) {
		this.costCategory = costCategory;
	}
	/**
	 * represent tbl_process_cost_entries.amount
	 */
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * represent tbl_cost_category.fix
	 */
	public Boolean getCostFixed() {
		return costFixed;
	}
	public void setCostFixed(Boolean costFixed) {
		this.costFixed = costFixed;
	}
	@Override
	public String toString() {
		return "Costs [id=" + id + ", costCategory=" + costCategory
				+ ", amount=" + amount + ", costFixed=" + costFixed + "]";
	}
	
	

}
