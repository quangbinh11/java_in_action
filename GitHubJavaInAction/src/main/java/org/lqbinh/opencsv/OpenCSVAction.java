package org.lqbinh.opencsv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

/**
 * Created by quangbinh on 7/15/14.
 */
public class OpenCSVAction {

    public static void parseCSVFileReadLineByLine() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("category.csv");
        CSVReader reader = new CSVReader(new FileReader(new File(url.toURI())));

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            System.out.println(nextLine[1]);
        }
    }

    public static void parseCSVFileReadAll() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("category.csv");
        CSVReader reader = new CSVReader(new FileReader(new File(url.toURI())));

        String[] row;
        List<?> content = reader.readAll();

        for (Object object : content) {
            row = (String[]) object;

            System.out.println("Cell Value: " + row[1]);

        }
    }

    public static void parseCSV2Bean() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("category.csv");
        CSVReader reader = new CSVReader(new FileReader(new File(url.toURI())));

        ColumnPositionMappingStrategy<Category> mappingStrategy
                = new ColumnPositionMappingStrategy<Category>();
        mappingStrategy.setType(Category.class);

        // the fields to bind do in your JavaBean
        String[] columns = new String[]{"categoryId", "categoryName", "categoryType", "description", "createdDate", "createdBy", "updatedDate", "updatedBy", "countryId", "parentId"};
        mappingStrategy.setColumnMapping(columns);

        CsvToBean<Category> csv = new CsvToBean<Category>();
        List<Category> categoryList = csv.parse(mappingStrategy, reader);

        for (int i = 0; i < categoryList.size(); i++) {
            Category categoryDetail = categoryList.get(i);
            // display CSV values
            System.out.println("Product No : " + categoryDetail.getCategoryId());
            System.out.println("Product Name : " + categoryDetail.getCategoryName());
            System.out.println("Price: " + categoryDetail.getCreatedDate());
            System.out.println("Quandity: " + categoryDetail.getDescription());
            System.out.println("------------------------------");
        }

    }

    public static void main(String[] args) throws Exception {
//        parseCSVFileReadLineByLine();

//        parseCSVFileReadAll();
        
        parseCSV2Bean();
    }
}
