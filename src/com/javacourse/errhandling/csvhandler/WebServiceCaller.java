package com.javacourse.errhandling.csvhandler;

import com.javacourse.errhandling.Logger;

import java.io.*;
import java.net.*;
import java.util.Properties;

public class WebServiceCaller
{
  private static Logger logger=new Logger();

  private String getWebServiceUrl()
  {
    String url="";
    Properties props=new Properties();
    try
    {
      props.load(new FileInputStream("webservice.properties"));
      url=props.getProperty("host");
    }
    catch (FileNotFoundException e)
    {
      logger.err(e, "Can't find properties file");
    }
    catch (IOException e)
    {
      logger.err(e, "Can't load properties file");
    }

    return url;
  }

  public String getVerificationCode(int id) throws IllegalArgumentException, IOException
  {
    URL wsUrl;
    URLConnection connection;
    InputStream input;
    String verificationCode="XXXXXXXXXXXXXXXXXX";

    // Create web service URL
    wsUrl=new URL(this.getWebServiceUrl() + "/get-verification-code/" + id);
    // Connect to web service
    connection=wsUrl.openConnection();
    // Get response
    input=connection.getInputStream();

    // Parse response
    try (BufferedReader br=new BufferedReader(new InputStreamReader(input)))
    {
      String line=br.readLine();
      if (line.startsWith("For your id '" + id + "'"))
      {
        verificationCode=line.substring(37 + String.valueOf(id).length());
      }
    }
    catch (IOException e)
    {
      logger.err(e, "Invalid response from verification service for id " + id);
    }

    return verificationCode;
  }
}
