package com.sahdkhan.systemSoftwareInfo;

public interface SystemSoftwareInfoProvider
{

    String getOSName();

    String getOSVersion();

    String getComputerName();

    String getSystemUptime();

    String getSystemUser();

    String getBootMode();

    String getKernelVersion();
}
