package com.example.todo_eisenhower_matrix.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.todo_eisenhower_matrix.ui.theme.Carbon

@Composable
fun CarbonTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    maxLength: Int = 30
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = Carbon.typography.label01,
            color = Carbon.colors.textSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = value,
            onValueChange = { newValue ->
                val singleLineValue = newValue.replace("\n", "")
                if (singleLineValue.length <= maxLength) {
                    onValueChange(singleLineValue)
                } else {
                    onValueChange(singleLineValue.take(maxLength))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = Carbon.typography.bodyShort02,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Carbon.colors.layer,
                unfocusedContainerColor = Carbon.colors.layer,
                focusedIndicatorColor = Carbon.colors.focus,
                unfocusedIndicatorColor = Carbon.colors.textSecondary.copy(alpha = 0.3f),
                focusedTextColor = Carbon.colors.textPrimary,
                unfocusedTextColor = Carbon.colors.textPrimary,
                focusedLabelColor = Carbon.colors.textSecondary,
                unfocusedLabelColor = Carbon.colors.textSecondary
            ),
            shape = RectangleShape
        )
    }
}

@Composable
fun CarbonToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = Carbon.typography.bodyShort02,
            color = Carbon.colors.textPrimary
        )
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(24.dp)
                .background(
                    color = if (checked) Carbon.colors.supportSuccess else Carbon.colors.layer,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { onCheckedChange(!checked) }
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                    .background(Color.White, CircleShape)
            )
        }
    }
}

@Composable
fun CarbonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Carbon.colors.buttonPrimary,
    contentColor: Color = Carbon.colors.textOnColor,
    icon: ImageVector? = null
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RectangleShape,
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = Carbon.typography.bodyShort01
            )
            if (icon != null) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CarbonCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .border(
                width = 1.dp,
                color = if (checked) Carbon.colors.buttonPrimary else Carbon.colors.textPrimary,
                shape = RectangleShape
            )
            .background(
                color = if (checked) Carbon.colors.buttonPrimary else Color.Transparent,
                shape = RectangleShape
            )
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Checkbox
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Carbon.colors.textOnColor,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun CarbonTag(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(24.dp),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = Carbon.typography.label01
            )
        }
    }
}

@Composable
fun CarbonHeader(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val borderColor = Color(0xFF6F6F6F)

    Surface(
        modifier = modifier.fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = borderColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            },
        color = Carbon.colors.uiShell,
        contentColor = Carbon.colors.onUiShell
    ) {
        Column(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = title,
                    style = Carbon.typography.heading01,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )

                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
            }
        }
    }
}