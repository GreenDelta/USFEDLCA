package gov.usda.nal.lci.template.excel;

import gov.usda.nal.lci.template.exporter.USFedLCAExporter;

import java.io.FileInputStream;
import java.io.IOException;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.InputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * @author Gary.Moore
 *
 */
public class ExcelWriter {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	XSSFWorkbook workbook;
	FileOutputStream of;
	public static String WB_TEMPLATE="blank2.xlsx";
	
	/**
	 * Getter for output file of
	 * @return
	 */
	public FileOutputStream getOf() {
		return this.of;
	}
	/**
	 * Setter for output file of
	 * @param of
	 */
	public void setOf(FileOutputStream of) {
		this.of = of;
	}
	public XSSFWorkbook getWorkbook() {
		return this.workbook;
	}
	public void setWorkbook(XSSFWorkbook workbook) {
		this.workbook = workbook;
	}
	/**
	 * Default constructor will instantiate a workbook using the default name defined in
	 * WB_TEMPLATE
	 * @throws IOException 
	 * 
	 */
	public ExcelWriter() throws IOException
	{
		this.setWorkbook(new XSSFWorkbook(this.getDefaultInputStream()));
	}
	/**
	 * Constructor will instantiate a workbook using a path parameter
	 * @param path -- complete path to a template to be updated
	 * @throws IOException
	 */
	public ExcelWriter(String path) throws IOException
	{
		log.trace("Creating output tplate "+path);
		this.setWorkbook(new XSSFWorkbook(new FileInputStream(path)));
	}
	public ExcelWriter(InputStream fs,FileOutputStream of) throws IOException
	{
		this.workbook=new XSSFWorkbook(fs);
		this.setOf(of);
	}
	
	/**
	 * Constructor which instantiates a workbook using a path paramter and an output file
	 * to which the updated workbook will be written
	 * @param path
	 * @param output
	 * @throws IOException
	 */
	public ExcelWriter(String path,String output) throws IOException
	{
		log.trace("Creating workbook "+path+" from blank template "+path);
		this.setWorkbook(new XSSFWorkbook(new FileInputStream(path)));
		this.setOf(new FileOutputStream(output));
	}

	/**
	 * Writes the current workbook to an output file
	 * @throws IOException 
	 */
	public void writeWorkbook() throws IOException
	{
		this.getWorkbook().write(this.of);
		this.getOf().close();
	}
	
	public InputStream getDefaultInputStream() throws IOException
	{
		return getClass().getResource(ExcelWriter.WB_TEMPLATE).openStream();
	}
	
	
}
