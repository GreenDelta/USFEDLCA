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
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.openlca.core.model.Actor;
import org.openlca.core.model.Process;

import gov.usda.nal.lci.template.exporter.TemplateSheet;

/**
 * @author Gary.Moore
 *
 */
public class ActorsSheet extends TemplateSheet {
	public ActorsSheet(Sheet s)
	{
		super(s);
	}
	private static final Map<String,Short> cols;
	static
	{
		cols=new HashMap<String,Short>();
		cols.put("name", Short.valueOf("1"));
		cols.put("description", Short.valueOf("2"));
		cols.put("address", Short.valueOf("3"));
		cols.put("city", Short.valueOf("4"));
		cols.put("country", Short.valueOf("5"));
		cols.put("email", Short.valueOf("6"));
		cols.put("fax", Short.valueOf("7"));
		cols.put("phone", Short.valueOf("8"));
		cols.put("website", Short.valueOf("9"));
		cols.put("zip", Short.valueOf("10"));
		
	}
	/* (non-Javadoc)
	 * @see gov.usda.nal.lci.template.exporter.TemplateSheet#updateSheet(org.openlca.core.model.Process, org.apache.poi.ss.usermodel.Sheet)
	 */
	@Override
	public void updateSheet(Process p) throws TemplateExportException {
		Sheet s=this.getSheet();
		checkUpdateSheetParameters(p,s);
		// put all of the actors in a local Set
		Set<Actor> aid=new HashSet<Actor>();
		if (isNotNull(p.getDocumentation().getDataGenerator()))
			aid.add(p.getDocumentation().getDataGenerator());
		if ( isNotNull(p.getDocumentation().getDataSetOwner()))
			aid.add(p.getDocumentation().getDataSetOwner());
		if ( isNotNull(p.getDocumentation().getDataDocumentor()))
			aid.add(p.getDocumentation().getDataSetOwner());
		if ( isNotNull(p.getDocumentation().getReviewer()))
			aid.add(p.getDocumentation().getReviewer());
		aid.remove(null);
		try {
			Iterator<Actor> i=aid.iterator();
			int fr=3; // first available row
			// add the Actors in the local set to the worksheet
			int rn=fr;
			while ( i.hasNext())
			{
				Actor a=i.next();
				Row r=s.getRow(rn++);
				if ( r == null ) // add the row if it doesn't exist
					r=insertRow(s.getRow(fr),fr);
				else if ( r.getCell(cols.get("name")) == null ) // add cells to the row if it has none
					insertCells(s.getRow(fr),r);
				r.getCell(cols.get("name")).setCellValue(a.getName());
				r.getCell(cols.get("description")).setCellValue(a.getDescription());
				r.getCell(cols.get("address")).setCellValue(a.getAddress());
				r.getCell(cols.get("city")).setCellValue(a.getCity());
				r.getCell(cols.get("country")).setCellValue(a.getCountry());
				r.getCell(cols.get("email")).setCellValue(a.getEmail());
				r.getCell(cols.get("fax")).setCellValue(a.getTelefax());
				r.getCell(cols.get("phone")).setCellValue(a.getTelephone());
				r.getCell(cols.get("website")).setCellValue(a.getWebsite());
				r.getCell(cols.get("zip")).setCellValue(a.getZipCode());
				
			}
		}
		catch ( Exception e){
			throw new TemplateExportException("Unexpected error occured",e);
		}

	}

}
