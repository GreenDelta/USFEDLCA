package gov.usda.nal.lci.template.app;
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


import gov.usda.nal.lci.template.support.IDataSet;
import gov.usda.nal.lci.template.vo.ActorVO;
import gov.usda.nal.lci.template.vo.AdministrativeInformationVO;
import gov.usda.nal.lci.template.vo.CostsVO;
import gov.usda.nal.lci.template.vo.ExchangeDataVO;
import gov.usda.nal.lci.template.vo.ISICDataVO;
import gov.usda.nal.lci.template.vo.ModelingAndValidationVO;
import gov.usda.nal.lci.template.vo.ParametersVO;
import gov.usda.nal.lci.template.vo.ProcessInformationVO;
import gov.usda.nal.lci.template.vo.SourceInformationVO;
import gov.usda.nal.lci.template.excel.MappingCells;
import gov.usda.nal.lci.template.importer.USFedLCATemplateImport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlca.core.database.DatabaseContent;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.mysql.MySQLDatabase;
import org.openlca.core.database.derby.DerbyDatabase;
import org.openlca.ecospold.io.DataSetType;
import org.openlca.ecospold.io.EcoSpoldIO;
import org.openlca.io.UnitMapping;
/**
 * Start USDA Template Program
 * Task: read data from Excel, saved to database
 * @author Y Radchenko
 */
public class UsdaTemplateStart {

	private static final Log LOG = LogFactory.getLog(UsdaTemplateStart.class);

	private IDatabase database;
	private IDataSet dataSet;
	private String url;
	private String user;
	private String password;
	private String template;  //excel document
	private String pf;	// properties file (full path)
	private List<ExchangeDataVO> exchangeDataVO;
	private List<ParametersVO> parameterDataVO;
	private List<ActorVO> actorVO;
	private List<SourceInformationVO> sourceVO;
	private ProcessInformationVO generalInfoVO;
	private AdministrativeInformationVO admVO;
	private ModelingAndValidationVO modVO;
	private List<ISICDataVO> isicDataVO;
	private List<CostsVO> costsVO;
	private static boolean flag= false;
	public UsdaTemplateStart()
	{
		
	}
	
	public void setDatabase(IDatabase database) {
		closeDatabase();
		this.database = database;
	}

	public IDatabase getDatabase() {
		return database;
	}

	public IDataSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(IDataSet dataSet) {
		this.dataSet = dataSet;
	}

	public void run() {
		try {
			readProperties();
			databaseConnection();
			if (database.createConnection() != null) {
				LOG.info("Database Connection Successful");
				
				UnitMapping mapping = UnitMapping.createDefault(database);
				if ( this.template.endsWith(".zip"))
					importZip(new File(this.template),mapping);
				else
					importXls(new File(this.template),mapping);

			} else
				LOG.info("Database Connection failed");

		} catch (FileNotFoundException fe) {
			LOG.error("File Not Found " + fe.getMessage());
		} catch (IOException ioe) {
			LOG.error("IO Exception " + ioe.getMessage(), ioe.getCause());
		} catch (Exception e) {
			LOG.error("Error " + e.getMessage(), e.getCause());

		} finally {

			// just ignore IO exception
		}

	}
	private void importFile(InputStream fs,UnitMapping mapping,String source)
	{
		USFedLCATemplateImport usdaTemplate = new USFedLCATemplateImport(
				database, fs, mapping);
		usdaTemplate.setSource(source);
		usdaTemplate.run();
	}
	private void importZip(File file,UnitMapping mapping) {
		try (ZipFile zipFile = new ZipFile(file)) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements() ) {
				ZipEntry entry = entries.nextElement();
				importFile(zipFile.getInputStream(entry),mapping,entry.getName().toLowerCase());
			}
		} catch (Exception e) {
			LOG.error("failed to import ZIP file " + file, e);
		}
	}
	private void importXls(File file,UnitMapping mapping )
	{
		try {
			if ( file.isDirectory())
			{
				for ( File f : file.listFiles() )
				{
					if ( f.getName().endsWith(".xlsx") || f.getName().endsWith(".XLSX"))
						importFile(new FileInputStream(f),mapping,f.getName());
				}
			}
			else
				importFile(new FileInputStream(file),mapping,file.getName());
		}
		catch (Exception e) {
			LOG.error( e);
		}
	}
	private void readProperties() throws FileNotFoundException, IOException {
		File file = new File(this.pf);

		Properties props = new Properties();
		props.load(new FileInputStream(file));
		this.url = props.getProperty("url");
		this.user = props.getProperty("user");
		this.password = props.getProperty("password");

	}

	public void mappingExcelCells() throws FileNotFoundException, IOException,
			Exception {

		MappingCells mappingCells = new MappingCells(new FileInputStream(this.template));

		mappingCells.run();

		this.exchangeDataVO  = mappingCells.getExchangeData();
		this.generalInfoVO   = mappingCells.getProcessData();
		this.admVO 		    = mappingCells.getAdministrationData(); 
		this.modVO           = mappingCells.getModelingAndValidationData(); 
		this.parameterDataVO = mappingCells.getParametersData();
		this.actorVO         = mappingCells.getActorData();
		this.sourceVO        = mappingCells.getSourceInformationData();
		this.isicDataVO      = mappingCells.getIsicDataVO();
		this.costsVO = mappingCells.getCostsDataVO();

	}

	private void databaseConnection() {
		if ( url.contains("mysql"))
			this.setDatabase( new MySQLDatabase(this.url, this.user, this.password));
		else {
			getDerbyDatabase();
		}
	}

	private void closeDatabase() {
		if (database == null)
			return;  
		LOG.info("close database");
		try {
			database.close();
		} catch (Exception e) {
			LOG.error("failed to closed database", e);
		}
	}



	public static void main(String[] args) throws ParseException,Exception {		
		CommandLineParser clp=new GnuParser();
		final Options opts=new Options();
		UsdaTemplateStart thisInstance=new UsdaTemplateStart();
		opts.addOption("d","debug",false,"output debugging messasges");
		opts.addOption("p","properties",true,"full path to the config properties file");
		String[] remainargs;
		try {
			CommandLine cl=clp.parse(opts,args);
			if (cl.getOptionValue("p") != null )
				thisInstance.pf=cl.getOptionValue("p");
			else {
				System.err.println ("-p option is required");
				thisInstance.printUsage();
				throw new Exception();
			}
			if ( (remainargs=cl.getArgs()) != null && remainargs.length > 0  ) 
				thisInstance.template=remainargs[0];
			else {
				System.err.println ("template name is required.");
				thisInstance.printUsage();
				throw new Exception();
			}
			
				thisInstance.run();
		}
		catch (ParseException e ) {
			System.err.println(e.getMessage());
			thisInstance.printUsage();
		}
		finally
		{
			thisInstance.closeDatabase();
		}
	}

		
		/**
		 * Outputs a usage message to stderr
		 *
		 */
		private void printUsage () {
				System.err.println("usage:  importtemplate -d -p propertiesfile template.xls");
				System.err.println("            -d  provide debugging output");
				System.err.println("            -p  full path to properites file.");
				System.err.println("            template.xls Excel template to import ");
				System.exit(2);
		}
		/**
		 * 
		 */
		private void getDerbyDatabase()
		{
			File folder=new File(this.url);
			boolean createnew=!folder.exists();
			DerbyDatabase db=new DerbyDatabase(folder);
			if (createnew )
				//db.fill(DatabaseContent.UNITS);
				db.fill(DatabaseContent.ALL_REF_DATA);
			setDatabase(db);
			
		}

}
