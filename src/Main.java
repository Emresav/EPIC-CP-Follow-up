
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.Days;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

//import java.io.FileNotFoundException;
//import java.util.*;

public class Main {
	
	public static ArrayList<Entry> readExcel(String filename) throws IOException {
		boolean parseDebug = false;
		
		ArrayList<Entry> allEntries = new ArrayList<Entry>();
		
		FileInputStream fl = new FileInputStream(filename);
		XSSFWorkbook workbook = new XSSFWorkbook (fl);
		XSSFSheet sheet1 = workbook.getSheetAt(0);
		
		Iterator<Row> rowItr = sheet1.iterator();
		Row row = rowItr.next();
		while(rowItr.hasNext()) {
			row = rowItr.next();
			
			Entry etr = new Entry();
			
			Iterator<Cell> cellItr = row.cellIterator();
			while(cellItr.hasNext() ) {
				
				Cell cell = cellItr.next();
				
//				System.out.print(cell.getColumnIndex() + "\t");
				
				switch(cell.getCellTypeEnum()) {
					case STRING:
						if(cell.getColumnIndex() == 0) {
							etr.setId(cell.getStringCellValue());
						};
						
						if(parseDebug) System.out.print(cell.getStringCellValue() + "\t");
	                    break;
					case NUMERIC: 
						if(DateUtil.isCellDateFormatted(cell)) {
							DateTime dt = new DateTime(cell.getDateCellValue());
							
							if(cell.getColumnIndex() == 1) {
								etr.setEpicDate(dt);
							} else if (cell.getColumnIndex() == 3)  {
								etr.setStartEBRT(dt);
							} else if (cell.getColumnIndex() == 4)  {
								etr.setEndEBRT(dt);
							}

//							dt = dt.plusDays(29); // add days
							if(parseDebug) System.out.print(dt.getDayOfMonth() + "/" + dt.getMonthOfYear() + "/" + dt.getYear() + "\t");
						} else {
							String dId = String.format("%.0f", cell.getNumericCellValue());
							
							if(cell.getColumnIndex() == 0) {
								etr.setId(dId);
							};
							
							if(parseDebug) System.out.print(dId + "\t");	
						}
						break;
					case BLANK:
						if(parseDebug) System.out.print("\t");
						break;
					case FORMULA: 
						if(parseDebug) System.out.print(cell.getCellFormula() + "\t");
						break;
					default:
						if(parseDebug) System.out.print(cell.getCellTypeEnum());
						break;
				}

				}
			allEntries.add(etr);
			if(parseDebug) System.out.print("\n");
			}		
		
		workbook.close();
		if(parseDebug) {
			for(Entry e : allEntries) {
				System.out.print(e.getId() + "\t\t");
				System.out.print(e.getEpic() + "\t\t");
				System.out.print(e.getStartEBRT() + "\t\t");
				System.out.println(e.getEndEBRT());
			} 
		}
	return allEntries;	
	}
	
	public static ArrayList<Patient> populatePatientsList(ArrayList<Entry> entries) {
		ArrayList<Patient> patients = new ArrayList<Patient>();
		
		for (Entry etr : entries) {
			boolean entryFound  = false;
			
			for (Patient pat : patients) {
				if(pat.patientMatches(etr.getId())) {
					// found a patient
					if(etr.getEpic() != null) { 
						pat.addEpicDate(etr.getEpic());
					}
					if(pat.getStartEBRT() == null && etr.getStartEBRT() != null) {
						pat.setStartEBRT(etr.getStartEBRT());
					}
					if(pat.getEndEBRT() == null && etr.getEndEBRT() != null) {
						pat.setEndEBRT(etr.getEndEBRT());
					}
					entryFound = true;
				} 
			}
			
			if (!entryFound) {
				// create a new patient and add to patients
				Patient pt = new Patient(etr.getId(), etr.getStartEBRT(), etr.getEndEBRT());
				
				if(etr.getEpic() != null) { 
					pt.addEpicDate(etr.getEpic());
				}
				patients.add(pt);			
			}
			
		}
		
		return patients;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(" - - - - - - - - - - - - - - - - UCL EPIC-CP Follow-up Check - - - - - - - - - - - - - - - - ");
		System.out.println(" - - - Emre Savas (emre.savas@kcl.ac.uk), Aylin Savas (aylin.sarova.15@ucl.ac.uk)- - - - - - ");
		System.out.println(" - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
		String filename = "data/sample_data.xlsx";
		ArrayList<Entry> entries = readExcel(filename);
		ArrayList<Patient> patients = populatePatientsList(entries);
		System.out.println("\nCASE 1: Summary of EPIC-CP Entries in " + filename + ":");
		for (Patient p : patients) {
			p.print();
		}
		System.out.println("\nCASE 2: Patient-specific follow-ups (and their delays):");
		for (Patient p : patients) {
			p.checkEpicLimits();
		}
		
		int times = 4;
		System.out.println("\nCASE 3: Patients for Late-toxicity Assessment:");
		System.out.println ("Patients with >= " + times + " follow-ups are:");
		for(Patient p : patients) {
			int ft = p.getFollowUpTimes();
			if(ft >= times) System.out.println("\tPatient ID: " + p.getId());
		}
		
	}
	
}
