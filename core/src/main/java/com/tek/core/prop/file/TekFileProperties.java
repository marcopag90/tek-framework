package com.tek.core.prop.file;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for file handling.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Getter
@Setter
public class TekFileProperties {

    private TmpProperties tmp = new TmpProperties();
    private BinaryProperties binary = new BinaryProperties();
}


