package com.sahdkhan.utilities.collections;

import java.util.List;

/**
 * Represents the result of executing a process.
 *
 * @param success  indicates if the execution was successful
 * @param timedOut indicates if the execution timed out
 * @param exitCode the exit code of the process
 * @param output   the output lines from the process
 * @param error    the error message if any
 */
public record ExecutionResult( boolean success, boolean timedOut,
                               int exitCode,
                               List< String > output,
                               String error
)
{
    /**
     * Creates a successful ExecutionResult.
     *
     * @param output   the output lines from the process
     * @param exitCode the exit code of the process
     * @return ExecutionResult representing success
     */
    public static ExecutionResult success( List< String > output, int exitCode )
    {
        return new ExecutionResult( true, false, exitCode, output, null );
    }

    /**
     * Creates a timeout ExecutionResult.
     *
     * @return ExecutionResult representing a timeout
     */
    public static ExecutionResult timeout()
    {
        return new ExecutionResult( false,
                true,
                -1,
                List.of(),
                "Process timed out" );
    }

    /**
     * Creates a failure ExecutionResult.
     *
     * @param error the error message
     * @return ExecutionResult representing failure
     */
    public static ExecutionResult failure( String error )
    {
        return new ExecutionResult( false, false, -1, List.of(), error );
    }
}
