package com.strobel.decompiler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.strobel.annotations.NotNull;

final class BriefLogFormatter extends Formatter {
    private static final DateFormat format  = new SimpleDateFormat("h:mm:ss");
    private static final String     lineSep = System.getProperty("line.separator");

    BriefLogFormatter() {
    }

    public String format(@NotNull LogRecord record) {
        String loggerName = record.getLoggerName();
        if (loggerName == null) {
            loggerName = "root";
        }

        return format.format(new Date(record.getMillis())) + " [" + record.getLevel() + "] " + loggerName + ": "
                + record.getMessage() + ' ' + lineSep;
    }
}
