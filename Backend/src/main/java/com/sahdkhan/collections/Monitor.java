package com.sahdkhan.collections;

import lombok.Getter;

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
}
