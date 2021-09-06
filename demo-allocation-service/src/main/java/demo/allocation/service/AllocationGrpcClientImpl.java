package demo.allocation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.protobuf.Descriptors.FieldDescriptor;

import demo.interfaces.grpc.Employee;
import demo.interfaces.grpc.EmployeeServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class AllocationGrpcClientImpl {

	@GrpcClient("employee-service")
	private EmployeeServiceGrpc.EmployeeServiceBlockingStub employeeServiceBlockingStub;

	@GrpcClient("employee-service")
	private EmployeeServiceGrpc.EmployeeServiceStub employeeServiceStub;


	public Map<FieldDescriptor, Object> getEmployeeDetailsByAllocationID(long allocationID) {

		return employeeServiceBlockingStub
					.getEmployee(Employee.newBuilder()
											.setEmployeeID(AllocationResourceProvider
															.getAllocationfromAllocationSource()
															.stream()
															.filter(alloc -> alloc.getAllocationID() == allocationID)
															.findFirst()
															.get()
															.getEmployeeID())
											.build())
					.getAllFields();
	}

}
