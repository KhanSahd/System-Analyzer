package com.sahdkhan.programsInfo;

import com.sahdkhan.collections.InstalledProgram;
import com.sahdkhan.utilities.Executor;
import com.sahdkhan.utilities.StringEditor;
import com.sahdkhan.utilities.collections.ExecutionResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * This class provides methods to get the list of installed programs on macOS.
 * It implements the InstalledProgramsProvider interface.
 */
public class MacOSInstalledProgramsProvider implements InstalledProgramsProvider
{
    @Override
    public CompletableFuture< List< InstalledProgram > > getInstalledProgramsAsync()
    {
        return CompletableFuture.supplyAsync( () ->
        {
            try
            {
                List< Path > appBundles = getAppPaths();
                List< InstalledProgram > programs = new ArrayList<>();

                for ( Path appPath : appBundles )
                {
                    Path plistPath = appPath.resolve( "Contents" ).resolve( "Info.plist" );
                    if ( !Files.exists( plistPath ) )
                    {
                        continue;
                    }

                    Map< String, String > plist = readPlist( plistPath );

                    String defaultName = appPath.getFileName() != null
                            ? appPath.getFileName().toString().replace( ".app", "" )
                            : appPath.toString();

                    String name = plist.getOrDefault(
                            "CFBundleName",
                            plist.getOrDefault( "CFBundleDisplayName", defaultName )
                    );

                    String version = plist.getOrDefault(
                            "CFBundleShortVersionString",
                            plist.getOrDefault( "CFBundleVersion", "" )
                    );

                    programs.add( new InstalledProgram( name, version ) );
                }

                return programs;
            }
            catch ( Exception e )
            {
                throw new RuntimeException( e );
            }
        } );
    }

    private static List< Path > getAppPaths() throws IOException
    {
        ProcessBuilder pb = new ProcessBuilder(
                "find", "/Applications",
                "-mindepth", "1",
                "-maxdepth", "2",
                "-type", "d",
                "-name", "*.app"
        );
        ExecutionResult processResult =
                Executor.execute( pb, Duration.ofSeconds( 3 ) );

        List< Path > appBundles = new ArrayList<>();
        if ( processResult.success() )
        {
            for ( String line : processResult.output() )
            {
                appBundles.add( Path.of( line.trim() ) );
            }
        }
        else
        {
            throw new IOException( "Failed to get installed programs: " +
                    processResult.error() );
        }
        return appBundles;
    }

    /**
     * Reads a plist file and returns its key-value pairs as a map.
     * Used by macGetInstalledPrograms.
     *
     * @param plistPath Path to the plist file.
     * @return Map of key-value pairs from the plist file.
     * @throws IOException if an I/O error occurs.
     */
    private static Map< String, String > readPlist( Path plistPath ) throws IOException
    {
        ProcessBuilder pb = new ProcessBuilder( "plutil", "-p", plistPath.toString() );
        ExecutionResult processResult =
                Executor.execute( pb, Duration.ofSeconds( 3 ) );

        if ( processResult.success() )
        {
            Map< String, String > values = new HashMap<>();

            for ( String line : processResult.output() )
            {
                // common format:  "CFBundleName" => "Safari"
                // also may be:    "SomeKey" => 123
                int arrow = line.indexOf( "=>" );
                if ( arrow < 0 ) continue;

                String left = line.substring( 0, arrow ).trim();
                String right = line.substring( arrow + 2 ).trim();

                String key = StringEditor.stripQuotes( left );
                String value = StringEditor.stripQuotes( StringEditor.stripTrailingComma( right ) );

                if ( !key.isEmpty() )
                {
                    values.put( key, value );
                }
            }
            return values;
        }

        throw new IOException( "Failed to read plist " + plistPath +
                ": " + processResult.error() );
    }
}
