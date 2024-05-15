package funtionaltests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.junit.jupiter.api.DynamicTest;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Used to mimic a bad request response from the service
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BadRequestResponseBody
{

    private int status;
    private String error;
    private String message;
    private String path;
    private Timestamp timeStamp;

    public BadRequestResponseBody(int status, String error, String message, String path, Timestamp timeStamp)
    {
        this.error = error;
        this.status = status;
        this.message = message;
        this.path = path;
        this.timeStamp = timeStamp;
    }

    /**
     * Execute the tests associated with execution
     * @param error - error header
     * @param message - message
     * @param path - path in url
     * @param status - status code returned
     * @return list of {@link DynamicTest} that represent the test result
     */
    public List<DynamicTest> executeTests(String error, String message, String path, int status)
    {
        List<DynamicTest> responseBodyTests =
                Arrays.asList(DynamicTest.dynamicTest("Error",
                        ()-> assertEquals(error, this.getError())),
                        DynamicTest.dynamicTest("Message",
                                ()-> assertEquals(message, this.getMessage())),
                        DynamicTest.dynamicTest("Path",
                                ()-> assertEquals(path, this.getPath())),
                        DynamicTest.dynamicTest("Status",
                                ()-> assertEquals(status, this.getStatus()))
                );


        return responseBodyTests;
    }
    public String getError() {
        return error;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
