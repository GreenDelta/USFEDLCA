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

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openlca.core.model.Process;
import org.openlca.core.model.Source;

/**
 * @author Gary.Moore
 *
 */
public class SourcesSheet extends TemplateSheet {

	private static final Map<String,Short> cols;
	static
	{
		cols=new HashMap<String,Short>();
		cols.put("name", Short.valueOf("1"));
		cols.put("description", Short.valueOf("2"));
		cols.put("category", Short.valueOf("3"));
		cols.put("doi", Short.valueOf("4"));
		cols.put("ref", Short.valueOf("5"));
		cols.put("year", Short.valueOf("6"));
		
		
		
	}
	public SourcesSheet(Sheet s)
	{
		super(s);
	}
	/* (non-Javadoc)
	 * @see gov.usda.nal.lci.template.exporter.TemplateSheet#updateSheet(org.openlca.core.model.Process, org.apache.poi.ss.usermodel.Sheet)
	 */
	@Override
	public void updateSheet(Process p) throws TemplateExportException {
		 Sheet s=this.getSheet();
		checkUpdateSheetParameters(p,s);
		try {
			int fr=3;
			int rn=fr;
			for ( Source so : p.getDocumentation().getSources() ) {
				Row r=s.getRow(rn++);
				if ( r == null ) // add the row if it doesn't exist
					r=insertRow(s.getRow(fr),rn-1);
				else if ( r.getCell(cols.get("name")) == null ) // add cells to the row if it has none
					insertCells(s.getRow(fr),r);
				r.getCell(cols.get("name")).setCellValue(so.getName());
				r.getCell(cols.get("description")).setCellValue(so.getDescription());
				r.getCell(cols.get("doi")).setCellValue(so.getDoi());
				r.getCell(cols.get("category")).setCellValue(categoryToString(so.getCategory()));
				r.getCell(cols.get("ref")).setCellValue(so.getTextReference());
				if ( isNotNull(so.getYear()))
					r.getCell(cols.get("year")).setCellValue(so.getYear());
			}
				
		}
		catch ( Exception e) {
			throw new TemplateExportException("Unexpected error occured",e);
		}

	}

}
