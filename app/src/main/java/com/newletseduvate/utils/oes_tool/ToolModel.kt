package com.newletseduvate.utils.oes_tool


data class ToolModel(
        var toolName: String,
        var toolIcon: Int,
        var selected: Boolean,
        var toolType: ToolType
)