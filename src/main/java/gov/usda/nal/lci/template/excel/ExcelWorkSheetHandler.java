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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;

/**
 * Generic Excel WorkSheet handler
 * 
 * 
 */
public class ExcelWorkSheetHandler<T> implements SheetContentsHandler {

	private static final Log LOG = LogFactory
			.getLog(ExcelWorkSheetHandler.class);

	private boolean verifiyHeader = true;
	private int skipRows = 0;
	private int HEADER_ROW = 0;
	private int currentRow = 0;
	private List<T> valueList;
	private Class<T> type;
	private Map<String, String> cellMapping = null;
	private T objCurrentRow = null;
	private T objHeader = null;

	/**
	 * Constructor
	 * 
	 * <br>
	 * <br>
	 * <strong>For Example:</strong> Reading rows (zero based) starting from
	 * Zero<br>
	 * <code>ExcelWorkSheetHandler&lt;XXXXVO> workSheetHandler = new ExcelWorkSheetHandler&lt;
	 * xxxxVO>(xxxxVO.class, cellMapping);</code>
	 * 
	 * @param type
	 *            a {@link Class} object
	 * @param cellMapping
	 *            a {@link Map} object
	 */
	public ExcelWorkSheetHandler(Class<T> type, Map<String, String> cellMapping) {
		this.type = type;
		this.cellMapping = cellMapping;
		this.valueList = new ArrayList<T>();
	}

	/**
	 * Constructor
	 * 
	 * <br>
	 * <br>
	 * <strong>For Example:</strong> Reading rows (zero based) starting from Row
	 * 11<br>
	 * <code>ExcelWorkSheetHandler&lt;xxxxVO> workSheetHandler = new ExcelWorkSheetHandler&lt;PersonVO>(xxxxVO.class, cellMapping, 10);</code>
	 * 
	 * @param type
	 *            a {@link Class} object
	 * @param cellMapping
	 *            a {@link Map} object
	 * @param skipRows
	 *            a <code>int</code> object - Number rows to skip (zero based).
	 *            default is 0
	 */
	public ExcelWorkSheetHandler(Class<T> type,
			Map<String, String> cellMapping, int skipRows) {
		this.type = type;
		this.cellMapping = cellMapping;
		this.valueList = new ArrayList<T>();
		this.skipRows = skipRows;
	}

	/**
	 * Returns Value List (List&lt;T>) read from Excel Workbook, Row represents
	 * one Object in a List.
	 * 
	 * <br>
	 * <br>
	 * <strong>For Example:</strong><br>
	 * <code>List&lt;XXXXVO> persons = workSheetHandler.getValueList();</code>
	 * 
	 * @return List&lt;T>
	 */
	public List<T> getValueList() {
		return valueList;
	}

	/**
	 * @see org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler#startRow(int)
	 */

	public void startRow(int rowNum) {
		this.currentRow = rowNum;

		if (rowNum > HEADER_ROW && rowNum >= skipRows) {
			objCurrentRow = this.getInstance();
		}
		if (rowNum >= skipRows) {
			objCurrentRow = this.getInstance();
		}
	}

	/**
	 * @see org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler#cell(java.lang.String,
	 *      java.lang.String)
	 */
	public void cell(String cellReference, String formattedValue) {

		if (currentRow >= skipRows) {
			if (StringUtils.isBlank(formattedValue)) {
				return;
			}

			if (HEADER_ROW == currentRow && verifiyHeader) {
				this.assignValue(objHeader, getCellReference(cellReference),
						formattedValue);
			}
		
			this.assignValue(objCurrentRow, getCellReference(cellReference),
					formattedValue);
		}
	}

	/**
	 * @see org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler#endRow()
	 */

	public void endRow() {
		if (HEADER_ROW == currentRow && verifiyHeader && null != objHeader) {
			if (!checkHeaderValues(objHeader)) {
				throw new RuntimeException(
						"Header values doesn't match, so invalid Excel file!");
			}
		}

		if (currentRow >= skipRows) {
			if (null != objCurrentRow && isObjectHasValue(objCurrentRow)) {
				// Current row data is populated in the object, so add it to
				// list
				this.valueList.add(objCurrentRow);
			}

			// Row object is added, so reset it to null
			objCurrentRow = null;
		}
	}

	/**
	 * Currently not considered for implementation
	 * 
	 * @see org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler#headerFooter(java.lang.String,
	 *      boolean, java.lang.String)
	 */

	public void headerFooter(String text, boolean isHeader, String tagName) {
		// currently not consider for implementation
	}

	private String getCellReference(String cellReference) {
		if (StringUtils.isBlank(cellReference)) {
			return "";
		}

		return cellReference.split("[0-9]*$")[0];
	}

	/*
	 * targetObj = pojo(xxxxVO);cellReference= column letter(A);val= header
	 * title("Flow Type")
	 */
	private void assignValue(Object targetObj, String cellReference,
			String value) {
		if (null == targetObj || StringUtils.isEmpty(cellReference)
				|| StringUtils.isEmpty(value)) {
			return;
		}
		
		try {
			String propertyName = this.cellMapping.get(cellReference);
			if (null == propertyName) {
				LOG.warn("Cell mapping doesn't exists for "+cellReference+"!");
				propertyName="noOpenLCA";
			} 
			PropertyUtils.setSimpleProperty(targetObj, propertyName, value);
		
		} catch (IllegalAccessException iae) {
			LOG.error(iae.getMessage());
		} catch (InvocationTargetException ite) {
			LOG.error(ite.getMessage());
		} catch (NoSuchMethodException nsme) {
			LOG.error(nsme.getMessage());
		
		
		}

	}

	private T getInstance() {
		try {
			return type.newInstance();
		} catch (InstantiationException ie) {
			LOG.error(ie.getMessage());
		} catch (IllegalAccessException iae) {
			LOG.error(iae.getMessage());
		}
		return null;
	}

	/**
	 * To check generic object of T has a minimum one value assigned or not
	 */
	private boolean isObjectHasValue(Object targetObj) {
		for (Map.Entry<String, String> entry : cellMapping.entrySet()) {

			if (StringUtils.isNotBlank(getPropertyValue(targetObj,
					entry.getValue()))) {
				return true;
			}
		}
		return false;
	}

	private boolean checkHeaderValues(Object targetObj) {
		boolean compareSuccess = true;
		for (Map.Entry<String, String> entry : cellMapping.entrySet()) {

			String value = getPropertyValue(targetObj, entry.getValue());
			LOG.debug("Comparing header value from excel file: " + value);

		}

		return compareSuccess;
	}

	private String getPropertyValue(Object targetObj, String propertyName) {
		String value = "";
		if (null == targetObj || StringUtils.isBlank(propertyName)) {
			LOG.error("targetObj or propertyName is null, both require to retrieve a value");
			return value;
		}

		try {
			if (PropertyUtils.isReadable(targetObj, propertyName)) {
				Object v = PropertyUtils.getSimpleProperty(targetObj,
						propertyName);
				if (null != v && StringUtils.isNotBlank(v.toString())) {
					value = v.toString();
				}
			} else {
				LOG.error("Given property (" + propertyName
						+ ") is not readable!");
			}
		} catch (IllegalAccessException iae) {
			LOG.error(iae.getMessage());
		} catch (InvocationTargetException ite) {
			LOG.error(ite.getMessage());
		} catch (NoSuchMethodException nsme) {
			LOG.error(nsme.getMessage());
		}
		return value;
	}
}
