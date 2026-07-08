package com.rogad.bracketcounter

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction

abstract class CountOpeningBracketsTask : DefaultTask() {

    @get:InputFiles
    @get:SkipWhenEmpty
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceFiles: ConfigurableFileCollection

    @get:Internal
    abstract val sourceRoots: ConfigurableFileCollection

    @get:Input
    abstract val openingBrackets: Property<String>

    @get:OutputFile
    abstract val reportFile: RegularFileProperty

    @TaskAction
    fun count() {
        val brackets: Set<Char> = openingBrackets.get().toSet()
        val roots = sourceRoots.files
        val report = reportFile.get().asFile
        report.parentFile?.mkdirs()

        val lines = sourceFiles.files
            .map { file ->
                val relativePath = roots
                    .firstOrNull { root -> file.startsWith(root) }
                    ?.let { root -> root.toPath().relativize(file.toPath()).toString() }
                    ?: file.name
                val count = file.readText().count { ch -> ch in brackets }
                relativePath.replace('\\', '/') to count
            }
            .sortedBy { it.first }

        report.bufferedWriter().use { writer ->
            for ((path, count) in lines) {
                writer.write("$path: $count")
                writer.newLine()
            }
        }

        logger.lifecycle("Bracket report written to ${report.absolutePath} (${lines.size} files)")
    }
}
