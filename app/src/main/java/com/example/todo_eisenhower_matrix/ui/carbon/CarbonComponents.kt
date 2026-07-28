package com.example.todo_eisenhower_matrix.ui.carbon

import com.example.todo_eisenhower_matrix.ui.theme.Carbon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun CarbonTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
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
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = Carbon.typography.bodyShort02,
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
                    color = if (checked) Carbon.colors.supportSuccess else Carbon.colors.textSecondary,
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
        shape = RoundedCornerShape(12.dp), // Carbon tags can have rounded ends or be rectangular
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
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        color = Carbon.colors.uiShell,
        contentColor = Carbon.colors.onUiShell
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = Carbon.typography.heading02,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            actions()
        }
    }
}
