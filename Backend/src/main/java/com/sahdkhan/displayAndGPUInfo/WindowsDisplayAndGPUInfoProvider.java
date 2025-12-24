package com.sahdkhan.displayAndGPUInfo;

import com.sahdkhan.collections.DisplayInfo;
import com.sahdkhan.collections.Monitor;
import com.sahdkhan.utilities.Executor;
import com.sahdkhan.utilities.StringEditor;
import com.sahdkhan.utilities.collections.ExecutionResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WindowsDisplayAndGPUInfoProvider implements DisplayAndGPUInfoProvider
{
    private final DisplayInfo displayInfo;

    public WindowsDisplayAndGPUInfoProvider()
    {
        displayInfo = new DisplayInfo();
        displayInfo.setModel( getNameOfGPU() );
        displayInfo.setCores( null );
        displayInfo.setMonitors( getMonitorsInfo() );
        if ( displayInfo.getMonitors().size() > 1 )
        {
            displayInfo.setHasMultipleDisplays( true );
        }
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
                    new java.io.InputStreamReader( process.getInputStream() ) ) ) {
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
        } catch ( Exception e ) {
            return "Error retrieving GPU name";
        }
    }

    private List< Monitor > getMonitorsInfo()
    {
        List< Monitor > monitors = new ArrayList<>();

        Path script = null;
        try {
            script = Monitor.extractResourceToTempFile( "/scripts/getMonitors.ps1" );
        } catch ( IOException e ) {
            System.out.println( "Error extracting PowerShell script: " + e.getMessage() );
            return monitors;
        }
        ProcessBuilder pb = new ProcessBuilder(
                "powershell",
                "-NoProfile",
                "-ExecutionPolicy", "Bypass",
                "-File", script.toAbsolutePath().toString()
        );
        pb.redirectErrorStream( true );
        ExecutionResult result = Executor.execute( pb, Duration.ofSeconds( 3 ) );
        List< String > jsonOutput = result.output();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode displayJson = mapper.readTree( jsonOutput.toString() );
        displayJson.get( 0 ).forEach( node ->
        {
            String id = StringEditor.stripQuotes( node.get( "id" ).toString() );
            String name = StringEditor.stripQuotes(node.get( "name" ).toString());
            String resolution = StringEditor.stripQuotes(node.get( "resolution" ).toString());
            resolution = resolution.replace( "x", " x " );
            String refreshRate = StringEditor.stripQuotes(node.get( "refreshRate" ).toString());
            String pixelDensity = StringEditor.stripQuotes(node.get( "pixelDensity" ).toString());
            Monitor monitor = new Monitor( name, resolution, refreshRate, pixelDensity, id );
            monitors.add( monitor );
        } );
        return monitors;
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
        return displayInfo.isHasMultipleDisplays();
    }

    @Override
    public List< Monitor > getMonitors()
    {
        return displayInfo.getMonitors();
    }
}
