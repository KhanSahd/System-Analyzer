package com.sahdkhan.displayAndGPUInfo;

import com.sahdkhan.collections.DisplayInfo;
import com.sahdkhan.collections.Monitor;
import com.sahdkhan.utilities.StringEditor;
import tools.jackson.core.util.JacksonFeatureSet;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public class MacOSDisplayAndGPUInfoProvider implements DisplayAndGPUInfoProvider
{
    private final DisplayInfo displayInfo;

    public MacOSDisplayAndGPUInfoProvider()
    {
        displayInfo = new DisplayInfo();
        String rawJson = getJsonOutput();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode displayJson = mapper.readTree( rawJson ).get( "SPDisplaysDataType" ).get( 0 );
        displayInfo.setModel( StringEditor.stripQuotes( displayJson.get( "sppci_model" ).toString() ) );
        displayInfo.setCores( StringEditor.stripQuotes( displayJson.get( "sppci_cores" ).toString() ) );

        // Check for multiple displays and mark flag.
        if ( displayJson.get( "spdisplays_ndrvs" ).size() > 1 )
        {
            // Handle multiple displays if needed
            displayInfo.setHasMultipleDisplays( true );
        }

        // Parse monitor information
        displayJson.get( "spdisplays_ndrvs" ).forEach( node ->
        {
            String resolutionRawString = StringEditor.stripQuotes( node.get( "_spdisplays_resolution" ).toString() );
            String refreshRate = parseRefreshRateFromResolution( resolutionRawString );
            String resolution = parseResolution( resolutionRawString );
            String name = StringEditor.stripQuotes( node.get( "_name" ).toString() );
            String pixelDensity = StringEditor.stripQuotes( node.get( "_spdisplays_pixels" ).toString() );
            String id = StringEditor.stripQuotes( node.get( "_spdisplays_displayID" ).toString() );
            Monitor monitor = new Monitor( name, resolution, refreshRate, pixelDensity, id );
            displayInfo.getMonitors().add( monitor );
        } );
    }

    private String getJsonOutput()
    {
        ProcessBuilder processBuilder = new ProcessBuilder( "system_profiler",
                "SPDisplaysDataType",
                "-json" );
        try
        {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            try ( var reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader( process.getInputStream() ) ) )
            {
                String line;
                while ( ( line = reader.readLine() ) != null )
                {
                    output.append( line ).append( "\n" );
                }
            }
            int exitCode = process.waitFor();
            if ( exitCode == 0 )
            {
                return output.toString();
            }
        }
        catch ( Exception e )
        {
            System.out.println( "Error retrieving display info: " + e.getMessage() );
            throw new RuntimeException( e );
        }
        return "";
    }

    private String parseRefreshRateFromResolution( String resolution )
    {
        // Example resolution string: "1920 x 1080 @ 60Hz"
        if ( resolution.contains( "@" ) )
        {
            String[] parts = resolution.split( "@" );
            if ( parts.length > 1 )
            {
                return parts[ 1 ].trim();
            }
        }
        return "";
    }

    private String parseResolution( String resolution )
    {
        // Example resolution string: "1920 x 1080 @ 60Hz"
        if ( resolution.contains( "@" ) )
        {
            String[] parts = resolution.split( "@" );
            if ( parts.length > 0 )
            {
                return parts[ 0 ].trim();
            }
        }
        return resolution;
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
