package gov.usda.nal.lci.template.vo;
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
/**
 *  Reading Values from Excel File 
 */
import java.io.Serializable;

public class CostsVO extends BaseVO {
	private final static long serialVersionUID = 1L;
	
	private String emptyColumn;
	private String costCategory;
	private String amount;
	private String costFixed;
	
	public String getEmptyColumn() {
		return emptyColumn;
	}
	public void setEmptyColumn(String emptyColumn) {
		this.emptyColumn = emptyColumn;
	}
	public String getCostCategory() {
		return costCategory;
	}
	public void setCostCategory(String costCategory) {
		this.costCategory = costCategory;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCostFixed() {
		return costFixed;
	}
	public void setCostFixed(String costFixed) {
		this.costFixed = costFixed;
	}
	
	

}
