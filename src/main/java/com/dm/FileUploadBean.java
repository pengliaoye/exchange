/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.dm;

import java.io.InputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.enterprise.inject.Model;
import javax.servlet.http.Part;

/**
 * 
 * @author Administrator
 */
@Model
public class FileUploadBean {

  private Part uploadedFile;

  public Part getUploadedFile() {
    return uploadedFile;
  }

  public void setUploadedFile(Part uploadedFile) {
    this.uploadedFile = uploadedFile;
  }

  public String getFileText() {
    String text = "";
    if (null != uploadedFile) {
      try (InputStream is = uploadedFile.getInputStream(); 
          Scanner scanner = new Scanner(is)) {
        text = scanner.useDelimiter("\\A").next();
      } catch (IOException ex) {
          ex.printStackTrace();
      }
    }
    return text;
  }

  public void upload() {}
}
