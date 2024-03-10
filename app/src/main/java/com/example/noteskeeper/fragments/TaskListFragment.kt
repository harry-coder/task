package com.example.noteskeeper.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.example.noteskeeper.R
import com.example.noteskeeper.data.DeeplinkHandler
import com.example.noteskeeper.data.DeeplinkHandlerImpl
import com.example.noteskeeper.data.Task
import com.example.noteskeeper.events.BottomSheetEvents
import com.example.noteskeeper.taskstate.RedirectionState
import com.example.noteskeeper.taskstate.SheetEnum
import com.example.noteskeeper.taskstate.TaskStateEnum
import com.example.noteskeeper.utils.logs.LoggerUtils
import com.example.noteskeeper.viewmodel.TaskViewModel
import com.example.noteskeeper.views.CCAppBar
import com.example.noteskeeper.views.InitialBottomSheetContent
import com.example.noteskeeper.views.NoteItems
import com.example.noteskeeper.views.PriorityBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val TAG = "TaskListFragment"
@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private val taskViewModel:TaskViewModel by viewModels ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.let {
            ComposeView(it).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MainContext()
                }
            }
        }
    }




    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun MainContext() {

        val modalSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { false },
            skipHalfExpanded = true,
        )
        val scope =rememberCoroutineScope()

        val sheetTypeState by remember {
            taskViewModel.sheetState
        }

        val isSheetFullScreen by remember { mutableStateOf(false) }
        val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp

        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
            sheetContent = {
               // LoggerUtils.logVerbose(TAG,"Sheet Type 1"+sheetTypeState.sheetAction)

                when (sheetTypeState.sheetAction) {
                   SheetEnum.SHOW->{
                       PriorityBottomSheet(context = requireActivity(), taskViewModel = taskViewModel, task =sheetTypeState.data )
                       LaunchedEffect(true ){
                           scope.launch { modalSheetState.show() }
                       }
                   }
                    SheetEnum.HIDE->{
                        taskViewModel.sheetState.value=taskViewModel.sheetState.value.copy(sheetAction = SheetEnum.NONE)
                    }
                    SheetEnum.NONE->{
                        InitialBottomSheetContent()
                        LaunchedEffect(true ){
                            scope.launch { modalSheetState.hide() }
                        }
                    }
                }
            }
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { HeaderContent() },
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it),
                ) {
                    AppContent()
                }
            }
        }

    }

     @SuppressLint("StateFlowValueCalledInComposition")
     @Composable
    fun AppContent() {

         /*LaunchedEffect(key1 = taskViewModel.redirectionState.value ){
             LoggerUtils.logVerbose(TAG,"Inside state ${taskViewModel.redirectionState.value.redirectionType} ")
            val dataValue=taskViewModel.redirectionState.value
             when(dataValue.redirectionType){
                 TaskStateEnum.REDIRECT_TO_ADD_NOTES->{
                     navigateToAddNotes()
                     taskViewModel.redirectionState.value=taskViewModel.redirectionState.value.copy(redirectionType = TaskStateEnum.NONE)
                 }
                 TaskStateEnum.REDIRECT_TO_EDIT_NOTES->{
                     navigateToAddNotes(dataValue.data)
                     taskViewModel.redirectionState.value=taskViewModel.redirectionState.value.copy(redirectionType = TaskStateEnum.NONE)
                 }
                 TaskStateEnum.NONE->{

                 }

             }
         }*/

         LaunchedEffect(key1 = true ){
             LoggerUtils.logVerbose(TAG,"Launched Called Times")

             taskViewModel.redirectionState.observe(viewLifecycleOwner){
                 when(it.redirectionType){
                     TaskStateEnum.REDIRECT_TO_ADD_NOTES->{
                         LoggerUtils.logVerbose(TAG,"Called Times")
                         navigateToAddNotes()
                         //  taskViewModel.redirectionState.value=taskViewModel.redirectionState.value.copy(redirectionType = TaskStateEnum.NONE)
                     }
                     TaskStateEnum.REDIRECT_TO_EDIT_NOTES->{
                         navigateToAddNotes(it.data)
                         // taskViewModel.redirectionState.value=taskViewModel.redirectionState.value.copy(redirectionType = TaskStateEnum.NONE)
                     }
                     TaskStateEnum.NONE->{
                         LoggerUtils.logVerbose(TAG," None Called Times")
                     }

                 }
             }
             /*taskViewModel.redirectionState.flowWithLifecycle(viewLifecycleOwner.lifecycle, minActiveState = Lifecycle.State.STARTED).distinctUntilChanged{
                 old, new ->LoggerUtils.logVerbose(TAG,"${old.redirectionType.name +" "+ new.redirectionType.name}")
                 old.redirectionType.name==new.redirectionType.name
             }.collect {
                 when(it.redirectionType){
                     TaskStateEnum.REDIRECT_TO_ADD_NOTES->{
                         LoggerUtils.logVerbose(TAG,"Called Times")
                         navigateToAddNotes()
                      //  taskViewModel.redirectionState.value=taskViewModel.redirectionState.value.copy(redirectionType = TaskStateEnum.NONE)
                     }
                     TaskStateEnum.REDIRECT_TO_EDIT_NOTES->{
                         navigateToAddNotes(it.data)
                        // taskViewModel.redirectionState.value=taskViewModel.redirectionState.value.copy(redirectionType = TaskStateEnum.NONE)
                     }
                     TaskStateEnum.NONE->{
                         LoggerUtils.logVerbose(TAG," None Called Times")
                     }

                 }
                 LoggerUtils.logVerbose(TAG,"Count :${taskViewModel.redirectionState.subscriptionCount.value}")

             }*/
         }

        NoteItems(viewModel = taskViewModel)
    }

    private fun navigateToAddNotes(task: Task= Task()){
        if(findNavController().currentDestination?.id== R.id.taskListFragment){
            val action=TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment(taskExtra=task)
            findNavController().navigate(action)
        }
    }

    @Composable
    private fun HeaderContent() {
        CCAppBar(context = requireContext(), taskViewModel = taskViewModel, title = "Tasks"){
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LoggerUtils.logVerbose(TAG,"On Destroy")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        LoggerUtils.logVerbose(TAG,"On DestroyView")
    }

}