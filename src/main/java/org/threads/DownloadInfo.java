package org.threads;
public interface DownloadInfo {
    int getSize(); //bytes
    String getOriginalFileName();
    String getFileKey();
    String getDownloadURL();
}
