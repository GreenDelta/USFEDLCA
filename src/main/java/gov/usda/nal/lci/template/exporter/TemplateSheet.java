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
package gov.usda.nal.lci.template.exporter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.openlca.core.model.Category;
import org.openlca.core.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Base class for transferring contents of a Process object to the Excel Worksheets which make up 
 * a "FedLCATemplate".
 * @author Gary.Moore
 *
 */
public abstract class TemplateSheet {
	public static String DEFAULT_STRING="";
	private Sheet sheet;
	public TemplateSheet()
	{
		
	}
	/**
	 * @param s
	 */
	public TemplateSheet(Sheet s) {
		super();
		this.sheet = s;
	}
	public Sheet getSheet() {
		return this.sheet;
	}
	public void setSheet(Sheet s) {
		this.sheet = s;
	}
	/**
	 * The <code>updateSheet</code> method transfers fields in the Process p to attributes in the worksheet. 
	 * @param p
	 * @param s
	 */
	public abstract void updateSheet(Process p) throws TemplateExportException;
	/**
	 * Returns true if an Object is not null
	 * @param o
	 * @return
	 */
	public boolean isNotNull(Object o)
	{
		return o != null;
	}
	/**
	 * <code>checkUpdateSheetParameters</code> used to throw <@link gov.usda.nal.lci.template.exporter.TemplateExportException> exception when
	 * one or both parameters to updateSheet method are null.
	 * @param p
	 * @param s
	 * @throws TemplateExportException
	 */
	public void checkUpdateSheetParameters(Process p, Sheet s) throws TemplateExportException
	{
		if ( p == null && s == null )
			throw new TemplateExportException("Cannot update sheet if process and sheet are null");
		else if ( p == null )
			throw new TemplateExportException("Cannot update sheet if process is null");
		else if ( s == null )
			throw new TemplateExportException("Cannot update sheet if sheet is null");
	}
	/**
	 * Makes a copy of row s, inserts it into sheet at row r and returns the new Row
	 * @return Row
	 * @throws IOException 
	 */
	public Row insertRow(Row s,int r)
	{
		
    	 Row row = this.getSheet().createRow(r);
    	 row.setRowStyle(s.getRowStyle());
    	 row.setRowStyle(s.getRowStyle());
    	 insertCells(s,row);
    	 return row;
	}
	/**
	 * Copies cells in Row s to Row r
	 * @param s
	 * @param r
	 */
	public void insertCells(Row s,Row r)
	{
		for (int i = 1; i < s.getLastCellNum(); i++) {
            Cell newCell = r.createCell(i);
            if ( isNotNull(s.getCell(i)))
            	newCell.setCellStyle(s.getCell(i).getCellStyle());
            newCell.setCellValue("");
        }
	}
	public String categoryToString(Category c )
	{
		StringBuffer sb=new StringBuffer();
		if ( isNotNull(c))
		{
			if ( isNotNull(c.getParentCategory()))
				sb.append(getParentCategory(c)+" ");
			if ( isNotNull(c.getName()))
				sb.append(getSubCategory(c));
		}
		return sb.toString();
	}
	public String getParentCategory(Category c)
	{
		String s="";
		if ( isNotNull(c) && isNotNull(c.getParentCategory()))
			s=c.getParentCategory().getName();
		return s;
	}
	public String getSubCategory(Category c)
	{
		String s="";
		if ( isNotNull(c) && isNotNull(c.getName()))
			s=c.getName();
		return s;
	}
}