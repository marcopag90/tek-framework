package com.tek.core.dto;

import java.io.File;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class FileAttachmentDto {

  private File file;
  private String contentType;
  private String encoding;

  private FileAttachmentDto() {
  }

  public FileAttachmentDto(
      @NonNull File file,
      @Nullable String contentType,
      @Nullable String encoding
  ) {
    Objects.requireNonNull(file);
    this.file = file;
    this.contentType = contentType;
    this.encoding = encoding;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }
}
