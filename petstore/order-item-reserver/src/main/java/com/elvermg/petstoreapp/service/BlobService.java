package com.elvermg.petstoreapp.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;

public class BlobService {

    private static final String FUNCTION_ENDPOINT = "https://martinmod6sa.blob.core.windows.net/";
    private static final String SAS_TOKEN = "sv=2024-11-04&ss=b&srt=sco&sp=rwdlaciytfx&se=2025-04-09T08:23:29Z&st=2025-04-09T00:23:29Z&spr=https,http&sig=PNx8neGsiMMEZdtZYlbcyC%2BpyI2laJAVEOVxBWzvZq8%3D";
    private static final String CONTAINER_NAME = "orders";


    public static void uploadBlob(String sessionId, String order) {
        BlobClient blobClient = new BlobClientBuilder()
                .endpoint(FUNCTION_ENDPOINT)
                .sasToken(SAS_TOKEN)
                .containerName(CONTAINER_NAME)
                .blobName(sessionId)
                .buildClient();

        blobClient.upload(BinaryData.fromString(order), true);
    }
}
