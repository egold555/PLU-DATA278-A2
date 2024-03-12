package org.golde.plu.datawrangler.data;

public class CSVParseException extends Exception {

    public CSVParseException(String message) {
        super(message);
    }

    public CSVParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
