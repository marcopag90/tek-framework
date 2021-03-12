package com.tek.core.properties.file;

import lombok.Data;

/**
 * Configuration properties for file handling.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Data
public class TekFileProperties {

  private TmpProperties tmp = new TmpProperties();
  private BinaryProperties binary = new BinaryProperties();
}


