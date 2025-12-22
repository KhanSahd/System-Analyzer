package com.sahdkhan.programsInfo;

import com.sahdkhan.collections.InstalledProgram;
import com.sahdkhan.utilities.StringEditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides methods to get the list of installed programs on macOS.
 * It implements the InstalledProgramsProvider interface.
 */
public class MacOSInstalledProgramsProvider implements InstalledProgramsProvider
{
    @Override
    public List< InstalledProgram > getInstalledPrograms() throws Exception
    {
        ProcessBuilder pb = new ProcessBuilder(
                "find", "/Applications",
                "-mindepth", "1",
                "-maxdepth", "2",
                "-type", "d",
                "-name", "*.app"
        );
        pb.redirectErrorStream( true );

        Process process = pb.start();

        List< Path > appBundles = new ArrayList<>();

        try ( BufferedReader reader = new BufferedReader(
                new InputStreamReader( process.getInputStream() ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                if ( !line.isBlank() )
                {
                    appBundles.add( Path.of( line.trim() ) );
                }
            }
        }

        waitForOrThrow( process, "find /Applications" );

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

            String name = plist.getOrDefault( "CFBundleName",
                    plist.getOrDefault( "CFBundleDisplayName", defaultName ) );

            String version = plist.getOrDefault( "CFBundleShortVersionString",
                    plist.getOrDefault( "CFBundleVersion", "" ) );

            programs.add( new InstalledProgram( name, version ) );
        }

        return programs;
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
        ProcessBuilder pb = new ProcessBuilder(
                "plutil", "-p", plistPath.toString()
        );
        pb.redirectErrorStream( true );

        Process p = pb.start();

        Map< String, String > values = new HashMap<>();

        try ( BufferedReader reader = new BufferedReader(
                new InputStreamReader( p.getInputStream() ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
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
        }

        waitForOrThrow( p, "plutil -p " + plistPath );

        return values;
    }

    /**
     * Waits for the given process to complete and throws an IOException if it exits with a non-zero code.
     *
     * @param p    The process to wait for.
     * @param what A description of the process for error messages.
     * @throws IOException if the process exits with a non-zero code or is interrupted.
     */
    private static void waitForOrThrow( Process p, String what ) throws IOException
    {
        try
        {
            int code = p.waitFor();
            if ( code != 0 )
            {
                throw new IOException( what + " failed with exit code " + code );
            }
        }
        catch ( InterruptedException e )
        {
            Thread.currentThread().interrupt();
            throw new IOException( "Interrupted while running: " + what, e );
        }
    }
}
