package com.rogad.bracketcounter

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.register

class BracketCounterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create(
            EXTENSION_NAME,
            BracketCounterExtension::class.java,
        )
        extension.openingBrackets.convention("({[")

        project.plugins.withType(JavaPlugin::class.java) {
            val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
            val mainSourceSet = javaExtension.sourceSets
                .getByName(SourceSet.MAIN_SOURCE_SET_NAME)

            project.tasks.register<CountOpeningBracketsTask>(COUNT_TASK_NAME) {
                group = "verification"
                description = "Counts opening brackets in each Java source file."
                sourceFiles.setFrom(mainSourceSet.allJava)
                sourceRoots.setFrom(mainSourceSet.allJava.sourceDirectories)
                openingBrackets.set(extension.openingBrackets)
                reportFile.set(
                    project.layout.buildDirectory.file("bracket-counter/opening-brackets.txt"),
                )
            }
        }
    }

    companion object {
        const val EXTENSION_NAME = "bracketCounter"
        const val COUNT_TASK_NAME = "countOpeningBrackets"
    }
}
