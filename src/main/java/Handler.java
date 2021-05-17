import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final AmazonS3 s3= AmazonS3ClientBuilder.standard().withRegion("eu-central-1").withPathStyleAccessEnabled(true).build();
    private final String bucket_name= "timesheet-approve-bucket";
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        LambdaLogger logger= context.getLogger();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "Content-Type");
        headers.put("Access-Control-Allow-Methods", "OPTIONS,GET");
        int year= Integer.parseInt(apiGatewayProxyRequestEvent.getPathParameters().get("year"));
        int month= Integer.parseInt(apiGatewayProxyRequestEvent.getPathParameters().get("month"));
        StudentRepository studentRepository= new StudentDBRepository();
        List<Student> students = studentRepository.getStudents();
        logger.log("Studentlist received");
        List<Timesheet> timesheets = new ArrayList<>();
        for (Student student : students) {
            timesheets.add(new Timesheet(student, year, month));
            logger.log("Student "+student.getFirstName()+" was added");
        }
        checkIfFileExists(timesheets, year, month, logger);
        logger.log("Timesheets were checked for existance");
        String timesheetJson = new Gson().toJson(timesheets);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(headers)
                .withBody(timesheetJson);

    }
    private void checkIfFileExists(List<Timesheet> timesheets, int year, int month, LambdaLogger logger) {
        for (Timesheet timesheet : timesheets) {
            try {
                timesheet.setFileExists(s3.doesObjectExist(bucket_name, "Timesheets/" + timesheet.getFileName()));
                logger.log("Timesheet " + timesheet.getFileName() + " exists: " + timesheet.fileExists);
            }catch (AmazonS3Exception e){
                e.printStackTrace();
            }
            catch (AmazonServiceException e){
                e.printStackTrace();
            }catch (AmazonClientException e){
                e.printStackTrace();
            }
        }
    }

}

