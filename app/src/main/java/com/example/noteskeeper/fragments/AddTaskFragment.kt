package com.example.noteskeeper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteskeeper.data.Task
import com.example.noteskeeper.utils.showToast
import com.example.noteskeeper.viewmodel.TaskViewModel
import com.example.noteskeeper.views.AddNotes
import com.example.noteskeeper.views.CCAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTaskFragment : Fragment() {
    private val taskViewModel: TaskViewModel by activityViewModels ()
    private val args: AddTaskFragmentArgs by navArgs()

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
    @Composable
    private fun MainContext() {
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
    @Composable
    fun AppContent() {
        AddNotes(viewModel = taskViewModel, task = args.taskExtra?: Task()){
            if(it==0) showToast(requireContext(),"Task Added") else showToast(requireContext(),"Task Updated")
            findNavController().popBackStack()
        }
    }
    @Composable
    private fun HeaderContent() {
        CCAppBar(context = requireContext(), title = if(args.taskExtra?.name?.isNotEmpty()==true)"Edit Notes" else "Add Notes", showMenu = false,){
            findNavController().popBackStack()
        }
    }
}