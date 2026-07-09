package com.rogad.bracketcounter

import org.gradle.testkit.runner.GradleRunner
import java.io.File

class BracketCounterPluginFunctionalTest {

    private fun runner(projectDir: File, vararg args: String): GradleRunner =
        GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments(*args)
            .forwardOutput()

    private fun writeJavaProject(dir: File) {
        dir.resolve("settings.gradle.kts")
            .writeText("rootProject.name = \"consumer\"")
        dir.resolve("build.gradle.kts").writeText(
            """
            plugins {
                `java-library`
                id("com.rogad.bracket-counter")
            }
            """.trimIndent(),
        )
        val src = dir.resolve("src/main/java/com/example").apply { mkdirs() }
        src.resolve("Hello.java").writeText(
            """
            package com.example;

            public class Hello {
                public String greet(String name) {
                    return "Hi, " + name + "!";
                }
            }
            """.trimIndent(),
        )
    }
}
