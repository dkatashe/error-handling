package com.javacourse.errhandling.csvhandler;

import com.javacourse.errhandling.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class Verifier
{
  private WebServiceCaller wsCaller=new WebServiceCaller();
  private Logger logger=new Logger();

  private ArrayList<String> readCsv(String path) throws IOException
  {
    ArrayList<String> content=new ArrayList<>();

    try (BufferedReader br=new BufferedReader(new FileReader(path)))
    {
      String line;

      while ((line=br.readLine()) != null)
      {
        content.add(line);
      }
    }
    catch (IOException e)
    {
      throw e;
    }

    return content;
  }

  private void writeCsv(String path, ArrayList<String> content) throws IOException
  {
    final String lineSep=System.getProperty("line.separator");

    try (BufferedWriter bw=new BufferedWriter(new FileWriter(path)))
    {
      for (String line : content)
      {
        bw.write(line + lineSep);
      }
    }
    catch (IOException e)
    {
      throw e;
    }
  }

  public void verify(String path) throws IllegalArgumentException, IOException
  {
    if (path.equals(""))
      throw new IllegalArgumentException("File name should not be empty");
    if (!path.endsWith(".csv"))
      throw new IllegalArgumentException("File should have an extension \".csv\"");

    ArrayList<String> csvContent=readCsv(path);
    int entries=0;

    for (int i=0; i < csvContent.size(); i++)
    {
      String line=csvContent.get(i);
      if (!line.startsWith("id"))
      {
        int id;
        try
        {
          String[] lineArr=line.split(",");
          id=Integer.parseInt(lineArr[0]);
          try
          {
            String verificationCode=wsCaller.getVerificationCode(id);
            line+="," + verificationCode;
          }
          catch (MalformedURLException | IllegalArgumentException e)
          {
            logger.err(e, "Wrong format of verification service URL");
            return;
          }
          catch (IOException e)
          {
            logger.err(e, "Can't connect to verification service");
            return;
          }
        }
        catch (NumberFormatException e)
        {
          logger.err(e, "Wrong format of ID (line " + i + ")");
          line+=",XXXXXXXXXXXXXXXXXX";
        }
        entries++;
      }
      else
      {
        line+=",verification_code";
      }
      csvContent.set(i, line);
    }

    writeCsv(path, csvContent);
    System.out.println(entries + " entries updated.");
  }
}
