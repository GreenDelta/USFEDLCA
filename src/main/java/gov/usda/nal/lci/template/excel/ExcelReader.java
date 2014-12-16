package gov.usda.nal.lci.template.excel;
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
/**
 *Generic Excel File(XLSX) Reading using Apache POI 
 * 
 */

import java.io.File;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class ExcelReader {

	private static final Log LOG = LogFactory.getLog(ExcelReader.class);

	private static final int READ_ALL = -1;

	private OPCPackage xlsxPackage;
	private SheetContentsHandler sheetContentsHandler;

	/**
	 * Constructor: Microsoft Excel File (XSLX) Reader
	 * 
	 * @param pkg
	 *            a {@link OPCPackage} object - The package to process XLSX
	 * @param sheetContentsHandler
	 *            a {@link SheetContentsHandler} object - WorkSheet contents
	 *            handler
	 */
	public ExcelReader(OPCPackage pkg, SheetContentsHandler sheetContentsHandler) {
		this.xlsxPackage = pkg;
		this.sheetContentsHandler = sheetContentsHandler;
	}

	/**
	 * Processing all the WorkSheet from XLSX Workbook.
	 * 
	 * @throws Exception
	 */
	public void process() throws Exception {
		read(READ_ALL);
	}

	/**
	 * Processing of particular WorkSheet (zero based) from XLSX Workbook.
	 * 
	 * @throws Exception
	 */
	public void process(int sheetNumber) throws Exception {
		read(sheetNumber);
	}

	private void read(int sheetNumber) throws RuntimeException {
		ReadOnlySharedStringsTable strings;
		try {
			strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
			XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
			StylesTable styles = xssfReader.getStylesTable();
			Iterator<InputStream> sheets = xssfReader.getSheetsData();

			for (int sheet = 0; sheets.hasNext(); sheet++) {

				InputStream stream = sheets.next();
				if ((READ_ALL == sheetNumber) || (sheet == sheetNumber)) {
					readSheet(styles, strings, stream);
				}
			}
		} catch (IOException ioe) {
			LOG.error(ioe.getMessage(), ioe.getCause());
		} catch (SAXException se) {
			LOG.error(se.getMessage(), se.getCause());
		} catch (OpenXML4JException oxe) {
			LOG.error(oxe.getMessage(), oxe.getCause());
		} catch (ParserConfigurationException pce) {
			LOG.error(pce.getMessage(), pce.getCause());
		}
	}

	/**
	 * Parses the content of one sheet using the specified styles and
	 * shared-strings tables.
	 * 
	 * @param styles
	 *            a {@link StylesTable} object
	 * @param sharedStringsTable
	 *            a {@link ReadOnlySharedStringsTable} object
	 * @param sheetInputStream
	 *            a {@link InputStream} object
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private void readSheet(StylesTable styles,
			ReadOnlySharedStringsTable sharedStringsTable,
			InputStream sheetInputStream) throws IOException,
			ParserConfigurationException, SAXException {

		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		XMLReader sheetParser = saxFactory.newSAXParser().getXMLReader();

		ContentHandler handler = new XSSFSheetXMLHandler(styles,
				sharedStringsTable, sheetContentsHandler, true);

		sheetParser.setContentHandler(handler);
		sheetParser.parse(new InputSource(sheetInputStream));
	}

	@SuppressWarnings("unused")
	private static File getFile(String filePath) throws Exception {
		if (null == filePath || filePath.isEmpty()) {
			throw new Exception("File path cannot be null");
		}

		return new File(filePath);
	}

	@SuppressWarnings("unused")
	private static OPCPackage getOPCPackage(File file) throws Exception {
		if (null == file || !file.canRead()) {
			throw new Exception(
					"File object is null or cannot have read permission");
		}

		return OPCPackage.open(new FileInputStream(file));
	}
}
