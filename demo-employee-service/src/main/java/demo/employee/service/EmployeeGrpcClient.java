package demo.employee.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import demo.interfaces.grpc.Employee;
import demo.interfaces.grpc.EmployeeServiceGrpc;
import org.springframework.stereotype.Service;

import com.google.protobuf.Descriptors.FieldDescriptor;

import demo.interfaces.grpc.Allocation;
import demo.interfaces.grpc.AllocationServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class EmployeeGrpcClient {

	@GrpcClient("allocation-service")
	private AllocationServiceGrpc.AllocationServiceBlockingStub allocationServiceBlockingStub;
	
	@GrpcClient("allocation-service")
	private AllocationServiceGrpc.AllocationServiceStub allocationServiceStub;

	@GrpcClient("employee-service")
	private EmployeeServiceGrpc.EmployeeServiceBlockingStub employeeServiceBlockingStub;

	@GrpcClient("employee-service")
	private EmployeeServiceGrpc.EmployeeServiceStub employeeServiceStub;

	public List<Map<FieldDescriptor, Object>> getAllocationByEmployeeIDViaSynchronousClient(long employeeID) {

		Iterator<Allocation> allocationReply = allocationServiceBlockingStub
				.getAllocationByEmployee(Allocation.newBuilder().setEmployeeID(employeeID).build());

		Iterable<Allocation> iterable = () -> allocationReply;
		Stream<Allocation> allocationStream = StreamSupport.stream(iterable.spliterator(), false);

		return allocationStream.map(allocation -> allocation.getAllFields()).collect(Collectors.toList());

	}
	

	public List<Map<FieldDescriptor, Object>> getAllocationByEmployeeIDViaAsynchronousClient(long employeeID) throws InterruptedException {
		
		final CountDownLatch finishLatch = new CountDownLatch(1);
		List<Map<FieldDescriptor, Object>> responceList = new ArrayList<Map<FieldDescriptor,Object>>();

		allocationServiceStub.getAllocationByEmployee(Allocation.newBuilder().setEmployeeID(employeeID).build(), new StreamObserver<Allocation>() {
			
			@Override
			public void onNext(Allocation value) {
				responceList.add(value.getAllFields());				
			}
			
			@Override
			public void onError(Throwable t) {
				finishLatch.countDown();
				
			}
			
			@Override
			public void onCompleted() {
				finishLatch.countDown();
				
			}
		});
		
		finishLatch.await(1, TimeUnit.MINUTES);

		return responceList;

	}


	public List<Employee> saveEmployeeViaSynchronousClient(Employee employee) {

		final CountDownLatch finishLatch = new CountDownLatch(1);
		List<Employee> employeesData = new ArrayList<>();
		employeeServiceStub.saveEmployee(employee, new StreamObserver<Employee>() {
			@Override
			public void onNext(Employee employee) {
				employeesData.add(employee);
			}

			@Override
			public void onError(Throwable throwable) {
				finishLatch.countDown();
			}

			@Override
			public void onCompleted() {
				finishLatch.countDown();
			}
		});


		return employeesData;
	}

}
