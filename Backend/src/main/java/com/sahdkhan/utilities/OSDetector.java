package com.sahdkhan.utilities;

public final class OSDetector
{
    private static final String OS_NAME =
            System.getProperty( "os.name" ).toLowerCase();

    private OSDetector()
    {
        // utility class - prevent instantiation
    }

    public static boolean isWindows()
    {
        return OS_NAME.contains( "win" );
    }

    public static boolean isMac()
    {
        return OS_NAME.contains( "mac" );
    }

    public static boolean isLinux()
    {
        return OS_NAME.contains( "nux" )
                || OS_NAME.contains( "nix" )
                || OS_NAME.contains( "aix" );
    }

    public static String getOsName()
    {
        return OS_NAME;
    }
}
