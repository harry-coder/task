package com.example.noteskeeper.views

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteskeeper.R
import com.example.noteskeeper.data.Task
import com.example.noteskeeper.events.BottomSheetEvents
import com.example.noteskeeper.events.RedirectEvents
import com.example.noteskeeper.events.TaskCrudEvents
import com.example.noteskeeper.utils.Textview
import com.example.noteskeeper.utils.getColorForPriority
import com.example.noteskeeper.utils.showToast
import com.example.noteskeeper.viewmodel.TaskViewModel


@Composable
fun NoteItems(modifier: Modifier = Modifier, viewModel: TaskViewModel) {
    val context= LocalContext.current
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = modifier.padding(top = 10.dp)) {
            items(items = viewModel.taskList, key = { it.id }) {
                SingleNoteItem(viewModel = viewModel, task = it, context = context)
            }
            if (viewModel.noTaskAvailableState.value) {
                item {
                    NoResultView(
                        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen._20sdp)),
                        resourceId = R.drawable.no_search_found,
                        message = "No Tasks Available"
                    )
                }
            }

        }
        ExtendedFloatingActionButton(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 40.dp)
                .align(alignment = Alignment.BottomEnd),
            onClick = {
                viewModel.onRedirectionEvents(RedirectEvents.RedirectToAddNotes)

            },
            text = { Textview(text = "Add") },
            icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Add") }
        )
    }

}

@Composable
fun SingleNoteItem(modifier: Modifier = Modifier, viewModel: TaskViewModel, task: Task,context:Context) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)

            .clickable {
                viewModel.onRedirectionEvents(RedirectEvents.RedirectToEditNotes(task = task))
            }
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .shadow(
                5.dp,
                shape = RoundedCornerShape(size = 10.dp),
                ambientColor = Color.Green,
                spotColor = if (task.completed) Color.Green else Color.Blue
            )
        ,
        elevation = 5.dp,

        shape = RoundedCornerShape(size = 10.dp),
        //border = BorderStroke(1.dp,Color.Green),


    ) {

        Column(
            modifier = modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = modifier
                        .clickable {
                            viewModel.onBottomSheetEvents(BottomSheetEvents.ShowBottomSheet(task = task))
                        }
                        .size(10.dp)
                        .background(
                            color = getColorForPriority(getPriorityType(task.important)),
                            shape = CircleShape
                        )
                )
                Textview(
                    modifier = modifier
                        .weight(0.5f, true)
                        .clickable {
                            viewModel.onBottomSheetEvents(BottomSheetEvents.ShowBottomSheet(task = task))
                        },
                    text = getPriorityType(task.important),
                    color = Color.Gray,
                )
                Column( verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.End) {
                    Textview(
                        text = task.createdDateFormatted,
                        color = Color.Gray,
                        textAlign = TextAlign.End

                    )
                    Textview(
                        modifier = modifier,
                        text = if (task.completed) "Completed" else "Incomplete",
                        color = if (task.completed) Color.Green else Color.Blue,
                        fontSize = 10.sp
                    )
                }
            }

            Textview(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                text = task.name,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Textview(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                text = task.description,
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                fontSize = 14.sp

            )

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), horizontalArrangement = Arrangement.End
            ) {
                IconButton(modifier = modifier
                    .size(15.dp), onClick = {
                    viewModel.onCrudEvents(TaskCrudEvents.Delete(task = task))
                    showToast(context = context,"Task Deleted")
                }) {
                    Icon(
                        contentDescription = "",
                        tint = Color.Black,
                        imageVector = Icons.Filled.Delete
                    )
                }
            }

        }

    }
}
private fun getPriorityType(item:Int)=
    when(item){
       1-> "High"
       2-> "Medium"
       3-> "Low"
       else->"None"
    }


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NoteItemPrev() {
    // NoteItems(viewModel = TaskViewModel())
}