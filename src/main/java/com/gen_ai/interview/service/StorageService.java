package com.gen_ai.interview.service;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageService {
//    private final Storage storage;
    private final String bucketName = "resume-plus-429023.appspot.com";
    
    public String getImageUrl(String imageName) {
        return "https://storage.googleapis.com/resume-plus-429023.appspot.com/" + imageName;

//        BlobId blobId = BlobId.of(bucketName, imageName);
//        Blob blob = storage.get(blobId);
//        if (blob != null && blob.exists()) {
//            return String.format("https://storage.googleapis.com/%s/%s", bucketName, imageName);
//        }
//        return null;
    }

}
