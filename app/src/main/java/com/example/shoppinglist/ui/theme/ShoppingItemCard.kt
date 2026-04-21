package com.example.shoppinglist.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.model.ShoppingItem

private val GreenCheck  = Color(0xFF4CAF50)
private val CardBg      = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF2D2D2D)
private val TextBought  = Color(0xFFB0B0B0)
private val QuantityBg  = Color(0xFFF0F0F0)

@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (item.isBought) Color(0xFFF7FFF7) else CardBg,
        animationSpec = tween(300), label = "cardBg"
    )
    val circleColor by animateColorAsState(
        targetValue = if (item.isBought) GreenCheck else Color(0xFFDDDDDD),
        animationSpec = tween(300), label = "circleColor"
    )

    Card(
        modifier = modifier.fillMaxWidth().clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(28.dp).clip(CircleShape).background(circleColor),
                contentAlignment = Alignment.Center
            ) {
                if (item.isBought) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Куплено",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = item.name,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = if (item.isBought) TextBought else TextPrimary,
                    textDecoration = if (item.isBought) TextDecoration.LineThrough else TextDecoration.None
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(QuantityBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "×${item.quantity}",
                    style = TextStyle(fontSize = 13.sp, color = TextBought)
                )
            }
        }
    }
}