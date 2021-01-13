package com.ravi.exceltocsv.ExcelToCsvProject.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ravi.exceltocsv.ExcelToCsvProject.controller.ExcelToCsvController;
import com.ravi.exceltocsv.ExcelToCsvProject.model.DataModel;
import com.ravi.exceltocsv.ExcelToCsvProject.repository.DataModelRepository;
import com.ravi.exceltocsv.ExcelToCsvProject.util.ExcelUtility;


/**
 * This class will be used as service layer to connect with database repository
 * @author RaviP
 *
 */
@Service
public class ExcelToCsvService {

	
	private static final Logger logger=LoggerFactory.getLogger(ExcelToCsvService.class);
	@Autowired
	private DataModelRepository dataModelRepository;

	public void saveAll(List<DataModel> listOfDataModels) {
		logger.debug("saveAll method started...");
		//truncating all data 
		dataModelRepository.deleteAll();
		//storign again
		dataModelRepository.saveAll(listOfDataModels);
		logger.debug("saveAll method ended. successfully!");
	}

	public List<DataModel> getAllData() {
		logger.debug("getAllData method started...");
		List<DataModel> listOfData = new ArrayList<DataModel>();
		dataModelRepository.findAll().forEach(p -> listOfData.add(p));
		logger.debug("getAllData method ended. successfully!");
		return listOfData;
	}

	public InputStream loadDataFromDatabase() {
		 List<DataModel> listOfDataModels = dataModelRepository.findAll();
		 List<DataModel> filteredOflistOfData= listOfDataModels.stream().filter(dm-> dm.getWorkItemType().equalsIgnoreCase("Requirement")).collect(Collectors.toList());
		 ByteArrayInputStream in = ExcelUtility.datamodelToCSV(filteredOflistOfData);
		 return in;
	}
	
}
