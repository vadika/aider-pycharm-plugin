package com.aider.pycharm

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.aider.pycharm.AiderSettings",
    storages = [Storage("aider.xml")]
)
class AiderSettings : PersistentStateComponent<AiderSettings> {
    var commandLineParameters: String = ""

    override fun getState(): AiderSettings = this

    override fun loadState(state: AiderSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): AiderSettings =
            project.getService(AiderSettings::class.java)
    }
}
