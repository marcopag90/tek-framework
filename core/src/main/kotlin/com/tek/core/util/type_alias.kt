package com.tek.core.util

import com.tek.core.TekCoreProperties
import java.time.Duration

/**kotlin typealias referencing properties
 *
 * stored in application-{[com.tek.core.SpringProfile]}.yaml and referencing [TekCoreProperties] */
typealias TekProperty = String

typealias TekDurationProperty = Duration
