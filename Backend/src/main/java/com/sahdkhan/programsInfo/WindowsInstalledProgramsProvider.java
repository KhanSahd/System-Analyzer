package com.sahdkhan.programsInfo;

import com.sahdkhan.collections.InstalledProgram;
import com.sahdkhan.utilities.Executor;
import com.sahdkhan.utilities.collections.ExecutionResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class provides methods to get the list of installed programs on Windows.
 * It implements the InstalledProgramsProvider interface.
 */
public class WindowsInstalledProgramsProvider implements InstalledProgramsProvider
{
    private static final String[] UNINSTALL_KEYS = {
            "HKLM\\Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall",
            "HKLM\\Software\\WOW6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall"
    };

    /**
     * Retrieves a list of InstalledPrograms.
     * Done by iterating over the UNINSTALL_KEYS array.
     *
     * @return List of InstalledProgram objects representing installed programs.
     * @throws Exception if an error occurs while accessing the registry.
     */
    @Override
    public CompletableFuture< List< InstalledProgram > > getInstalledProgramsAsync()
    {
        return CompletableFuture.supplyAsync( () ->
        {
            try
            {
                List< InstalledProgram > programs = new ArrayList<>();

                for ( String rootKey : UNINSTALL_KEYS )
                {
                    programs.addAll( readUninstallKey( rootKey ) );
                }

                return programs;
            }
            catch ( Exception e )
            {
                throw new RuntimeException( e );
            }
        } );
    }

    /**
     * Reads the uninstall registry key and extracts installed program information.
     *
     * @param rootKey The root registry key to read.
     * @return List of InstalledProgram objects found under the specified registry key.
     * @throws Exception if an error occurs while accessing the registry.
     */
    private List< InstalledProgram > readUninstallKey( String rootKey ) throws Exception
    {
        List< InstalledProgram > programs = new ArrayList<>();

        ProcessBuilder pb = new ProcessBuilder(
                "reg", "query", rootKey, "/s"
        );
        ExecutionResult result = Executor.execute( pb, Duration.ofSeconds( 30 ) );
        if ( !result.success() )
        {
            throw new Exception( "Failed to read registry: " + result.error() );
        }

        String currentName = null;
        String currentVersion = null;

        for ( String line : result.output() )
        {
            line = line.trim();

            // New registry key starts → flush previous entry
            if ( line.startsWith( "HKEY" ) )
            {
                if ( currentName != null )
                {
                    programs.add( new InstalledProgram(
                            currentName,
                            currentVersion != null ? currentVersion : "Unknown"
                    ) );
                }

                currentName = null;
                currentVersion = null;
                continue;
            }

            if ( line.startsWith( "DisplayName" ) )
            {
                currentName = extractRegistryValue( line );
            }
            else if ( line.startsWith( "DisplayVersion" ) )
            {
                currentVersion = extractRegistryValue( line );
            }
        }

        // Flush last entry
        if ( currentName != null )
        {
            programs.add( new InstalledProgram(
                    currentName,
                    currentVersion != null ? currentVersion : "Unknown"
            ) );
        }

        return programs;
    }

    /**
     * Extracts the value from a registry query line.
     *
     * @param line The registry query line.
     * @return The extracted value, or null if not found.
     */
    private String extractRegistryValue( String line )
    {
        // Format: DisplayName    REG_SZ    Google Chrome
        String[] parts = line.split( "\\s{2,}" );
        return parts.length >= 3 ? parts[ 2 ].trim() : null;
    }
}
