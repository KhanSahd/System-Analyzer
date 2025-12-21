package com.sahdkhan.programsInfo;


import com.sahdkhan.collections.InstalledProgram;

import java.util.List;

/**
 * This class contains methods that involve reading the installed programs
 * on the system.
 */
public interface InstalledProgramsProvider
{
    /**
     * Gets the list of installed programs on the system.
     *
     * @return List of installed programs on the system.
     * @throws Exception if an error occurs while retrieving the installed programs.
     */
    List< InstalledProgram > getInstalledPrograms() throws Exception;
}