# gRPC -  Implementation

In this implementation, I have implementated a spring boot microservice solution which contains a Netflix-Eureka discovery server and two microservice in Employee & Allocation domains. And the interface project contains the raw protocol-buffer files and using the protocol buffer compiler maven plugin generates the java model and service classes.

## How to Run
1. Clone the repository to your local machine
2. Navigate to the grpc-springboot-demo folder and run following command for build the project<br/> 
`mvn package`
3. Run applications using following commands
<br/>`java -jar grpc-springboot-demo\demo-eureka-server\target\demo-eureka-server-0.0.1-SNAPSHOT.jar`<br/>
`java -jar grpc-springboot-demo\demo-employee-service\target\demo-employee-service-0.0.1-SNAPSHOT.jar`<br/>
`java -jar grpc-springboot-demo\demo-allocation-service\target\demo-allocation-service-0.0.1-SNAPSHOT.jar`

## Service Methods

 - **Unary RPCs** (gRPC Server: employee-service, gRPC Client: allocation-service) <br/>
 Proto Definition:  `rpc getEmployee (Employee) returns (Employee) {
    }`<br/>
     End Point: `{IP Address}:8082/allocation/{allocationID}`
 
 - **Bidirectional streaming RPCs** (gRPC Server: employee-service, gRPC Client: allocation-service)<br/>
 Proto Definition: `rpc saveEmployee (stream Employee) returns (stream Employee) {
    }`<br/>
    End Point: `{IP Address}:8082/employee/save}`

  - To configure file location for XML and CSV properties are defined in the application.properties file.
