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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openlca.core.model.AllocationFactor;
import org.openlca.core.model.AllocationMethod;
import org.openlca.core.model.Exchange;
import org.openlca.core.model.Process;
import org.openlca.core.model.Flow;
import org.openlca.core.database.FlowDao;
import org.openlca.core.database.IDatabase;

/**
 * @author Gary.Moore
 *
 */
public class AllocationSheet extends TemplateSheet implements IAllocationSheet {
	private static final Map<String,Short> cols;
	static
	{
		cols=new HashMap<String,Short>();
		cols.put("coproduct", Short.valueOf("8"));
		cols.put("physical", Short.valueOf("8"));
		cols.put("economic", Short.valueOf("8"));
		cols.put("ig", Short.valueOf("3"));
		cols.put("og", Short.valueOf("4"));
		cols.put("flow", Short.valueOf("5"));
		cols.put("category", Short.valueOf("6"));
		cols.put("subcategory", Short.valueOf("7"));
		cols.put("value", Short.valueOf("8"));
		
	}
	public AllocationSheet(Sheet s)
	{
		super(s);
	}
	/**
	 * A stub which throws an exception if called since an IDatabase connection is required to
	 * export an AllocationSheet
	 */
	public void updateSheet(Process p) throws TemplateExportException
	{
		throw new TemplateExportException("Database connection required!");
	}
	/**
	 *  <code>updateSheet</code> requires a bit of extra work to get the data into the template structure which
	 * is complicated by the fact that we have a variable number of up to 20 co-product columns and that some
	 * cells for some allocations may be blank, i.e. we don't have a value in our allocation factors list.  Therefore,
	 * we need to create a sparse array to make sure that the allocation values get put to the correct cell.
	 * @see gov.usda.nal.lci.template.exporter.TemplateSheet#updateSheet(org.openlca.core.model.Process)
	 */
	public void updateSheet(Process p,IDatabase db) throws TemplateExportException 
	{
		Sheet s=this.getSheet();
		checkUpdateSheetParameters(p,s);
		try {
			Long coprodid;
			FlowDao dao=new FlowDao(db);
			
			Map<Long,ArrayList<AllocationFactor>> coprodmap=new TreeMap<Long,ArrayList<AllocationFactor>>();
			ArrayList<AllocationFactor> coprodlist;
			// group the allocation factors for the process by co-product id
			// this allows us to size our arrays, etc.
			for (AllocationFactor f : p.getAllocationFactors() )
			{
				coprodid=f.getProductId();
				coprodlist=coprodmap.get(coprodid);
				if ( coprodlist == null )
					coprodlist=new ArrayList<AllocationFactor>();
				coprodlist.add(f);
				coprodmap.put(coprodid, coprodlist);
			}
			HashMap<Long,Short> cooffs=new HashMap<Long,Short>();
			Short offset=0;
			// Use the coproduct map to size the array that will hold all our values
			Double values[]=new Double[coprodmap.size()];
			HashMap<Long,Double[]> varray=new HashMap<Long,Double[]>();
			HashMap<Long,Exchange> exchanges=new HashMap<Long,Exchange>();
			// make an offset list to insure that each coproduct allocation has the same number of columns
			// in the output sheet
			for (Entry<Long, ArrayList<AllocationFactor>> m : coprodmap.entrySet() )
			{
				coprodid=m.getKey();
				cooffs.put(coprodid, offset++);
			}
			offset=0;
			// build the arrays of allocation values and add them to a list collated by allocation method
			for (Entry<Long,ArrayList<AllocationFactor>> m : coprodmap.entrySet())
			{
				Row r=s.getRow(2);
				if ( r == null ) // add the row if it doesn't exist
					r=insertRow(s.getRow(2),2);
				if ( r.getCell(cols.get("coproduct")+offset) == null ) // add cells to the row if it has none
					insertCells(s.getRow(2),r);
				// fetch the flow name from the database for each co-product
				Flow coprodflow=dao.getForId(m.getKey());
				r.getCell(cols.get("coproduct")+offset++).setCellValue(coprodflow.getName());
				coprodlist=m.getValue();
				for ( AllocationFactor f : coprodlist)
				{
					
					if ( f.getAllocationType().equals(AllocationMethod.ECONOMIC))
					{
						
						if ( varray.get(0L) == null  )
							varray.put(0L, new Double[coprodmap.size()]);
						values=varray.get(0L);
						values[cooffs.get(f.getProductId())]=f.getValue();
						varray.put(0L,values);
					}
					else if ( f.getAllocationType().equals(AllocationMethod.PHYSICAL))
					{
						
						if ( varray.get(1L) == null  )
							varray.put(1L, new Double[coprodmap.size()]);
						values=varray.get(1L);
						values[cooffs.get(f.getProductId())]=f.getValue();
						varray.put(1L,values);
					}
					else if ( f.getAllocationType().equals(AllocationMethod.CAUSAL))
					{
						Long eid=f.getExchange().getId();
						exchanges.put(eid, f.getExchange());
						if ( varray.get(eid) == null  )
							varray.put(eid, new Double[coprodmap.size()]);
						values=varray.get(eid);
						values[cooffs.get(f.getProductId())]=f.getValue();
						varray.put(eid,values);
					}
				}
			}
			
			// write out physical allocation values from our varray list of values
			Double[] vv=varray.get(1L);
			for ( int i =0; i<vv.length;i++)
			{
				Row r=s.getRow(3); 
				if ( r == null ) // add the row if it doesn't exist
					r=insertRow(s.getRow(3),3);
				if ( r.getCell(cols.get("physical")+i) == null ) // add cells to the row if it has none
					insertCells(s.getRow(3),r);
				r.getCell(cols.get("physical")+i).setCellValue(vv[i]);
			}
			// write out economic allocation values from our varray list of values
			vv=varray.get(0L);
			for ( int i=0;i<vv.length;i++)
			{
				Row r=s.getRow(4);
				if ( r == null ) // add the row if it doesn't exist
					r=insertRow(s.getRow(4),4);
				if ( r.getCell(cols.get("economic")+i) == null ) // add cells to the row if it has none
					insertCells(s.getRow(4),r);
				r.getCell(cols.get("economic")+i).setCellValue(vv[i]);
			}
			// last, the causal allocations (by exchange) from our varray list of values
			int rn=7;
			for (Entry<Long,Double[]> v : varray.entrySet())
			{
				Long id=v.getKey();
				if ( id <= 1L )
					continue;
				Exchange e=exchanges.get(id);
				vv=v.getValue();
				Row r=s.getRow(rn);
				if ( r == null ) // add the row if it doesn't exist
					r=insertRow(s.getRow(rn),rn);
				r.getCell(cols.get("flow")).setCellValue(e.getFlow().getName());
				r.getCell(cols.get("category")).setCellValue(getParentCategory(e.getFlow().getCategory()));
				r.getCell(cols.get("subcategory")).setCellValue(getSubCategory(e.getFlow().getCategory()));
				if ( e.isInput() )
					r.getCell(cols.get("ig")).setCellValue(getInputGroup(e));
				else
					r.getCell(cols.get("og")).setCellValue(getOutputGroup(e,p.getQuantitativeReference().getId()));
				// write the allocation values for each coproduct
				for ( int i=0;i<vv.length;i++) {
					if ( r.getCell(cols.get("value")+i) == null ) // add cells to the row if it has none
						insertCells(s.getRow(rn),r);
					r.getCell(cols.get("value")+i).setCellValue(vv[i]);
				}
				rn++;
			}
			// glad that's over!
		}
		catch ( Exception e) {
			throw new TemplateExportException("Unexpected error occured",e);
		}
		

	}

}
