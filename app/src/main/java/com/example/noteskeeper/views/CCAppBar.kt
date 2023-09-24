package com.example.noteskeeper.views

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.noteskeeper.R
import com.example.noteskeeper.events.SortEvents
import com.example.noteskeeper.utils.Textview
import com.example.noteskeeper.utils.color
import com.example.noteskeeper.utils.dp
import com.example.noteskeeper.utils.sp
import com.example.noteskeeper.viewmodel.TaskViewModel

@Composable
fun CCAppBar(
    context: Context,
    modifier: Modifier = Modifier,
    title: String = "Add Notes",
    showMenu: Boolean = true,
    taskViewModel: TaskViewModel? = null,
    onBackPress: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val listItems = remember {
        arrayOf("Sort By Date Asc", "Sort By Date Desc", "Sort By Status Complete")
    }


    TopAppBar(
        modifier = modifier.height(R.dimen._60sdp.dp()),
        backgroundColor = Color.White,
        elevation = R.dimen._2sdp.dp()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(R.dimen._50sdp.dp())
                .padding(horizontal = R.dimen._20sdp.dp()),
            horizontalArrangement = Arrangement.spacedBy(R.dimen._8sdp.dp()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "", modifier = Modifier
                    .size(dimensionResource(id = R.dimen._28sdp))
                    .padding(R.dimen._8sdp.dp())
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        onBackPress.invoke()
                    },
                tint = Color.Black
            )

            Textview(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f),
                text = title,
                fontSize = R.dimen._16ssp.sp(),
                color = R.color.sa_input_txt_color.color(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold
            )

            if (showMenu) {
                IconButton(onClick = {
                    expanded = true
                }) {
                    Icon(
                        contentDescription = "",
                        tint = Color.Black,
                        painter = painterResource(id = R.drawable.ic_options)
                    )
                }
            }

        }
        ShowMenu(expanded = expanded, listItems, viewModel = taskViewModel) {
            expanded = it
        }

    }
}

@Composable
private fun ShowMenu(
    expanded: Boolean,
    listItems: Array<String>,
    viewModel: TaskViewModel?,
    showState: (Boolean) -> Unit
) {

    val expandedState by rememberUpdatedState(newValue = expanded)
    val configuration = LocalConfiguration.current
    DropdownMenu(
        expanded = expandedState,
        onDismissRequest = {
            showState(false)
        },
        offset = DpOffset(x = (configuration.screenWidthDp - 10).dp, y = 10.dp)
    ) {
        // adding items
        listItems.forEachIndexed { itemIndex, itemValue ->
            DropdownMenuItem(
                onClick = {
                    when (itemIndex) {
                        0 -> {
                            viewModel?.onSortEvents(SortEvents.SortByDateAsc)
                        }
                        1 -> {
                            viewModel?.onSortEvents(SortEvents.SortByDateDesc)
                        }
                        else -> {
                            viewModel?.onSortEvents(SortEvents.SortByStatus)
                        }
                    }
                    showState(false)
                },

                //enabled = (itemIndex != disabledItem)
            ) {
                Text(text = itemValue)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CCAppBarPrev() {
    //  CCAppBar( context = LocalContext.current)
}
