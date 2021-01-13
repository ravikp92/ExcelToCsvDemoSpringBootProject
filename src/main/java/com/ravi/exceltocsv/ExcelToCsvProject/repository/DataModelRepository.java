package com.ravi.exceltocsv.ExcelToCsvProject.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ravi.exceltocsv.ExcelToCsvProject.model.DataModel;

/**
 * This is repository class which used to connect to database.
 * @author RaviP
 *
 */
public interface DataModelRepository extends JpaRepository<DataModel, Long>{

	
}
