package com.sahdkhan.collections;

import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Getter
public class Monitor
{
    private String resolution;
    private String refreshRate;
    private String pixelDensity;
    private String name;
    private String id;

    public Monitor( final String name,
                    final String resolution,
                    final String refreshRate,
                    final String pixelDensity,
                    final String id )
    {
        this.name = name;
        this.resolution = resolution;
        this.refreshRate = refreshRate;
        this.pixelDensity = pixelDensity;
        this.id = id;
    }

    public static Path extractResourceToTempFile( String resourcePath )
            throws IOException
    {
        try ( InputStream in = Monitor.class.getResourceAsStream(resourcePath )) {
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }

            Path tempFile = Files.createTempFile("monitor-script-", ".ps1");
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING );
            tempFile.toFile().deleteOnExit();
            return tempFile;
        }
    }

}
