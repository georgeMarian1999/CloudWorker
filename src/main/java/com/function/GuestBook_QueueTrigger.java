package com.function;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;

import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Azure Functions with Azure Storage Queue trigger.
 */
public class GuestBook_QueueTrigger {
    BlobServiceClient blobServiceClient;
    BlobContainerClient containerClient;
    BlobClient blobClient;

    final String imageStorageContainerName = "images-storage";
    final String resizedImageStorageContainerName = "resized-images-storage";
    final String connectionString = "DefaultEndpointsProtocol=https;AccountName=guestbookstorage99;AccountKey=bTuX+vfB4ZrAifz/bdPxuZrjJ4OCHxQT0QpaTKnusPCOKjXDsirO3BQSKv3JiVV0lTPri4zwinFi+AStIpUZDQ==;EndpointSuffix=core.windows.net";
    /**
     * This function will be invoked when a new message is received at the specified path. The message contents are provided as input to this function.
     */
    @FunctionName("GuestBook-QueueTrigger")
    public void run(
        @QueueTrigger(name = "message", queueName = "guestbook-file-queue", connection = "AzureWebJobsStorage") String message,
        final ExecutionContext context
    ) throws IOException {
        blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        containerClient = blobServiceClient
                .getBlobContainerClient(imageStorageContainerName);
        blobClient = containerClient.getBlobClient(message);
        BinaryData binaryDataImageContent = blobClient.downloadContent();

        containerClient = blobServiceClient.getBlobContainerClient(resizedImageStorageContainerName);
        blobClient = containerClient.getBlobClient(message);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(binaryDataImageContent.toStream())
                .size(200, 140)
                .outputFormat("JPG")
                .outputQuality(1)
                        .toOutputStream(outputStream);

        blobClient.upload(BinaryData.fromBytes(outputStream.toByteArray()));

        context.getLogger().info("Java Queue trigger function processed a image resize for post with rowKey:" +  message);
    }
}
