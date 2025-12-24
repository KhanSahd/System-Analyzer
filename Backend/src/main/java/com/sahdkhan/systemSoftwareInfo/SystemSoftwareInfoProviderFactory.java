package com.sahdkhan.systemSoftwareInfo;

import com.sahdkhan.utilities.OSDetector;

public class SystemSoftwareInfoProviderFactory
{
    private SystemSoftwareInfoProviderFactory()
    {
    }

    public static SystemSoftwareInfoProvider create()
    {
        if ( OSDetector.isMac() )
        {
            return new MacSystemSoftwareInfoProvider();
        }
        if ( OSDetector.isWindows() )
        {
            return new WindowsSystemSoftwareInfoProvider();
        }
        throw new UnsupportedOperationException( "Unsupported OS" );
    }
}
