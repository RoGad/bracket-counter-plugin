package com.rogad.bracketcounter

import org.gradle.api.provider.Property

abstract class BracketCounterExtension {
    abstract val openingBrackets: Property<String>
}