package com.aider.pycharm

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

class OpenAiderSettingsAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let { project ->
            ShowSettingsUtil.getInstance().showSettingsDialog(project, "Aider Chat")
        }
    }
}
