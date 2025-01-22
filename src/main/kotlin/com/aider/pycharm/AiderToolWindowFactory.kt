package com.aider.pycharm

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.JPanel
import javax.swing.JTextArea
import java.awt.BorderLayout

class AiderToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JPanel(BorderLayout())
        val chatArea = JTextArea()
        panel.add(chatArea, BorderLayout.CENTER)
        
        val content = ContentFactory.getInstance().createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
