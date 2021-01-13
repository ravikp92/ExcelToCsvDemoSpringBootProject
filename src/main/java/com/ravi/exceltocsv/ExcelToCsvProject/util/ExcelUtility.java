package com.ravi.exceltocsv.ExcelToCsvProject.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ravi.exceltocsv.ExcelToCsvProject.model.DataModel;


/**
 * This is utility class to handle format conversion of excel to csv or csv to
 * excel
 * 
 * @author RaviP
 *
 */
@Service
public class ExcelUtility {

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	static String[] HEADERs = { "ID", "Work Item Type", "Title", "State", "Original Estimate", "Iteration Path",
			"Remaining Work", "Assigned To", "Size", "Created Date", "Parent" };
	static String SHEET = "Data";

	private static int count=0;
	
	/**
	 * This method will check input file is excel or not
	 * @param file
	 * @return
	 */
	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	
	/**
	 * This method will read file and convert into set of data models
	 * @param file
	 * @return
	 */
	public static List<DataModel> readExcel(MultipartFile file) {
		try {
			
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();
			List<DataModel> dataModels = new ArrayList<DataModel>();
			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();
				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}
				Iterator<Cell> cellsInRow = currentRow.iterator();
				DataModel dataModel = new DataModel();
				int cellIdx = 0;
				dataModel.setId(++count);
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					switch (cellIdx) {
					case 0:
						dataModel.setWorkItemId((long) currentCell.getNumericCellValue());
						break;
					case 1:
						dataModel.setWorkItemType(currentCell.getStringCellValue());
						break;
					case 2:
						dataModel.setTitle(currentCell.getStringCellValue());
						break;
					case 3:
						dataModel.setState(currentCell.getStringCellValue());
						break;
					case 4:
						dataModel.setOriginalEstimate((int) currentCell.getNumericCellValue());
						break;
					case 5:
						dataModel.setIterationPath(currentCell.getStringCellValue());
						break;
					case 6:
						dataModel.setRemainingWork((int) currentCell.getNumericCellValue());
						break;
					case 7:
						dataModel.setAssignedTo(currentCell.getStringCellValue());
						break;
					case 8:
						dataModel.setSize((int) currentCell.getNumericCellValue());
						break;
					case 9:
						dataModel.setCreatedDate(currentCell.getDateCellValue());
						break;
					case 10:
						dataModel.setParent((long) currentCell.getNumericCellValue());
						break;
					default:
						break;
					}
					cellIdx++;
				}
				dataModels.add(dataModel);
			}
			return dataModels;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}

	/**
	 * This method will get data from database and convert into csv
	 * @param listOfDataModels
	 * @return
	 */
	public static ByteArrayInputStream datamodelToCSV(List<DataModel> listOfDataModels) {
		final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

	    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
	        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
	    	
	    	String[] csvHeader = { "ID", "Work Item Type", "Title", "State", "Original Estimate", "Iteration Path",
	    			"Remaining Work", "Assigned To", "Size", "Created Date", "Parent"};
	      csvPrinter.printRecord(csvHeader);
	      
	     
	      for (DataModel dataModel : listOfDataModels) {
	    	 
	    	  
	        List<String> data = Arrays.asList("TEST","TEST", dataModel.getTitle(),
	        		dataModel.getTitle(), dataModel.getAssignedTo(), String.valueOf(dataModel.getSize()),
	        		
	        		"",
	        		
	        		"","NA","",String.valueOf(dataModel.getOriginalEstimate()), String.valueOf(dataModel.getRemainingWork()),
	        		String.valueOf(dataModel.getOriginalEstimate()), "test"
	        		);
	        csvPrinter.printRecord(data);
	      }

	      csvPrinter.flush();
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
	    }
	}

	// It will calculate sum of child original estimates
		public static int getSumOfEstimate(List<DataModel> list) {
			int sum = 0;
			if(list!=null) {
				for(int i=0;i<list.size();i++) {
					if(list.get(i).getOriginalEstimate()!=0) {
						sum = sum + list.get(i).getOriginalEstimate();
					}
				}
			}
			return sum;
		}

		// It will create a map of parent to list of child data models
		public static Map<Long, List<DataModel>> getMapOfDataModelsForParent(List<DataModel> listOfDataModels) {
			Map<Long, List<DataModel>> mapOfParentToListOfDataModel = new HashMap<>();

			for (DataModel datamodel : listOfDataModels) {
				if (datamodel.getOriginalEstimate() != 0) {
					if (!mapOfParentToListOfDataModel.containsKey(datamodel.getParent())) {
						List<DataModel> newDataModelList = new ArrayList<>();
						newDataModelList.add(datamodel);
						mapOfParentToListOfDataModel.put(datamodel.getParent(), newDataModelList);
					} else {
						mapOfParentToListOfDataModel.get(datamodel.getParent()).add(datamodel);
					}
				}
			}
			return mapOfParentToListOfDataModel;
		}
	
	/*
	 * public static void convertSelectedSheetInXLXSFileToCSV(MultipartFile
	 * xlsxFile, int sheetIdx) throws Exception {
	 * 
	 * 
	 * // Open the xlsx and get the requested sheet from the workbook XSSFWorkbook
	 * workBook = new XSSFWorkbook(xlsxFile.getInputStream()); XSSFSheet selSheet =
	 * workBook.getSheetAt(sheetIdx);
	 * 
	 * HashSet<StringBuffer> set=new HashSet<>();
	 * 
	 * // Iterate through all the rows in the selected sheet Iterator<Row>
	 * rowIterator = selSheet.iterator(); while (rowIterator.hasNext()) {
	 * 
	 * Row row = rowIterator.next();
	 * 
	 * // Iterate through all the columns in the row and build "," // separated
	 * string Iterator<Cell> cellIterator = row.cellIterator(); StringBuffer sb =
	 * new StringBuffer(); while (cellIterator.hasNext()) { Cell cell =
	 * cellIterator.next(); if (sb.length() != 0) { sb.append(","); }
	 * 
	 * // If you are using poi 4.0 or over, change it to // cell.getCellType switch
	 * (cell.getCellType()) { case Cell.CELL_TYPE_STRING:
	 * sb.append(cell.getStringCellValue()); break; case Cell.CELL_TYPE_NUMERIC:
	 * sb.append(cell.getNumericCellValue()); break; case Cell.CELL_TYPE_BOOLEAN:
	 * sb.append(cell.getBooleanCellValue()); break; default: } }
	 * 
	 * set.add(sb);
	 * 
	 * }
	 * 
	 * System.out.println(set.size()); }
	 */
	

    

}
