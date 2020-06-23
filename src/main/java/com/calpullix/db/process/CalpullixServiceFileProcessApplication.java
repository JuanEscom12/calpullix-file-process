package com.calpullix.db.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
@Slf4j
public class CalpullixServiceFileProcessApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalpullixServiceFileProcessApplication.class, args);
	}
	
	@Autowired
	private DataSource dataSource;

//	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		log.info(":: Reading files to replace collate ");
		String directory = "/Users/juancarlospedrazaalcala/Documents/WorkSpaceServices/DataBaseProcess/"
				+ "calpullix-file-process/src/main/resources/CURRENT";
		try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach(item -> {
		        	log.info(":: FILE {} ", item.getFileName());
		        	if (!item.getFileName().toString().equals("CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Product_Branch.sql")) {
		        		readFiles(directory + "/" + item.getFileName().toString());
		        	}
		        });
		} catch (IOException e1) {
			e1.printStackTrace();
		} 	
		log.info(":: End Reading files to replace collate ");
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void createDefaultDB() {
		String[] filesOrder  = {
								"CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Employee_Position.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Employee.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Users.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Contact_Type.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_State.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Branch_Region.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Branch.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Services.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Branch_Services.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Branch_Employee.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Branch_Deduction.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Product_Status.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Brand.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Weight_Units.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Provider.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Product_Classification.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Product_Categories.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Product.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Product_History.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Data_Sheet.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Product_Location.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Distribution_Center.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Product_Branch_Status.sql",
//							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Product_Branch.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_PurchaseOrder_Status.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_PurchaseOrder.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Promotions_Status.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Promotions.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Education_Level.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Marital_Status.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Customer_Profile.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Customers.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Profile_Promotions.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Profile_KMeans.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Profile_KNearest.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Profile_Regression.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Product_Recomendation_Profile.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Statistics.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Statistics_Variable_Relation.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Statistics_HeatMap.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Statistics_Correlation.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Statistics_Correlation_Variable_Relation.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Statistics_Groupby.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Statistics_Groupby_Variable_Relation.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Statistics_Anova.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Statistics_Blox_Plot.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Twitter_Type_Message.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Twitter.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Twitter_Messages.sql",
							    "CALPULLIX_VIRTUAL_MANAGER_ASSISTANT_Regression.sql"};
		
		Resource resource;
		ResourceDatabasePopulator databasePopulator;
		for (final String file: filesOrder) {
			log.info(":::::: File {} ", file);
			resource = new ClassPathResource(
    				"/CURRENT/" + file);
    		databasePopulator = new ResourceDatabasePopulator(resource);
    		databasePopulator.execute(dataSource);
		}
			
	}
	
	public void readFiles(final String pathFile) {
		File fileToBeModified = new File(pathFile);
		String oldContent = "";
		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(fileToBeModified));
			// Reading all the lines of input text file into oldContent
			String line = reader.readLine();
			while (line != null) {
				oldContent = oldContent + line + System.lineSeparator();
				line = reader.readLine();
			}
			// Replacing oldString with newString in the oldContent
			String newContent = oldContent.replaceAll(
					"CHARSET=utf8 COLLATE=utf8_general_ci;",
					"CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;");
			// Rewriting the input text file with newContent
			writer = new FileWriter(fileToBeModified);
			writer.write(newContent);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}
