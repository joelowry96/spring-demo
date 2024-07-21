package com.joe.demo;

import org.springframework.stereotype.Service;

@Service
public class MyService {
  
      private final MyConfig myConfig;
  
      public MyService(MyConfig myConfig) {
          this.myConfig = myConfig;
      }
  
      public String getProperty() {
          return myConfig.getProperty();
      }
}
