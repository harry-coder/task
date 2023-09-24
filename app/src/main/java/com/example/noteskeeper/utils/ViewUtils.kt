package com.example.noteskeeper.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteskeeper.R

@Composable
fun Int.sp() = dimensionResource(id = this).value.sp

@Composable
fun Int.color() = colorResource(id = this)

@Composable
fun Int.getString(): String = stringResource(this)

@Composable
fun Int.dp() = dimensionResource(id = this)


@Composable
fun ImageView(
    drawableId: Int,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale
) {
    Image(
        painter = painterResource(id = drawableId), contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
    )
}

@Composable
fun Textview(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = R.dimen._14sdp.sp(),
    maxLines: Int = Int.MAX_VALUE,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        color = color,
        overflow = overflow,
        textAlign = textAlign,
        fontWeight = fontWeight,
        maxLines = maxLines,

        )


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AdvanceEditText(

    value: String,
    modifier: Modifier = Modifier,
    placeholderText: String,
    focusedColor: Color,
    unFocusedColor: Color,
    onValueChange: (value: String) -> Unit,
    visualTransformation: VisualTransformation,
    isError: Boolean,
    keyboardOptions: KeyboardOptions,
    drawableId: Int=0,
    readOnly:Boolean=false,
    singleLine:Boolean=true

//borderColor: String
) {

    val source = remember {
        MutableInteractionSource()
    }
    BasicTextField(
        modifier = modifier
            .height(42.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp)),
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray
        ),
        readOnly = readOnly,
        singleLine=singleLine,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = {
                    if (value.isEmpty() && placeholderText.isNotEmpty()) {
                        Text(
                            text = placeholderText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.LightGray,
                        )
                    }
                },
                // label = label,
                // leadingIcon = leadingIcon,
                trailingIcon = {
                    if (drawableId != 0) ImageView(
                        drawableId = drawableId,
                        contentDescription = "",
                        modifier = Modifier,
                        contentScale = ContentScale.Crop
                    )
                },
                contentPadding = PaddingValues(start = 10.dp, top = 10.dp),
                singleLine = singleLine,
                enabled = true,
                isError = isError,
                interactionSource = source,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = focusedColor,
                    unfocusedBorderColor = unFocusedColor

                ),
                border = {
                    TextFieldDefaults.BorderBox(
                        true,
                        isError,
                        source,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = focusedColor,
                            unfocusedBorderColor = unFocusedColor
                        ),
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                }
            )
        }
    )
}
fun showToast(context: Context?, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
 fun getColorForPriority(item:String)=
    when(item){
        "High"->Color.Red
        "Medium"->Color.Yellow
        "Low"->Color.Green
        else->Color.Blue
    }

