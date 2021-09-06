package demo.employee.service;

import java.util.List;
import java.util.Map;

import demo.interfaces.grpc.AllocationServiceGrpc;
import demo.interfaces.grpc.Employee;
import demo.interfaces.grpc.EmployeeServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.google.protobuf.Descriptors.FieldDescriptor;

import javax.ws.rs.Path;

@RestController
public class EmployeeRestController {

	@Autowired
	private EmployeeGrpcClient employeeGrpcClient;

	@RequestMapping("/")
	public String echo() {
		return "Employee Service";
	}

	@GetMapping("/employee/{employeeID}/allocation")
	public List<Map<FieldDescriptor, Object>> getAllocationByEmployeeID(@PathVariable Long employeeID, @RequestParam(value = "isSyncClient", required = false, defaultValue = "Y") String isSyncClient) throws InterruptedException {

		return isSyncClient.equals("Y") ? employeeGrpcClient.getAllocationByEmployeeIDViaSynchronousClient(employeeID)
				: employeeGrpcClient.getAllocationByEmployeeIDViaAsynchronousClient(employeeID);
	}

	@PostMapping(value = "/employee/save")
	public List<Employee> saveEmployeeToFile(@RequestBody  Employee employees,
											 String isSyncClient) {

		return isSyncClient.equals("Y") ? employeeGrpcClient.saveEmployeeViaSynchronousClient(employees)
				: null;
	}

}
