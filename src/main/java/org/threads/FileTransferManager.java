package org.threads;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.rsm.HomeWork.uploadURL;

public class FileTransferManager {
    private static final int MAX_CONCURRENT_DOWNLOADS = 5;
    private static final int MAX_CONCURRENT_UPLOADS = 10;
    private static final int MAX_TOTAL_DOWNLOAD_SIZE = 100 * 1024 * 1024; // 100MB

    private ExecutorService downloadExecutor;
    private ExecutorService uploadExecutor;

    public FileTransferManager() {
        downloadExecutor = Executors.newFixedThreadPool(MAX_CONCURRENT_DOWNLOADS);
        uploadExecutor = Executors.newFixedThreadPool(MAX_CONCURRENT_UPLOADS);
    }

    public TransferReport transferFiles(long packageId) {
        TransferReport report = new TransferReport();

        try {
            List<DownloadInfo> downloadInfos = getDownloadInfos(packageId);

            // Filter out disallowed files and calculate total download size
            List<DownloadInfo> filteredDownloadInfos = new ArrayList<>();
            int totalDownloadSize = 0;
            for (DownloadInfo downloadInfo : downloadInfos) {
                if (isFileAllowed(downloadInfo)) {
                    filteredDownloadInfos.add(downloadInfo);
                    totalDownloadSize += downloadInfo.getSize();
                }
            }

            if (totalDownloadSize > MAX_TOTAL_DOWNLOAD_SIZE) {
                throw new TransferException("Total download size exceeds the limit");
            }

            Set<String> uploadedFileNames = new HashSet<>();

            // Download and upload files concurrently
            List<Future<UploadResult>> uploadFutures = new ArrayList<>();
            for (DownloadInfo downloadInfo : filteredDownloadInfos) {
                Future<UploadResult> uploadFuture = downloadAndUploadFile(downloadInfo, uploadedFileNames);
                uploadFutures.add(uploadFuture);
            }

            // Collect results from upload tasks
            for (Future<UploadResult> uploadFuture : uploadFutures) {
                try {
                    UploadResult uploadResult = uploadFuture.get();
                    report.addFileResult(uploadResult.getFileName(), uploadResult.getUploadTime(), uploadResult.isSuccess());
                } catch (InterruptedException | ExecutionException e) {
                    report.addFileResult("Unknown", 0, false); // Failed to upload file
                }
            }
        } catch (TransferException e) {
            report.setOperationResult(false);
        }

        // Shutdown the executors
        downloadExecutor.shutdown();
        uploadExecutor.shutdown();

        return report;
    }

    private List<DownloadInfo> getDownloadInfos(long packageId) {
        // Yanush: We have no details about download service API
        // instead we will simulate it calling DownloadInfoProvider
        DownloadInfoProvider infoProvider = new  DownloadInfoProvider();
        return infoProvider.getDownloadFilesList();
    }

    private boolean isFileAllowed(DownloadInfo downloadInfo) {
        String fileName = downloadInfo.getOriginalFileName();
        String fileExtension = getFileExtension(fileName);

        String[] disallowedExtensions = {"cmd", "com", "dll", "dmg", "exe", "iso", "jar", "js"};
        for (String extension : disallowedExtensions) {
            if (extension.equalsIgnoreCase(fileExtension)) {
                return false; // File extension not allowed
            }
        }

        return true;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }

    private Future<UploadResult> downloadAndUploadFile(DownloadInfo downloadInfo, Set<String> uploadedFileNames) {

        return downloadExecutor.submit(() -> {
            String fileName = getUniqueFileName(downloadInfo.getOriginalFileName(), uploadedFileNames);
            long startTime = System.currentTimeMillis();

            try (InputStream inputStream = downloadFile(downloadInfo.getDownloadURL(), downloadInfo.getOriginalFileName())) {
                doUpload(downloadInfo.getFileKey(), inputStream, downloadInfo.getSize(), fileName);
                uploadedFileNames.add(fileName);
                long uploadTime = System.currentTimeMillis() - startTime;
                return new UploadResult(fileName, uploadTime, true);
            } catch (IOException | TransferException e) {
                return new UploadResult(fileName, 0, false);
            }
        });
    }

    private String getUniqueFileName(String fileName, Set<String> uploadedFileNames) {
        String baseName = fileName;
        int counter = 1;
        while (uploadedFileNames.contains(fileName)) {
            fileName = baseName + "_" + counter;
            counter++;
        }
        return fileName;
    }

    private InputStream downloadFile(String downloadURL, String fileToDownload) throws IOException {

        URL url = new URL(downloadURL.concat("/" ).concat(fileToDownload));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        } else {
            throw new IOException("Failed to download file. Response Code: " + responseCode);
        }
    }

    private void doUpload(String key, InputStream data, int size, String uploadFileName) throws TransferException {
        try {
            // Implement the logic to upload the file using the upload service API
            // This method should call the upload service API
            // For simplicity, let's assume we have implemented this method
            URL url = new URL(uploadURL.concat("/").concat(uploadFileName));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/octet-stream");

            OutputStream outputStream = connection.getOutputStream();

            // Read the input stream and write it to the output stream
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            data.close();
            outputStream.close();
        } catch (Exception e) {
            throw new TransferException("Failed to upload file "
                        .concat(key).concat(" -> ")
                        .concat(uploadFileName)
                        .concat(" " + size));
        }
    }

    private static class UploadResult {
        private final String fileName;
        private final long uploadTime;
        private final boolean success;

        public UploadResult(String fileName, long uploadTime, boolean success) {
            this.fileName = fileName;
            this.uploadTime = uploadTime;
            this.success = success;
        }

        public String getFileName() {
            return fileName;
        }

        public long getUploadTime() {
            return uploadTime;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    private static class TransferException extends Exception {
        public TransferException(String message) {
            super(message);
        }
    }

    public static class TransferReport {
        private boolean operationResult;
        private List<FileResult> fileResults;
        private int successCount;
        private int failureCount;

        public TransferReport() {
            fileResults = new ArrayList<>();
        }

        public void setOperationResult(boolean operationResult) {
            this.operationResult = operationResult;
        }

        public void addFileResult(String fileName, long uploadTime, boolean success) {
            fileResults.add(new FileResult(fileName, uploadTime, success));
            if (success) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        public long getTotalTime() {
            long totalTime = 0;
            for (FileResult fileResult : fileResults) {
                totalTime += fileResult.getUploadTime();
            }
            return totalTime;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getFailureCount() {
            return failureCount;
        }

        public void printReport() {
            System.out.println("Transfer Report:");
            System.out.println("Operation Result: " + (operationResult ? "Success" : "Failure"));
            System.out.println("Total Time: " + getTotalTime() + " milliseconds");
            System.out.println("Number of Successes: " + successCount);
            System.out.println("Number of Failures: " + failureCount);
            System.out.println("File Results:");
            for (FileResult fileResult : fileResults) {
                System.out.println("File Name: " + fileResult.getFileName());
                System.out.println("Upload Time: " + fileResult.getUploadTime() + " milliseconds");
                System.out.println("Success: " + (fileResult.isSuccess() ? "Yes" : "No"));
                System.out.println("------------------------");
            }
        }
    }

    public static class FileResult {
        private final String fileName;
        private final long uploadTime;
        private final boolean success;

        public FileResult(String fileName, long uploadTime, boolean success) {
            this.fileName = fileName;
            this.uploadTime = uploadTime;
            this.success = success;
        }

        public String getFileName() {
            return fileName;
        }

        public long getUploadTime() {
            return uploadTime;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}