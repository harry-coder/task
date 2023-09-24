package com.example.noteskeeper.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteskeeper.R
import com.example.noteskeeper.data.Task
import com.example.noteskeeper.events.BottomSheetEvents
import com.example.noteskeeper.events.TaskCrudEvents
import com.example.noteskeeper.utils.*
import com.example.noteskeeper.viewmodel.TaskViewModel


private const val TAG = "CardOptionBottomSheet"
@Composable
fun PriorityBottomSheet(
    modifier: Modifier = Modifier,
    context: Context,
    taskViewModel: TaskViewModel,
    task:Task

) {
    Surface(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            ImageView(
                drawableId = R.drawable.ic_cancel,
                contentDescription = "",
                modifier = modifier
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        taskViewModel.onBottomSheetEvents(BottomSheetEvents.HideBottomSheet)
                    }
                    .padding(top = R.dimen._17sdp.dp(), end = R.dimen._20sdp.dp())
                    .size(R.dimen._24sdp.dp())
                    .align(Alignment.End),
                contentScale = ContentScale.Fit
            )

            LazyColumn() {
                item {
                    Textview(modifier = modifier.padding(start = 10.dp), text = "Set Priority")
                }
                itemsIndexed(items = arrayOf("High","Medium","Low")) { index, item ->
                    OptionsLinks(option =  item) {
                        when(index){
                            0->{
                                taskViewModel.onCrudEvents(TaskCrudEvents.Update(task.copy(important = 1)))
                            }
                            1->{
                                taskViewModel.onCrudEvents(TaskCrudEvents.Update(task.copy(important = 2)))
                            }
                            2->{
                                taskViewModel.onCrudEvents(TaskCrudEvents.Update(task.copy(important = 3)))
                            }
                        }

                    }
                    if (index != 2) {
                        Divider(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = R.dimen._20sdp.dp()), color = Color(0xFFEAEAEA)
                        )
                    }

                }
            }


        }
    }

}

@Composable
private fun OptionsLinks(modifier: Modifier = Modifier, option: String, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .clickable {
                onClick.invoke()
            }
            .fillMaxWidth()
            .padding(20.dp),

        verticalAlignment = Alignment.CenterVertically

    ) {
        Box(
            modifier = modifier
                .size(10.dp)
                .background(color = getColorForPriority(item = option) , shape = CircleShape)
        )

        Text(
            modifier = modifier
                .weight(1f)
                .padding(start = 14.dp),
            text = option,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF222222),
            )
        )

    }

}


@Composable
fun InitialBottomSheetContent() {
    Spacer(
        modifier = Modifier
            .alpha(0f)
            .width(1.dp)
            .height(1.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CardOptionBottomSheetPrev() {
   /* PriorityBottomSheet(
        context = LocalContext.current,

    )*/
}
