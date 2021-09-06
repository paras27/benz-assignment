package demo.employee.service;
import demo.interfaces.grpc.Employee;
import org.springframework.util.CollectionUtils;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {

    public static void writeCSVFile(String csvFileName, List<Employee> listCustomerRequestMappings) {
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors = new CellProcessor[] {
                new ParseInt(), //employeeID
                new NotNull(), // firstName
                new NotNull(), // lastName
                new ParseDate("MM-dd-yyyy"), // date of birth
                new ParseInt(), // working Year
        };

        try {
            // read existing data first
            List<Employee> existingRecords = readCSVFile(csvFileName);
            if(!CollectionUtils.isEmpty(existingRecords)) {
                existingRecords.removeAll(listCustomerRequestMappings);
                listCustomerRequestMappings.addAll(0, existingRecords);
            }

            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName),
                    CsvPreference.STANDARD_PREFERENCE);
            String[] header = {"employeeID","firstName", "lastName", "dateOfBirth", "workingYear"};
            beanWriter.writeHeader(header);

            for (Employee employee : listCustomerRequestMappings) {
                beanWriter.write(employee, header, processors);
            }

        } catch (IOException ex) {
            System.err.println("Error writing the CSV file: " + ex);
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error closing the writer: " + ex);
                }
            }
        }
    }

    public static List<Employee> readCSVFile(String csvFileName) {
        List<Employee> existingRecords = new ArrayList<>();
        ICsvBeanReader beanReader = null;
        CellProcessor[] processors = new CellProcessor[] {
                new NotNull(), // name
                new ParseDate("MM-dd-yyyy"), // date of birth
                new ParseDouble(), // salary
                new ParseInt() // age
        };

        try {
            beanReader = new CsvBeanReader(new FileReader(csvFileName),
                    CsvPreference.STANDARD_PREFERENCE);
            String[] header = beanReader.getHeader(true);

            Employee eachCustomerRecord = null;
            while ((eachCustomerRecord = beanReader.read(Employee.class, header, processors)) != null) {
                existingRecords.add(eachCustomerRecord);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Could not find the CSV file: " + ex);
        } catch (IOException ex) {
            System.err.println("Error reading the CSV file: " + ex);
        } catch (Exception ex) {
            System.err.println("Error reading the CSV file: " + ex);
        }finally {
            if (beanReader != null) {
                try {
                    beanReader.close();
                } catch (IOException ex) {
                    System.err.println("Error closing the reader: " + ex);
                }
            }
            return existingRecords;
        }
    }
}
