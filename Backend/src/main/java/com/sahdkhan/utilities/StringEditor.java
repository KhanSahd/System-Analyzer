package com.sahdkhan.utilities;

public class StringEditor
{
    /**
     * Strips leading and trailing quotes from a string.
     *
     * @param s The string to strip quotes from.
     * @return The string without leading and trailing quotes.
     */
    public static String stripQuotes( String s )
    {
        s = s.trim();
        if ( s.startsWith( "\"" ) ) s = s.substring( 1 );
        if ( s.endsWith( "\"" ) ) s = s.substring( 0, s.length() - 1 );
        return s.trim();
    }

    /**
     * Strips a trailing comma from a string.
     *
     * @param s The string to strip the trailing comma from.
     * @return The string without a trailing comma.
     */
    public static String stripTrailingComma( String s )
    {
        s = s.trim();
        if ( s.endsWith( "," ) ) s = s.substring( 0, s.length() - 1 );
        return s.trim();
    }
}
