package com.aider.pycharm

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.JLabel
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import java.awt.Insets

class AiderSettingsConfigurable(private val project: Project) : Configurable {
    private var settingsComponent: AiderSettingsComponent? = null

    override fun getDisplayName(): String = "Aider Chat"

    override fun createComponent(): JComponent {
        settingsComponent = AiderSettingsComponent()
        return settingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings = AiderSettings.getInstance(project)
        return settingsComponent?.commandLineParameters != settings.commandLineParameters
    }

    override fun apply() {
        val settings = AiderSettings.getInstance(project)
        settings.commandLineParameters = settingsComponent?.commandLineParameters ?: ""
    }

    override fun reset() {
        val settings = AiderSettings.getInstance(project)
        settingsComponent?.commandLineParameters = settings.commandLineParameters
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}

class AiderSettingsComponent {
    val panel: JPanel = JPanel(GridBagLayout())
    private val commandLineParamsField = JTextField()

    var commandLineParameters: String
        get() = commandLineParamsField.text
        set(value) {
            commandLineParamsField.text = value
        }

    init {
        val constraints = GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            insets = Insets(5, 5, 5, 5)
            weightx = 0.0
            gridx = 0
            gridy = 0
        }

        panel.add(JLabel("Command Line Parameters:"), constraints)

        constraints.gridx = 1
        constraints.weightx = 1.0
        panel.add(commandLineParamsField, constraints)
    }
}
