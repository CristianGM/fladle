package com.osacky.flank.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class YamlConfigWriterTask @Inject constructor(private val extension: FlankGradleExtension, projectLayout: ProjectLayout, objectFactory: ObjectFactory) : DefaultTask() {

  private val yamlWriter = YamlWriter()

  @get:Nested
  var fladleConfig: FladleConfig? = null

  @get:OutputFile
  val flankConfig: RegularFileProperty =
    objectFactory.fileProperty().convention(projectLayout.fladleDir.map { it.file("flank.yml") })

  @Internal
  override fun getDescription(): String {
    return "Writes a flank.yml file based on the current FlankGradleExtension configuration."
  }

  @Internal
  override fun getGroup(): String {
    return FladlePluginDelegate.TASK_GROUP
  }

  @TaskAction
  fun writeFile() {
    val fladleConfig = checkNotNull(fladleConfig)
    flankConfig.get().asFile.writeText(yamlWriter.createConfigProps(fladleConfig, extension))
  }
}
