package com.sahdkhan.displayAndGPUInfo;

import com.sahdkhan.utilities.OSDetector;

public class DisplayAndGPUInfoProviderFactory
{
    /**
     * Private constructor to prevent instantiation.
     */
    private DisplayAndGPUInfoProviderFactory()
    {
    }

    /**
     * Creates a DisplayAndGPUInfoProvider instance based on the operating system.
     *
     * @return DisplayAndGPUInfoProvider instance for the current OS.
     * @throws UnsupportedOperationException if the OS is not supported.
     */
    public static DisplayAndGPUInfoProvider create()
    {
        if ( OSDetector.isWindows() )
        {
            return new WindowsDisplayAndGPUInfoProvider();
        }
        if ( OSDetector.isMac() )
        {
            return new MacOSDisplayAndGPUInfoProvider();
        }
//        if ( OSDetector.isLinux() )
//        {
//            return new LinuxDisplayAndGPUInfoProvider();
//        }
        throw new UnsupportedOperationException( "Unsupported OS" );
    }
}
