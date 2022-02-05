package com.tek.core.properties.file;

/**
 * Configuration properties for file handling.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
public class TekFileProperties {

  private TmpFileProperties tmp = new TmpFileProperties();
  private BinaryFileProperties binary = new BinaryFileProperties();

  public TmpFileProperties getTmp() {
    return tmp;
  }

  public void setTmp(TmpFileProperties tmp) {
    this.tmp = tmp;
  }

  public BinaryFileProperties getBinary() {
    return binary;
  }

  public void setBinary(BinaryFileProperties binary) {
    this.binary = binary;
  }
}


