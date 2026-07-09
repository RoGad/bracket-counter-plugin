plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.rogad"
version = "0.1.0"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("bracketCounter") {
            id = "com.rogad.bracket-counter"
            implementationClass = "com.rogad.bracketcounter.BracketCounterPlugin"
            displayName = "Bracket Counter Plugin"
            description = "Adds a text file with the number of opening brackets " +
                    "per source file into the final JAR of a Java module."
        }
    }
}
