package com.javacourse.errhandling;

import com.javacourse.errhandling.csvhandler.Verifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Application
{
  private Scanner sc;
  private static Logger logger=new Logger();

  public static void main(String[] args)
  {
    Application app=new Application();
    Verifier verifier=new Verifier();
    boolean isRunning=true;

    while (isRunning)
    {
      System.out.println("Path to csv file: ");
      app.sc=new Scanner(System.in);
      String path=app.sc.nextLine();
      try
      {
        verifier.verify(path);
      } catch (IllegalArgumentException | FileNotFoundException e) {
        logger.err(e, e.getMessage());
        continue;
      } catch (IOException e) {
        logger.err(e, "Can't perform read/write operations with a file");
        break;
      }
      isRunning=false;
    }
  }
}
