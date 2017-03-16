package ch.hsr.isf.serepo.client.simple.api.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ISeItemWithContent extends ISeItem, ISeItemContent {

  @JsonIgnore
  byte[] getContent();

  @JsonIgnore
  String getMimeType();

}
