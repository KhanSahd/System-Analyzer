package com.sahdkhan.programsInfo;


import com.sahdkhan.collections.InstalledProgram;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class contains methods that involve reading the installed programs
 * on the system.
 */
public interface InstalledProgramsProvider
{

    /**
     * Asynchronously gets the list of installed programs on the system.
     *
     * @return CompletableFuture containing the list of installed programs.
     */
    CompletableFuture< List< InstalledProgram > > getInstalledProgramsAsync();

    /**
     * Gets the list of installed programs on the system.
     *
     * @return List of installed programs on the system.
     * @throws Exception if an error occurs while retrieving the installed programs.
     */
    default List< InstalledProgram > getInstalledPrograms()
    {
        return getInstalledProgramsAsync().join();
    }
}