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
import gov.usda.nal.lci.template.exporter.USFedLCAExporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;



import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.mysql.MySQLDatabase;
import org.openlca.core.database.derby.DerbyDatabase;

/**
 * Standalone command line program which exports a unit process to Excel.  Useful for development and testing. 
 * @author Gary Moore
 */
public class FedLCATemplateExportStart {

	private static final Log LOG = LogFactory.getLog(FedLCATemplateExportStart.class);
	private Long pid; // process ID to export
	private IDatabase database;
	private IDataSet dataSet;
	private String url;
	private String user;
	private String password;
	private String pf;	// properties file (full path)
	private String of; // output file
	public FedLCATemplateExportStart()
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
			if (database.createConnection() != null)
				LOG.trace("Database Connection Successful");
			else
				LOG.trace("Database Connection failed");
			USFedLCAExporter export=new USFedLCAExporter(database,this.pid,this.of);
			export.run();
			database.close();
		} catch (FileNotFoundException fe) {
			LOG.error("File Not Found " + fe.getMessage());
		} catch (IOException ioe) {
			LOG.error("IO Exception " + ioe.getMessage(), ioe.getCause());
		} catch (Exception e) {
			LOG.error("Error " + e.getMessage(), e.getCause());

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


	/**
	 * The -p -i options and output file name are required.
	 * @param args
	 * @throws ParseException
	 * @throws Exception
	 */
	public static void main(String[] args) throws ParseException,Exception {		
		CommandLineParser clp=new GnuParser();
		final Options opts=new Options();
		FedLCATemplateExportStart thisInstance=new FedLCATemplateExportStart();
		opts.addOption("p","properties",true,"full path to the config properties file");
		opts.addOption("i","id",true,"database id of the process to export");
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
			if ( cl.getOptionValue("i") != null )
				thisInstance.pid=Long.valueOf(cl.getOptionValue("i"));
			else {
				System.err.println("-i option is required");
				thisInstance.printUsage();
				throw new Exception();
			}
			
			if ( (remainargs=cl.getArgs()) != null && remainargs.length > 0  ) 
				thisInstance.of=remainargs[0];
			else {
				System.err.println ("output file name is required.");
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
				System.err.println("usage:  exporttemplate -p propertiesfile template.xls");
				System.err.println("            -p  full path to properites file.");
				System.err.println("            -i database id of the process to export ");
				System.err.println("            template.xls Excel template to output ");
				System.exit(2);
		}
		/**
		 * 
		 */
		private void getDerbyDatabase()
		{
			File folder=new File(this.url);
			if ( folder.exists())
				setDatabase(new DerbyDatabase(folder));
		}

}
