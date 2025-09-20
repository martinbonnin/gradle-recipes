/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register

class CustomPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.plugins.withType(AppPlugin::class.java) {
            val androidComponents =
                project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant ->
                val generateKotlinSources =
                    project.tasks.register<GenerateKotlinSources>("generate${variant.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}KotlinSources")

                // Changing to sources.java here makes it work
                variant.sources.kotlin!!.addGeneratedSourceDirectory(
                    generateKotlinSources,
                    GenerateKotlinSources::outputDirectory
                )
            }
        }
    }
}

abstract class GenerateKotlinSources: DefaultTask() {
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun taskAction() {
        File(outputDirectory.get().asFile, "hello.kt")
            .writeText("val hello = \"hello world\"")
    }
}

