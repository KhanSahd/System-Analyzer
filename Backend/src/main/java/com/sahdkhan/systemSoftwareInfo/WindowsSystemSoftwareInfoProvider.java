package com.sahdkhan.systemSoftwareInfo;

import com.sahdkhan.utilities.Executor;
import com.sahdkhan.utilities.StringEditor;
import com.sahdkhan.utilities.collections.ExecutionResult;

import java.time.Duration;

public class WindowsSystemSoftwareInfoProvider implements SystemSoftwareInfoProvider
{
    public WindowsSystemSoftwareInfoProvider()
    {
    }

    @Override
    public String getOSName()
    {
        ProcessBuilder pb = new ProcessBuilder(
                "powershell",
                "-Command",
                "(Get-CimInstance Win32_OperatingSystem).Caption" );
        ExecutionResult result = Executor.execute( pb, Duration.ofSeconds( 3 ) );
        if ( result.success() )
        {
            return StringEditor.stripBrackets(result.output().toString().trim() );
        }
        return "Unknown";
    }

    @Override
    public String getOSVersion()
    {
        ProcessBuilder pb = new ProcessBuilder(
                "powershell",
                "-NoProfile",
                "-Command",
                "(Get-CimInstance Win32_OperatingSystem | Select-Object -ExpandProperty Version)"
        );
        ExecutionResult result = Executor.execute( pb, Duration.ofSeconds( 3 ) );
        if ( result.success() )
        {
            return StringEditor.stripBrackets(result.output().toString().trim() );
        }
        return "Unknown";
    }

    @Override
    public String getComputerName()
    {
        return "";
    }

    @Override
    public String getSystemUptime()
    {
        return "";
    }

    @Override
    public String getSystemUser()
    {
        return "";
    }

    @Override
    public String getBootMode()
    {
        return "";
    }

    @Override
    public String getKernelVersion()
    {
        return "";
    }
}
