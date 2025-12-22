package com.sahdkhan.systemSoftwareInfo;

import com.sahdkhan.utilities.Executor;
import com.sahdkhan.utilities.collections.ExecutionResult;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class MacSystemSoftwareInfoProvider implements SystemSoftwareInfoProvider
{
    Map< String, String > systemInfoMap;

    public MacSystemSoftwareInfoProvider()
    {
        ProcessBuilder processBuilder = new ProcessBuilder( "system_profiler", "SPSoftwareDataType" );
        ExecutionResult result = Executor.execute( processBuilder, Duration.ofSeconds( 3 ) );
        if ( !result.success() )
        {
            systemInfoMap = Map.of();
        }
        else
        {
            systemInfoMap = parseSystemProfilerOutput( result.output() );
        }
    }

    private Map< String, String > parseSystemProfilerOutput( List< String > output )
    {
        Map< String, String > systemInfoMap = new java.util.HashMap<>();
        for ( String line : output )
        {
            line = line.trim();
            if ( line.contains( ":" ) )
            {
                String[] parts = line.split( ":", 2 );
                if ( parts.length == 2 )
                {
                    String key = parts[ 0 ].trim();
                    String value = parts[ 1 ].trim();
                    systemInfoMap.put( key, value );
                }
            }
        }
        return systemInfoMap;
    }

    @Override
    public String getOSName()
    {
        return parseOsName( systemInfoMap.getOrDefault( "System Version", null ) );
    }

    @Override
    public String getOSVersion()
    {
        return parseOsVersion( systemInfoMap.getOrDefault( "System Version", null ) );
    }

    @Override
    public String getComputerName()
    {
        return systemInfoMap.getOrDefault( "Computer Name", null );
    }

    @Override
    public String getSystemUptime()
    {
        return systemInfoMap.getOrDefault( "Time since boot", null );
    }

    @Override
    public String getSystemUser()
    {
        String systemUserLine = systemInfoMap.getOrDefault( "User Name", null );
        int index = systemUserLine.indexOf( "(" );
        return index > 0 ? systemUserLine.substring( 0, index ).trim() : systemUserLine;
    }

    @Override
    public String getBootMode()
    {
        return systemInfoMap.getOrDefault( "Boot Mode", null );
    }

    @Override
    public String getKernelVersion()
    {
        return systemInfoMap.getOrDefault( "Kernel Version", null );
    }

    /**
     * Parses the OS name from the system version line.
     *
     * @param systemVersionLine the system version line
     * @return the OS name or null if not found
     */
    private String parseOsName( final String systemVersionLine )
    {
        if ( systemVersionLine == null || systemVersionLine.isEmpty() )
        {
            return null;
        }

        String[] parts = systemVersionLine.split( " " );
        if ( parts.length < 1 )
        {
            return null;
        }

        return parts[ 0 ];
    }

    /**
     * Parses the OS version from the system version line.
     *
     * @param systemVersionLine the system version line
     * @return the OS version or null if not found
     */
    private String parseOsVersion( final String systemVersionLine )
    {
        if ( systemVersionLine == null || systemVersionLine.isEmpty() )
        {
            return null;
        }

        String[] parts = systemVersionLine.split( " " );
        if ( parts.length < 2 )
        {
            return null;
        }

        return parts[ 1 ];
    }

}
