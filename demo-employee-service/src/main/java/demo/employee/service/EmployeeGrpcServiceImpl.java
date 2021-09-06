package demo.employee.service;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.util.JSONPObject;
import demo.interfaces.grpc.Employee;
import demo.interfaces.grpc.EmployeeServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


@GrpcService
@PropertySource("classpath:config/application.properties")
public class EmployeeGrpcServiceImpl extends EmployeeServiceGrpc.EmployeeServiceImplBase {

	@Value("${csv.file.location}")
	private String csvFileLocation;

	@Value("csv.file.Name")
	private String csvFileName;

	@Value("xml.file.location")
	private String xmlFileLocation;

	@Value("xml.file.name")
	private String xmlFileName;

	@Override
	public void getEmployee(Employee request, StreamObserver<Employee> responseObserver) {
		responseObserver.onNext(EmployeeResourceProvider.getEmployeeListfromEmployeeSource()
													.stream()
													.filter(emp -> emp.getEmployeeID() == request.getEmployeeID())
													.findFirst()
													.get());
		responseObserver.onCompleted();
	}

	@Override
	public void saveEmployee(Employee request, StreamObserver<Employee> responseObserver) {


	{
			if (request.getFileType().equals("CSV")) {
				List<Employee> recordsToAddCsv = new ArrayList<>();
				recordsToAddCsv.add(request);
				CSVUtils.writeCSVFile(csvFileLocation + csvFileName, recordsToAddCsv);
			}
			if (request.getFileType().equals("XML")){
				JSONObject xml = new JSONObject(request);
				String xmlData = XML.toString(xml);
				saveToXML(xmlData);
				System.out.println(xmlData);
			}
		}

	}

	private void saveToXML(String xmlData) {
		{

			try {
				DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
				Document document = convertStringToXMLDocument(xmlData);


				// setting attribute to element
				String firstName = document.getElementsByTagName("firstName").item(0).getTextContent();
				String lastName = document.getElementsByTagName("firstName").item(0).getTextContent();
				String dateOfBirth = document.getElementsByTagName("dateOfBirth").item(0).getTextContent();
				String workingYear = document.getElementsByTagName("workingYear").item(0).getTextContent();


				Element userData = document.createElement("userData");

				// root element
				Element users = document.createElement("users");
				document.appendChild(users);

				Element fName = document.createElement("firstName");
				fName.appendChild(document.createTextNode(firstName));
				users.appendChild(fName);

				Element lName = document.createElement("lastName");
				lName.appendChild(document.createTextNode(lastName));
				users.appendChild(lName);


				Element dob = document.createElement("dob");
				dob.appendChild(document.createTextNode(dateOfBirth));
				users.appendChild(dob);

				Element year = document.createElement("workingYear");
				year.appendChild(document.createTextNode(workingYear));
				users.appendChild(year);



				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(xmlFileLocation + xmlFileName));
				transformer.transform(source, result);

				// Output to console for testing
				StreamResult consoleResult = new StreamResult(System.out);
				transformer.transform(source, consoleResult);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static Document convertStringToXMLDocument(String xmlString)
	{

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder;
		try
		{
			builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateEmployee(Employee request, StreamObserver<Employee> responseObserver) {

	}
}
