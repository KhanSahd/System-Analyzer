package com.sahdkhan.programsInfo;

import com.sahdkhan.utilities.OSDetector;

/**
 * Factory class to create InstalledProgramsProvider instances based on the operating system.
 */
public class InstalledProgramsFactory
{
    /**
     * Private constructor to prevent instantiation.
     */
    private InstalledProgramsFactory()
    {
    }

    /**
     * Creates an InstalledProgramsProvider instance based on the operating system.
     *
     * @return InstalledProgramsProvider instance for the current OS.
     * @throws UnsupportedOperationException if the OS is not supported.
     */
    public static InstalledProgramsProvider create()
    {
        if ( OSDetector.isWindows() )
        {
            return new WindowsInstalledProgramsProvider();
        }

        if ( OSDetector.isMac() )
        {
            return new MacOSInstalledProgramsProvider();
        }

//        if ( OSDetector.isLinux() )
//        {
//            return new LinuxInstalledProgramsProvider();
//        }
        // TODO: Implement windows and linux providers

        throw new UnsupportedOperationException( "Unsupported OS" );
    }
}
