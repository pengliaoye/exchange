package com.dm.demo;

import com.dm.system.JcrBean;

import java.net.URL;

public class DownloadFileGoogle {

  public static void main(String[] args) throws Exception {
    JcrBean.Search search = new JcrBean.Search("rtf", "", 0);
    URL[] urls = search.getURLs();
    for (URL url : urls) {
      System.out.println(url.toString());
    }
  }

}
