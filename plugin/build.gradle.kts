plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.rogad"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(gradleTestKit())
    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
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
