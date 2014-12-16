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
import org.openlca.core.model.ProcessCostEntry;

/**
 * @author Gary.Moore
 *
 */
public class CostsSheet extends TemplateSheet {
	public CostsSheet(Sheet s)
	{
		super(s);
	}
	
	private static final Map<String,Short> cols;
	static
	{
		cols=new HashMap<String,Short>();
		cols.put("category", Short.valueOf("1"));
		cols.put("amount", Short.valueOf("2"));
		cols.put("fixed", Short.valueOf("3"));
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
			for ( ProcessCostEntry pe : p.getCostEntries() )
			{
				Row r=s.getRow(rn++);
				if ( r == null ) // add the row if it doesn't exist
					r=insertRow(s.getRow(fr),rn-1);
				else if ( r.getCell(cols.get("amount")) == null ) // add cells to the row if it has none
					insertCells(s.getRow(fr),r);
				r.getCell(cols.get("category")).setCellValue(pe.getCostCategory().getName());
				r.getCell(cols.get("amount")).setCellValue(pe.getAmount());
				r.getCell(cols.get("fixed")).setCellValue(pe.getCostCategory().isFix()?"true":"false");
			}
		}
		catch ( Exception e)	{
			throw new TemplateExportException("Unexpected error occured",e);
		}

	}

}
