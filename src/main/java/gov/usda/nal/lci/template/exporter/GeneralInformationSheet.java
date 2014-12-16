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
import java.util.Map;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Sheet;
import org.openlca.core.model.Process;
/**
 * @author Gary.Moore
 *
 */
public class GeneralInformationSheet extends TemplateSheet {
	//*TODO move this to JSON
	private static final Map<String,Short> rows;
	static
	{
		rows=new HashMap<String,Short>();
		rows.put("basename", Short.valueOf("3"));
		rows.put("treatment", Short.valueOf("4"));
		rows.put("loctype", Short.valueOf("5"));
		rows.put("mixtype", Short.valueOf("6"));
		rows.put("qprod", Short.valueOf("7"));
		rows.put("description", Short.valueOf("8"));
		rows.put("cat0", Short.valueOf("10"));
		rows.put("cat1", Short.valueOf("11"));
		rows.put("cat2", Short.valueOf("12"));
		rows.put("isic", Short.valueOf("13"));
		rows.put("infrastruct", Short.valueOf("14"));
		rows.put("qref", Short.valueOf("16"));
		rows.put("sdate", Short.valueOf("18"));
		rows.put("edate", Short.valueOf("19"));
		rows.put("timecomment", Short.valueOf("20"));
		rows.put("location", Short.valueOf("22"));
		rows.put("geocomment", Short.valueOf("23"));
		rows.put("techcomment", Short.valueOf("25"));
	}
	public GeneralInformationSheet(Sheet s)
	{
		super(s);
	}
	public void updateSheet(Process p) throws TemplateExportException
	{
			Sheet s=this.getSheet();
			checkUpdateSheetParameters(p,s);
			// set title parts
			try {
				String[] title=getTitleParts(p.getName());
				s.getRow(rows.get("basename")).getCell(3).setCellValue(title[0]);
				s.getRow(rows.get("treatment")).getCell(3).setCellValue(title[1]);
				s.getRow(rows.get("loctype")).getCell(3).setCellValue(title[2]);
				s.getRow(rows.get("mixtype")).getCell(3).setCellValue(title[3]);
				s.getRow(rows.get("description")).getCell(3).setCellValue(p.getDescription());
				s.getRow(rows.get("infrastruct")).getCell(3).setCellValue(p.isInfrastructureProcess());
				s.getRow(rows.get("geocomment")).getCell(3).setCellValue(p.getDocumentation().getGeography());
				s.getRow(rows.get("location")).getCell(3).setCellValue(p.getLocation().getCode());
				s.getRow(rows.get("sdate")).getCell(3).setCellValue(p.getDocumentation().getValidFrom());
				s.getRow(rows.get("edate")).getCell(3).setCellValue(p.getDocumentation().getValidUntil());
				s.getRow(rows.get("timecomment")).getCell(3).setCellValue(p.getDocumentation().getTime());
				s.getRow(rows.get("cat0")).getCell(3).setCellValue(p.getCategory().getParentCategory().getName());
				s.getRow(rows.get("cat1")).getCell(3).setCellValue(p.getCategory().getName());
				s.getRow(rows.get("qref")).getCell(3).setCellValue(p.getQuantitativeReference().getFlow().getName());
				s.getRow(rows.get("techcomment")).getCell(3).setCellValue(p.getDocumentation().getTechnology());
			}
			catch ( Exception e){
				throw new TemplateExportException("Unexpected error occured",e);
			}
	}
	/**
	 * Make a String Array of size 4
	 * @param t
	 * @return
	 */
	private String[] getTitleParts(String t)
	{
		String s[]=new String[] {TemplateSheet.DEFAULT_STRING,TemplateSheet.DEFAULT_STRING,TemplateSheet.DEFAULT_STRING,TemplateSheet.DEFAULT_STRING};
		String[] tt=t.split(";");
		for ( int i=0;i<tt.length;i++ )
			s[i]=tt[i];
		return s;
		
	}
}
