package com.example.noteskeeper.views

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteskeeper.R
import com.example.noteskeeper.data.Task
import com.example.noteskeeper.events.TaskCrudEvents
import com.example.noteskeeper.utils.*
import com.example.noteskeeper.utils.logs.LoggerUtils
import com.example.noteskeeper.viewmodel.TaskViewModel
import java.util.*

private const val TAG = "AddNotes"

@Composable
fun AddNotes(modifier: Modifier = Modifier, viewModel: TaskViewModel, task: Task,saveClickEvent:(Int)->Unit) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedDateText by remember { mutableStateOf("Due Date: ${task.createdDateFormatted}") }
    var titleState by remember {
        mutableStateOf(task.name)
    }
    var descriptionState by remember {
        mutableStateOf(task.description)
    }

    var titleEmptyStateError by remember {
        mutableStateOf(false)
    }
    var descriptionEmptyStateError by remember {
        mutableStateOf(false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }
    val listItems = remember {
        arrayOf("Incomplete", "Complete")
    }

    val lambdaRemember = remember<(Boolean)->Unit> {
        {
            expanded=it
        }
    }

    var selectedItem by remember {
        mutableStateOf(
            if (task.name.isEmpty()) listItems[0] else {
                if (!task.completed) listItems[0] else listItems[1]
            }
        )
    }
    val lambdaRemember2 = remember<(String)->Unit> {
        {
            selectedItem=it
        }
    }

    var dayOfWeek by remember {
        mutableStateOf(0)
    }
    var monthOfYear by remember {
        mutableStateOf(0)
    }
    var yearOfCentury by remember {
        mutableStateOf(0)
    }

// Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]


    val datePicker = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                dayOfWeek=selectedDayOfMonth
                monthOfYear=selectedMonth
                yearOfCentury=selectedYear

                selectedDateText = "Due Date: $selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            }, year, month, dayOfMonth
        )
    }
    datePicker.datePicker.minDate = calendar.timeInMillis


    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        AdvanceEditText(
            modifier = modifier
                .padding(top = R.dimen._20sdp.dp())
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.White, shape = RoundedCornerShape(10.dp)),
            value = titleState,
            placeholderText = "Title",
            focusedColor = R.color.grey_line.color(),
            unFocusedColor = R.color.upi_text_grey.color(),
            onValueChange = {
                titleEmptyStateError = it.isEmpty()
                titleState = it

            },
            visualTransformation = VisualTransformation.None,
            isError = titleEmptyStateError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),

            )
        if (titleEmptyStateError) {
            Text(
                text = "Please enter title",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        AdvanceEditText(
            modifier = modifier
                .padding(top = R.dimen._20sdp.dp())
                .fillMaxWidth()
                .height(82.dp)
                .background(Color.White, shape = RoundedCornerShape(10.dp)),
            value = descriptionState,
            placeholderText = "Description",
            focusedColor = R.color.grey_line.color(),
            unFocusedColor = R.color.upi_text_grey.color(),
            onValueChange = {
                descriptionEmptyStateError = it.isEmpty()

                descriptionState = it

            },
            visualTransformation = VisualTransformation.None,
            isError = descriptionEmptyStateError,

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            singleLine = false

        )
        if (descriptionEmptyStateError) {
            Text(
                text = "Please enter description",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }


        Box(
            modifier = modifier
                .padding(top = R.dimen._20sdp.dp())
                .height(50.dp)
                .fillMaxWidth()
                .clickable {
                    datePicker.show()
                }
                .border(
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, R.color.upi_text_grey.color())
                )
                .background(Color.White, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Textview(
                modifier = modifier.padding(start = 10.dp),
                text = selectedDateText.takeIf { it.isNotEmpty() } ?: "Due Date",
                color = Color.LightGray
            )
        }

        if(task.name.isNotEmpty()){
            Box {
                ShowSpinner(
                    listItems = listItems,
                    expanded = expanded,
                    stateExpanded = lambdaRemember,
                    selectedItemState = lambdaRemember2,
                    selectedItemTop = selectedItem

                )
            }
        }


        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = R.dimen._20sdp.dp(),
                    end = R.dimen._20sdp.dp(),
                    bottom = R.dimen._5sdp.dp(),
                    top = R.dimen._20sdp.dp()
                )
                .height(R.dimen._60sdp.dp()),

            onClick = {
                if (validateEntries(
                        titleState,
                        descriptionState,
                        selectedDateText,
                        titleState = { titleEmptyStateError = it },
                        desState = { descriptionEmptyStateError = it })
                ) {
                    if (task.name.isEmpty()) {
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfWeek)
                        calendar.set(Calendar.MONTH, monthOfYear)
                        calendar.set(Calendar.YEAR, yearOfCentury)


                        val dateInMillis = calendar.timeInMillis
                        val taskData = Task(
                            name = titleState,
                            description = descriptionState,
                            created = dateInMillis,
                            completed = selectedItem != "Incomplete"
                        )
                        viewModel.onCrudEvents(TaskCrudEvents.Add(task = taskData))
                        saveClickEvent.invoke(0)
                    } else {
                        val dateInMillis: Long =
                            if (task.createdDateFormatted != selectedDateText) {
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfWeek)
                                calendar.set(Calendar.MONTH, monthOfYear)
                                calendar.set(Calendar.YEAR, yearOfCentury)

                                LoggerUtils.logVerbose(TAG, "Day ${dayOfWeek}")
                                calendar.timeInMillis


                            } else task.created
                        val taskData = task.copy(
                            name = titleState,
                            description = descriptionState,
                            created = dateInMillis,
                            completed = selectedItem != "Incomplete"
                        )

                        viewModel.onCrudEvents(TaskCrudEvents.Update(task = taskData))
                        saveClickEvent.invoke(1)
                    }


                } else {
                    showToast(context, "Kindly provide all entries")
                }
            },

            shape = RoundedCornerShape(size = R.dimen._10sdp.dp()),
            colors = ButtonDefaults.buttonColors(R.color.color_ff7744.color())

        )
        {
            Textview(
                text = "Save",
                color = R.color.white.color(),
                fontSize = R.dimen._16ssp.sp(),
                fontWeight = FontWeight.SemiBold
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Stable
private fun ShowSpinner(
    modifier: Modifier = Modifier,
    listItems: Array<String>,
    expanded: Boolean,
    stateExpanded: (Boolean) -> Unit,
    selectedItemState: (String) -> Unit,
    selectedItemTop: String
) {

    var selectedItem by remember {
        mutableStateOf("Status: $selectedItemTop")
    }
    ExposedDropdownMenuBox(
        modifier = modifier.padding(top = 20.dp),
        expanded = expanded,
        onExpandedChange = { stateExpanded(!expanded) }) {

        Box(
            modifier = modifier
                .height(50.dp)
                .fillMaxWidth()
                .clickable {

                }
                .border(
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, R.color.upi_text_grey.color())
                )
                .background(Color.White, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Textview(
                modifier = modifier.padding(start = 10.dp),
                text = selectedItem,
                color = Color.LightGray
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { stateExpanded(false) }
        ) {
            listItems.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    selectedItem = "Status: $selectedOption"
                    selectedItemState(selectedOption)
                    stateExpanded(false)
                }) {
                    Text(text = selectedOption)
                }
            }
        }
    }

}


private fun validateEntries(
    title: String,
    description: String,
    date: String,
    titleState: (Boolean) -> Unit,
    desState: (Boolean) -> Unit
): Boolean {

    return when {
        title.isEmpty() -> {
            titleState(true)
            false
        }
        description.isEmpty() -> {
            desState(true)
            false
        }
        else -> true

    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AddNotePrev() {
    //AddNotes(viewModel = TaskViewModel(TaskDao))
}

private fun someFeaturesInMaster(){
    LoggerUtils.logVerbose(TAG,"Some Features in master")
}
