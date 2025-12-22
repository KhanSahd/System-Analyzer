package com.sahdkhan.utilities;

import com.sahdkhan.utilities.collections.ExecutionResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Executor
{

    private static final ExecutorService DEFAULT_EXECUTOR =
            Executors.newCachedThreadPool();

    /**
     * Private constructor to prevent instantiation.
     */
    private Executor()
    {
    }

    /**
     * Synchronous execution of a process with timeout.
     *
     * @param processBuilder the ProcessBuilder to execute
     * @param timeout        the timeout duration
     * @return ExecutionResult containing output, exit code, or error
     */
    public static ExecutionResult execute( ProcessBuilder processBuilder, Duration timeout
    )
    {
        try
        {
            return executeInternal( processBuilder, timeout );
        }
        catch ( Exception e )
        {
            return ExecutionResult.failure( e.getMessage() );
        }
    }

    /**
     * Asynchronous execution of a process with timeout.
     *
     * @param processBuilder the ProcessBuilder to execute
     * @param timeout        the timeout duration
     * @return CompletableFuture of ExecutionResult
     */
    public static CompletableFuture< ExecutionResult > executeAsync(
            ProcessBuilder processBuilder,
            Duration timeout
    )
    {
        return CompletableFuture.supplyAsync(
                () -> execute( processBuilder, timeout ),
                DEFAULT_EXECUTOR
        );
    }

    /**
     * Internal method to execute a process and handle timeout.
     *
     * @param processBuilder the ProcessBuilder to execute
     * @param timeout        the timeout duration
     * @return ExecutionResult containing output, exit code, or timeout
     * @throws Exception if an error occurs during execution
     */
    private static ExecutionResult executeInternal(
            ProcessBuilder processBuilder,
            Duration timeout
    ) throws Exception
    {
        processBuilder.redirectErrorStream( true );

        Process process = processBuilder.start();

        List< String > outputLines = readProcessOutput( process );

        boolean finished = process.waitFor(
                timeout.toMillis(),
                TimeUnit.MILLISECONDS
        );

        if ( !finished )
        {
            process.destroyForcibly();
            return ExecutionResult.timeout();
        }

        return ExecutionResult.success(
                outputLines,
                process.exitValue()
        );
    }

    /**
     * Reads the output of a process.
     *
     * @param process the Process to read output from
     * @return List of output lines
     */
    private static List< String > readProcessOutput( Process process )
    {
        List< String > outputLines = new ArrayList<>();

        try ( BufferedReader reader = new BufferedReader(
                new InputStreamReader( process.getInputStream() ) ) )
        {
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                if ( line.isBlank() )
                {
                    continue;
                }
                outputLines.add( line );
            }
        }
        catch ( Exception ignored )
        {
        }

        return outputLines;
    }

}
