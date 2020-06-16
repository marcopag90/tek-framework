package com.tek.core.prop;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for email handling.
 * <p>
 * Fallback to default configuration if none provided.
 *
 * @author MarcoPagan
 */
@Getter
@Setter
public class TekMailProperties {

    private Boolean sendErrors = false;
    private Boolean realDelivery = false;
}
