package org.threads;

import java.util.ArrayList;
import java.util.List;

public class DownloadInfoProvider {
    private List<DownloadInfo> downloadInfoList;

    public DownloadInfoProvider() {
        // Initialize the list with some data
        String downloadFromUrl = "http://downloadserver.com";

        downloadInfoList = new ArrayList<>();
        downloadInfoList.add(new DownloadFileInfo( 11397602, "test1_gtfs_2023-03-06_20-35-35.zip", "FUIXKIUF", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 13202944, "Spektr_na_svetlinata._Tsvetove_1 (1).ppt", "HAIVHKJW", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 16041244, "mingw-w64-v10.0.0.zip", "GBMWFHCE", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 16283681, "metaqa.v0.22.06.zip", "VMNFVOPW", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 16812857, "GTFS_aleop_44.zip", "FJEDWXGS", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 17277440, "cloud_sql_proxy.exe", "ZRKSLRRN", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 5417437, "IMG_20230216_193158.jpg", "EOYMSANE", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 5717587, "2013_geo.zip", "OKZEDYTB", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 6180382, "chromedriver_win32.zip", "JTPDOQGX", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 6603303, "chromedriver_win32_114_0_5735.zip", "QGWMGQPM", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 6727431, "nnn_S4bb0485a63fe442ab1f190f9c6ef6d9fU.jpg", "GLNVRBJW", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 7119503, "chromedriver_win32 (1).zip", "GTBEONWQ", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 8203816, "S23_resolution_2_IMG_20230217_141839.jpg", "QKUWAFAJ", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 8682859, "latest_usb_driver_lenovp_k10_windows.zip", "WCRDRRXZ", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 9085208, "MongoDBJDBCDriver.exe", "UFKTSQON", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 9233920, "SQUASH-all-test-cases_html_20210913.xls", "GFWZMJSS", downloadFromUrl ));
        downloadInfoList.add(new DownloadFileInfo( 9233920, "SQUASH-test-cases_rich_text_20210913.xls", "AWKTLIVQ", downloadFromUrl ));
    }

    public List<DownloadInfo> getDownloadFilesList() {
        // Here, you would typically query your data source based on packageId
        return downloadInfoList;
    }
}