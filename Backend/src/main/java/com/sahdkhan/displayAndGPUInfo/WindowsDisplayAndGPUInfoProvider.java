package com.sahdkhan.displayAndGPUInfo;

import com.sahdkhan.collections.DisplayInfo;
import com.sahdkhan.collections.Monitor;

import java.util.List;

public class WindowsDisplayAndGPUInfoProvider implements DisplayAndGPUInfoProvider
{
    private final DisplayInfo displayInfo;

    public WindowsDisplayAndGPUInfoProvider()
    {
        displayInfo = new DisplayInfo();
        displayInfo.setModel( getNameOfGPU() );
        displayInfo.setCores( null );
    }

    private String getNameOfGPU()
    {
        ProcessBuilder pb = new ProcessBuilder(
                "powershell",
                "-Command",
                "Get-CimInstance Win32_VideoController | Select Name" );
        try
        {
            Process process = pb.start();
            try ( var reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader( process.getInputStream() ) ) )
            {
                String line;
                while ( ( line = reader.readLine() ) != null )
                {
                    if ( line.trim().isEmpty() || line.contains( "Name" ) || line.contains( "---" ) )
                    {
                        continue;
                    }
                    return line.trim();
                }
            }
            process.waitFor();
            return "Unknown GPU";
        }
        catch ( Exception e )
        {
            return "Error retrieving GPU name";
        }
    }

    @Override
    public String getModel()
    {
        return displayInfo.getModel();
    }

    @Override
    public String getCores()
    {
        return displayInfo.getCores();
    }

    @Override
    public boolean hasMultipleDisplays()
    {
        return false;
    }

    @Override
    public List< Monitor > getMonitors()
    {
        return List.of();
    }
}
