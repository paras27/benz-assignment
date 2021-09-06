package demo.allocation.service;

import demo.interfaces.grpc.Allocation;
import demo.interfaces.grpc.AllocationServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AllocationGrpcServiceImpl extends AllocationServiceGrpc.AllocationServiceImplBase {


	@Override
	public void getAllocationByEmployee(Allocation request, StreamObserver<Allocation> responseObserver) {
		
		AllocationResourceProvider.getAllocationfromAllocationSource().stream()
				.filter(alloc -> alloc.getEmployeeID() == request.getEmployeeID())
				.forEach(responseObserver::onNext);

		responseObserver.onCompleted();
	}	

}
