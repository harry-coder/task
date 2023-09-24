package com.example.noteskeeper.events

import com.example.noteskeeper.data.Task

sealed class RedirectEvents {
    object RedirectToAddNotes : RedirectEvents()
    data class RedirectToEditNotes(val task: Task) : RedirectEvents()

}
sealed class BottomSheetEvents {
    data class ShowBottomSheet(val task: Task) : BottomSheetEvents()
    object HideBottomSheet : BottomSheetEvents()

}


sealed class TaskCrudEvents {
    data class Add(val task: Task) : TaskCrudEvents()
    data class Delete(val task: Task) : TaskCrudEvents()
    data class Update(val task: Task) : TaskCrudEvents()
}
sealed class SortEvents {
    object SortByDateAsc : SortEvents()
    object SortByDateDesc : SortEvents()
    object SortByStatus : SortEvents()
}
enum class SortType{
    Asc,
    Desc
}

