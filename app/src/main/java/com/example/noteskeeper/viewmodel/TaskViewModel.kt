package com.example.noteskeeper.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.noteskeeper.data.Task
import com.example.noteskeeper.data.TaskDao
import com.example.noteskeeper.events.*
import com.example.noteskeeper.taskstate.BottomSheetState
import com.example.noteskeeper.taskstate.RedirectionState
import com.example.noteskeeper.taskstate.SheetEnum
import com.example.noteskeeper.taskstate.TaskStateEnum
import com.example.noteskeeper.utils.logs.LoggerUtils
import com.example.noteskeeper.utils.showToast
import com.think.searchimage.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "TaskViewModel"

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskDao: TaskDao) : BaseViewModel() {

    //val redirectionState = mutableStateOf(RedirectionState())
    //val redirectionState = MutableSharedFlow<RedirectionState>(replay = 0, extraBufferCapacity = 1)
    //val redirectionState = MutableStateFlow(RedirectionState())
    val redirectionState = MutableLiveData(RedirectionState())
    val sheetState = mutableStateOf(BottomSheetState())

    val taskList = mutableStateListOf<Task>()
    val noTaskAvailableState = mutableStateOf(false)

    init {
        LoggerUtils.logVerbose("TaskListFragment","INIT with ${redirectionState.value?.redirectionType?.name}")
        getTask()
    }


   /* private fun generate()= flow<Int> {
        emit(100)
        emit(200)
    }

    val data=generate().stateIn(
        scope = viewModelScope
    )*/
    fun onRedirectionEvents(events: RedirectEvents) {
        LoggerUtils.logVerbose(TAG, events.toString())
        when (events) {

            RedirectEvents.RedirectToAddNotes -> {

                LoggerUtils.logVerbose("TaskListFragment","Event Add")
                val state=RedirectionState(redirectionType = TaskStateEnum.REDIRECT_TO_ADD_NOTES)
            redirectionState.value=(state)
            /*redirectionState.updateAndGet {
                    it.copy(redirectionType = TaskStateEnum.REDIRECT_TO_ADD_NOTES)
                }*/
                /*redirectionState.value =
                    redirectionState.value.copy(redirectionType = TaskStateEnum.REDIRECT_TO_ADD_NOTES)*/
            }
            is RedirectEvents.RedirectToEditNotes -> {
                LoggerUtils.logVerbose("TaskListFragment","Event Edit")
              /*  val state=RedirectionState(redirectionType = TaskStateEnum.REDIRECT_TO_EDIT_NOTES)
                redirectionState.tryEmit(state)*/
              /*  redirectionState.updateAndGet {
                    it.copy(redirectionType = TaskStateEnum.REDIRECT_TO_EDIT_NOTES)
                }*/

                /* redirectionState.value = redirectionState.value.copy(
                     data = events.task,
                     redirectionType = TaskStateEnum.REDIRECT_TO_EDIT_NOTES
                 )*/
            }

        }

    }

    fun onCrudEvents(events: TaskCrudEvents) {
        when (events) {
            is TaskCrudEvents.Add -> {
                addTask(events.task)
            }
            is TaskCrudEvents.Delete -> {
                deleteTask(events.task)

            }
            is TaskCrudEvents.Update -> {
                LoggerUtils.logVerbose(TAG, events.task.toString())
                editTask(events.task)

            }
        }
    }

    fun onSortEvents(events: SortEvents) {
        LoggerUtils.logVerbose(TAG, events.toString())
        when (events) {
            is SortEvents.SortByStatus -> {
                sortByStatus()
            }
            SortEvents.SortByDateAsc -> {
                sortByDate(SortType.Asc)
            }
            SortEvents.SortByDateDesc -> {
                sortByDate(SortType.Desc)
            }
        }
    }
    fun onBottomSheetEvents(events: BottomSheetEvents) {
        LoggerUtils.logVerbose(TAG, events.toString())
        when (events) {
            BottomSheetEvents.HideBottomSheet -> {
                sheetState.value =
                    sheetState.value.copy(sheetAction = SheetEnum.HIDE)
            }
            is BottomSheetEvents.ShowBottomSheet -> {
                sheetState.value =
                    sheetState.value.copy(sheetAction = SheetEnum.SHOW, data = events.task)
            }
        }
    }

    private fun sortByDate(sortType:SortType) {
        when(sortType){
            SortType.Asc->{
                taskList.sortBy { it.created }
            }
            SortType.Desc->{
                taskList.sortByDescending { it.created }
            }
        }

    }

    private fun sortByStatus() {
        taskList.sortByDescending { it.completed }
    }

    private fun getTask() {
        taskDao.getTasks().mapLatest {
            noTaskAvailableState.value = it.isEmpty()
            taskList.addAll(it.toSet() - taskList)


        }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = Unit)

       /* taskDao.getTasks().onEach {
            noTaskAvailableState.value = it.isEmpty()
            taskList.addAll(it.toSet() - taskList)

        }.launchIn(viewModelScope)*/
    }

    private fun addTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDao.insert(task)
            }
        }

    }

    private fun editTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDao.update(task)
            }
            val item = taskList.firstOrNull { it.id == task.id }
            val index = taskList.indexOf(item)
            taskList[index] = task

            sheetState.value = sheetState.value.copy(sheetAction = SheetEnum.NONE)
        }
    }

    private fun deleteTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskDao.delete(task)
            }
            taskList.remove(task)
        }
    }


    override fun onCleared() {
        super.onCleared()

        LoggerUtils.logVerbose("TaskListFragment","On Cleared Called")
    }
}
