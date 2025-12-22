package com.sahdkhan.displayAndGPUInfo;

import com.sahdkhan.collections.Monitor;

import java.util.List;

public interface DisplayAndGPUInfoProvider
{
    String getModel();

    String getCores();

    boolean hasMultipleDisplays();

    List< Monitor > getMonitors();
}
