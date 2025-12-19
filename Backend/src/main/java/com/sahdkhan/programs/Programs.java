package com.sahdkhan.programs;


import com.sahdkhan.collections.InstalledProgram;
import com.sahdkhan.utilities.OSDetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Programs
{
    /**
     *
     * @return
     * @throws IOException
     */
    public static List< InstalledProgram > getInstalledPrograms()
            throws IOException
    {
        if ( OSDetector.isMac() )
        {
            return macGetInstalledPrograms();
        }
        // TODO: Implement windows and linux methods
        return List.of();
    }

    private static List< InstalledProgram > macGetInstalledPrograms() throws IOException
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

            programs.add( new InstalledProgram( name, version, appPath.toString() ) );
        }

        return programs;
    }

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

                String key = stripQuotes( left );
                String value = stripQuotes( stripTrailingComma( right ) );

                if ( !key.isEmpty() )
                {
                    values.put( key, value );
                }
            }
        }

        waitForOrThrow( p, "plutil -p " + plistPath );

        return values;
    }

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

    private static String stripQuotes( String s )
    {
        s = s.trim();
        if ( s.startsWith( "\"" ) ) s = s.substring( 1 );
        if ( s.endsWith( "\"" ) ) s = s.substring( 0, s.length() - 1 );
        return s.trim();
    }

    private static String stripTrailingComma( String s )
    {
        s = s.trim();
        if ( s.endsWith( "," ) ) s = s.substring( 0, s.length() - 1 );
        return s.trim();
    }
}