package com.function;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Azure Storage Queue trigger.
 */
public class DeletePostQueueTrigger {
    BlobServiceClient blobServiceClient;
    BlobContainerClient containerClient;
    BlobClient blobClient;

    final String resizedImageStorageContainerName = "resized-images-storage";
    final String connectionString = "DefaultEndpointsProtocol=https;AccountName=guestbookstorage99;AccountKey=bTuX+vfB4ZrAifz/bdPxuZrjJ4OCHxQT0QpaTKnusPCOKjXDsirO3BQSKv3JiVV0lTPri4zwinFi+AStIpUZDQ==;EndpointSuffix=core.windows.net";

    /**
     * This function will be invoked when a new message is received at the specified path. The message contents are provided as input to this function.
     */
    @FunctionName("DeletePostQueueTrigger")
    public void run(
        @QueueTrigger(name = "message", queueName = "guestbook-resize-delete-queue", connection = "AzureWebJobsStorage") String message,
        final ExecutionContext context
    ) {
        blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        containerClient = blobServiceClient
                .getBlobContainerClient(resizedImageStorageContainerName);
        blobClient = containerClient.getBlobClient(message);

        if (blobClient.exists()) {
            blobClient.delete();
            context.getLogger().info("Worker deleted a resized image for post with row key: " + message);
        }
        else  {
            context.getLogger().info("Doesn't exist resized image for post with row key: " + message);

        }

    }
}
