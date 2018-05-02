package com.predera.dmml.service.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("storage")
public class ModelUploadStorageProperties {

 /**
  * Folder location for storing files
  */
 private String location;

 public String getLocation() {
  return location;
 }

 public void setLocation(String location) {
  this.location = location;
 }

}