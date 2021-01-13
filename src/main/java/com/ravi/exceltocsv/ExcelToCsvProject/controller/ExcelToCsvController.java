package com.ravi.exceltocsv.ExcelToCsvProject.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ravi.exceltocsv.ExcelToCsvProject.constants.ExcelToCsvConstants;
import com.ravi.exceltocsv.ExcelToCsvProject.model.DataModel;
import com.ravi.exceltocsv.ExcelToCsvProject.model.ResponseMessage;
import com.ravi.exceltocsv.ExcelToCsvProject.service.ExcelToCsvService;
import com.ravi.exceltocsv.ExcelToCsvProject.util.ExcelUtility;

/**
 * This class is created to handle rest based services with respect to excel
 * upload , read and write to csv
 * 
 * @author RaviP
 *
 */
@RestController
@RequestMapping("/api")
public class ExcelToCsvController {

	/**
	 * 
	 * This method will validate and read excel file from multipart file
	 * 
	 * @param file
	 * @return
	 */
	@Autowired
	private ExcelToCsvService excelToCsvService;

	private static final Logger logger = LoggerFactory.getLogger(ExcelToCsvController.class);

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadAndReadFile(@RequestParam("file") MultipartFile file) {
		logger.debug("uploadAndReadFile method started..");
		String message = "";

		if (ExcelUtility.hasExcelFormat(file)) {
			try {

				List<DataModel> listOfDataModels = ExcelUtility.readExcel(file);
				// ExcelUtility.convertSelectedSheetInXLXSFileToCSV(file,0);
				// Remove duplicates by adding the list to a HashSet
				Set<DataModel> datamodelSet = new HashSet<>(listOfDataModels);
				System.out.println(listOfDataModels);
				// Now add the unique elements from Set to List
				listOfDataModels.clear();
				listOfDataModels.addAll(datamodelSet);
				System.out.println("After removing duplicates:");
				System.out.println(listOfDataModels.size());
				excelToCsvService.saveAll(listOfDataModels);
				message = "Uploaded and read the file successfully: " + file.getOriginalFilename();
				logger.debug("uploadAndReadFile method ended successfully");
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				logger.error("uploadAndReadFile method failed!");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}

		message = "Please upload an excel file!";
		logger.warn("Proper excel file not added ");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}

	@GetMapping("/datamodels")
	public ResponseEntity<List<DataModel>> getAllDataModels() {
		logger.debug("getAllDataModels method started..");
		try {
			List<DataModel> listOfDataModels = excelToCsvService.getAllData();
			if (listOfDataModels.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			logger.debug("getAllDataModels method ended successfully");
			return new ResponseEntity<>(listOfDataModels, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("getAllDataModels method failed!");
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/download")
	public ResponseEntity<Resource> exportToCSV(HttpServletResponse response) throws IOException {
		logger.debug("exportToCSV method started..");
		String filename = "data_file.csv";
		InputStreamResource file = new InputStreamResource(excelToCsvService.loadDataFromDatabase());

		logger.debug("exportToCSV method started..");
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/csv")).body(file);

	}

	@GetMapping("/calculateEstimates")
	public List<DataModel> calculateOrigianlEstimates() {
		// fetch data from database
		List<DataModel> listOfDataModels = excelToCsvService.getAllData();
		List<DataModel> updatedListOfDataModels = new ArrayList<>();
		// created a map of parent id and all its list of child
		Map<Long, List<DataModel>> mapOfParentToListOfDataModel = ExcelUtility
				.getMapOfDataModelsForParent(listOfDataModels);
		// if map has same parent and id , then update original estimate of parent and
		// store it in new list
		for (DataModel datamodel : listOfDataModels) {
			if (mapOfParentToListOfDataModel.containsKey(datamodel.getWorkItemId())) {
				List<DataModel> listOfDataModel = mapOfParentToListOfDataModel.get(datamodel.getWorkItemId());
				int sumOfOriginalEstimate = ExcelUtility.getSumOfEstimate(listOfDataModel);
				if (listOfDataModel != null) {
					for (DataModel datamodel1 : listOfDataModel) {
						if (ExcelToCsvConstants.WorkItemType_Requirements
								.equalsIgnoreCase(datamodel1.getWorkItemType()))
							datamodel1.setOriginalEstimate(sumOfOriginalEstimate);
							updatedListOfDataModels.add(datamodel1);
							break;
					}
				}
			} else {
					updatedListOfDataModels.add(datamodel);
			}
				
		}
		System.out.println(listOfDataModels.size());
		System.out.println(updatedListOfDataModels.size());
		// we can save it in database 
		excelToCsvService.saveAll(updatedListOfDataModels);
		
		return updatedListOfDataModels;
	}

}
