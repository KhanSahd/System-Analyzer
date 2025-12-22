package com.sahdkhan.collections;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing display information.
 */
@Getter
@Setter
public class DisplayInfo
{
    private String model;
    private String cores;
    private boolean hasMultipleDisplays;
    List< Monitor > monitors = new ArrayList<>();

    public DisplayInfo()
    {

    }
}
