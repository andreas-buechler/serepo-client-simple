package ch.hsr.isf.serepo.client.simple.api.helpers;

import ch.hsr.isf.serepo.client.simple.api.entities.CreateSeItem;

public class SeItemWithContent extends CreateSeItem implements ISeItemWithContent {

  private byte[] content;
  private String mimeType;

  public SeItemWithContent() {}

  public SeItemWithContent(byte[] content, String mimeType) {
    super();
    this.content = content;
    this.mimeType = mimeType;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

}
