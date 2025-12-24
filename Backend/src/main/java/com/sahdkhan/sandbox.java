package com.sahdkhan;

import com.sahdkhan.collections.Monitor;
import com.sahdkhan.utilities.Executor;
import com.sahdkhan.utilities.collections.ExecutionResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public class sandbox
{
    public static void main( String[] args )
    {
        Path script = null;
        try
        {
            script = Monitor.extractResourceToTempFile( "/scripts/getMonitors.ps1" );
        }
        catch ( IOException e )
        {
            System.out.println( "Error extracting PowerShell script: " + e.getMessage() );
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
//        System.out.println( jsonOutput.toString() );
        ObjectMapper mapper = new ObjectMapper();
        JsonNode displayJson = mapper.readTree( jsonOutput.toString() );
        System.out.println( displayJson.get( 0 ) );
        displayJson.get( 0 ).forEach( node ->
        {
            String id = node.get( "id" ).toString();
            String name = node.get( "name" ).toString();
            String resolution = node.get( "resolution" ).toString();
            String refreshRate = node.get( "refreshRate" ).toString();
            String pixelDensity = node.get( "pixelDensity" ).toString();
            Monitor monitor = new Monitor( name, resolution, refreshRate, pixelDensity, id );
            System.out.println( monitor.getName() );
        } );
    }
}
