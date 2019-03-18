package com.javacourse.errhandling;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger
{
  public void err(Exception e, String message)
  {
    System.err.println("Error: " + message);
    DateTimeFormatter formatterFilename=DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterLogTimestamp=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime now=LocalDateTime.now();
    String date=now.format(formatterFilename);
    String timestamp=now.format(formatterLogTimestamp);

    File logFile=new File("error_" + date + ".log");

    if (!logFile.exists())
    {
      try
      {
        logFile.createNewFile();
      }
      catch (IOException ex)
      {
        System.err.println("Internal error: Can't create log file");
        return;
      }
    }

    try
    {
      PrintStream ps=new PrintStream(new FileOutputStream(logFile, true));
      ps.print(timestamp + " Exception:\n");
      e.printStackTrace(ps);
      ps.close();
    }
    catch (FileNotFoundException ex)
    {
      System.err.println("Internal error: Log file was not found");
    }
  }
}
