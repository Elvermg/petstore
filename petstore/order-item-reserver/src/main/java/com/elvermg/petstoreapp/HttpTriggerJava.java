package com.elvermg.petstoreapp;

import java.util.*;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import com.elvermg.petstoreapp.service.BlobService;
/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerJava {
    /**
     * This function listens at endpoint "/api/HttpTriggerJava". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTriggerJava
     * 2. curl {your host}/api/HttpTriggerJava?name=HTTP%20Query
     */
    @FunctionName("HttpTriggerJava")
    @StorageAccount("martinmod6sa")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION)
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String sessionId = request.getQueryParameters().get("sessionId");
        String order = request.getBody().orElse(null);

        if (order == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("The order should be in the request body").build();
        } else {
            BlobService.uploadBlob(sessionId, order);
            return request.createResponseBuilder(HttpStatus.OK).body("Order updated successfully!0").build();
        }
    }
}
