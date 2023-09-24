package com.example.noteskeeper.views

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.example.noteskeeper.utils.Textview
import com.example.noteskeeper.R


@Composable
fun NoResultView(
    modifier: Modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen._10sdp)),
    resourceId : Int = 101,
    message: String = "",
) {
    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen._16sdp))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "No Result Resource",
            modifier = Modifier
                .height(dimensionResource(id = R.dimen._100sdp))
                .width(dimensionResource(id = R.dimen._100sdp))
        )
        Textview(
            message,
            fontSize = 18.sp,
            color = colorResource(id = R.color.sa_input_hint_color),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}
