package com.rogad.bracketcounter

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    @Test
    fun `report is generated for java sources`(@TempDir dir: File) {
        writeJavaProject(dir)

        val result = runner(dir, "countOpeningBrackets").build()

        assertEquals(
            TaskOutcome.SUCCESS,
            result.task(":countOpeningBrackets")?.outcome,
        )
        val report = dir.resolve("build/bracket-counter/opening-brackets.txt")
        assertTrue(report.exists(), "report file must exist")
    }

    @Test
    fun `report contains per-file opening bracket count`(@TempDir dir: File) {
        writeJavaProject(dir)

        runner(dir, "countOpeningBrackets").build()

        val report = dir.resolve("build/bracket-counter/opening-brackets.txt").readText()
        assertTrue(
            report.contains("com/example/Hello.java: 3"),
            "unexpected report:\n$report",
        )
    }
}
