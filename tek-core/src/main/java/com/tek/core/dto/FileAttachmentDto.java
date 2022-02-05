package com.tek.core.dto;

import java.io.File;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Getter
@Setter
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

}
