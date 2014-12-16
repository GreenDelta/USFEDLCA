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
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.ProcessDao;
import org.openlca.core.model.Process;
import org.openlca.io.ImportEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import gov.usda.nal.lci.template.consts.Consts;
import gov.usda.nal.lci.template.excel.ExcelWriter;


/**
 * @author Gary.Moore
 *
 */
public class USFedLCAExporter implements Runnable {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private EventBus eventBus;
	IDatabase database;
	Long pid;
	String outfile;
	String template=ExcelWriter.WB_TEMPLATE;
	ExcelWriter writer;
	
	public ExcelWriter getWriter() {
		return this.writer;
	}
	public void setWriter(ExcelWriter writer) {
		this.writer = writer;
	}
	public USFedLCAExporter(IDatabase db)
	{
		setDatabase(db);
	}
	public USFedLCAExporter(IDatabase db,Long id)
	{
		setDatabase(db);
		setPid(id);
	}
	public USFedLCAExporter(IDatabase db,Long id,String of)
	{
		setDatabase(db);
		setPid(id);
		setOutfile(of);
	}
	public IDatabase getDatabase() {
		return this.database;
	}
	public void setDatabase(IDatabase database) {
		this.database = database;
	}
	public Long getPid() {
		return this.pid;
	}
	public void setPid(Long pid) {
		this.pid=pid;
	}
	
	public String getOutfile() {
		return this.outfile;
	}
	public void setOutfile(String outfile) {
		this.outfile = outfile;
	}
	public EventBus getEventBus() {
		return this.eventBus;
	}
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	public String getTemplate() {
		return this.template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			fireEvent(this.outfile);
			ProcessDao dao = new ProcessDao(database);
			Process process = dao.getForId(this.pid);
			if (process == null || process.getDocumentation() == null) {
				log.error("process "+this.pid.toString()+" was null or has no documentation: "
						+ "not exported");
			}
			else
			{
				log.trace("Creating output template "+ExcelWriter.WB_TEMPLATE);
				java.net.URL url=getClass().getResource(ExcelWriter.WB_TEMPLATE);
				this.writer=new ExcelWriter(url.openStream(),new FileOutputStream(this.outfile));
				createWorkBook(process);
				writer.writeWorkbook();			
			}
		}
		catch( Exception e)
		{
			log.error("failed to export process to FedLCATemplate", e);
		}
	}
	
	public void createWorkBook(Process p)
	{
		try {
			new GeneralInformationSheet(this.getWriter().getWorkbook().getSheetAt(Consts.GENERAL_INFORMATION_PAGE)).updateSheet(p);
			new AdminstrativeInformationSheet(this.getWriter().getWorkbook().getSheetAt(Consts.ADMINISTRATIVE_INFORMATION_PAGE)).updateSheet(p);
			new ModellingAndValidationSheet(this.getWriter().getWorkbook().getSheetAt(Consts.MODELING_VALIDATION_PAGE)).updateSheet(p);
			new ActorsSheet(this.getWriter().getWorkbook().getSheetAt(Consts.ACTORS_PAGE)).updateSheet(p);
			new ParametersSheet(this.getWriter().getWorkbook().getSheetAt(Consts.PARAMETERS_PAGE)).updateSheet(p);
			new SourcesSheet(this.getWriter().getWorkbook().getSheetAt(Consts.SOURCES_PAGE)).updateSheet(p);
			new InputOutputSheet(this.getWriter().getWorkbook().getSheetAt(Consts.EXCHANGES_PAGE)).updateSheet(p);
			new CostsSheet(this.getWriter().getWorkbook().getSheetAt(Consts.COSTS_PAGE)).updateSheet(p);
		}
		catch (TemplateExportException e )
		{
			log.error("failed to export process to FedLCATemplate",e);
		}
	}
	
	private void getProcessCostEntry(Long id )
	{
		
	}
	private void fireEvent(String dataSetName) {
		log.trace("import data set {}", dataSetName);
		if (this.eventBus == null)
			return;
		this.eventBus.post(new ImportEvent(dataSetName));
	}
	
}