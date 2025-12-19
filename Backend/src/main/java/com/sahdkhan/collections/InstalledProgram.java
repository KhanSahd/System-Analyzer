package com.sahdkhan.collections;

public class InstalledProgram
{
    private final String name;
    private final String version;
    private final String appPath;

    public InstalledProgram( String name, String version,
                             String appPath )
    {
        this.name = name;
        this.version = version;
        this.appPath = appPath;
    }

    public String getName()
    {
        return name;
    }

    public String getVersion()
    {
        return version;
    }

    public String getAppPath()
    {
        return appPath;
    }
}
