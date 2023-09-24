package com.example.noteskeeper.taskstate

import com.example.noteskeeper.data.Task

enum class TaskStateEnum {
    REDIRECT_TO_ADD_NOTES,
    REDIRECT_TO_EDIT_NOTES,
    NONE

}

enum class SheetEnum {
    SHOW,
    HIDE,
    NONE

}


data class RedirectionState(
    val redirectionType: Enum<TaskStateEnum> = TaskStateEnum.NONE,
    val data: Task = Task()
)

data class BottomSheetState(
    val sheetAction: Enum<SheetEnum> = SheetEnum.NONE,
    val data: Task = Task()
)
