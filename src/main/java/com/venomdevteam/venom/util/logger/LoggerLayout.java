package com.venomdevteam.venom.util.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerLayout extends LayoutBase<ILoggingEvent> {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    String prefix = null;

    boolean printThreadName = false;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPrintThreadName(boolean printThreadName) {
        this.printThreadName = printThreadName;
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        StringBuilder sb = new StringBuilder(128);
        if (prefix != null) {
            sb.append(ANSI_BLUE).append(prefix).append(ANSI_YELLOW).append(" > ").append(ANSI_RESET);
        }
        DateFormat format = new SimpleDateFormat("HH:mm");
        sb.append(ANSI_PURPLE).append(format.format(new Date(event.getTimeStamp()))).append(ANSI_RESET);
        sb.append(ANSI_YELLOW + " > " + ANSI_RESET);
        if (event.getLevel().equals(Level.ALL) || event.getLevel().equals(Level.INFO)) {
            sb.append(ANSI_BLUE + "[LOG]" + ANSI_YELLOW + " > " + ANSI_RESET);
        } else if (event.getLevel().equals(Level.DEBUG)) {
            sb.append(ANSI_PURPLE + "[DEBUG]" + ANSI_YELLOW + " > " + ANSI_RESET);
        } else if (event.getLevel().equals(Level.WARN)) {
            sb.append(ANSI_YELLOW + "[WARN] > " + ANSI_RESET);
        } else if (event.getLevel().equals(Level.ERROR)) {
            sb.append(ANSI_RED + "[ERROR]" + ANSI_YELLOW + " > " + ANSI_RESET);
            sb.append(ANSI_CYAN + "[")
                    .append(event.getCallerData()[0].getClassName())
                    .append(":").append(event.getCallerData()[0].getLineNumber())
                    .append("]")
                    .append(ANSI_RESET);
            sb.append(ANSI_YELLOW + " > " + ANSI_RESET);
        } else {
            sb.append(ANSI_GREEN + "[OTHER]" + ANSI_YELLOW + " > " + ANSI_RESET);
        }
        sb.append(ANSI_PURPLE + "[").append(event.getThreadName()).append("]").append(ANSI_RESET);
        sb.append(ANSI_YELLOW + " > " + ANSI_RESET);
        sb.append(event.getLoggerName());
        sb.append(ANSI_YELLOW + " > " + ANSI_RESET);
        sb.append(event.getFormattedMessage());
        sb.append(CoreConstants.LINE_SEPARATOR);
        return sb.toString();
    }
}
