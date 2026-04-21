package com.example.shoppinglist.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppinglist.viewmodel.ShoppingViewModel

private val OrangeAccent  = Color(0xFFF5A623)
private val BgPage        = Color(0xFFF5F5F5)
private val TextPrimary   = Color(0xFF2D2D2D)
private val TextSecondary = Color(0xFF9E9E9E)
private val SectionLabel  = Color(0xFF9E9E9E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(viewModel: ShoppingViewModel = viewModel()) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    val remaining by viewModel.remainingCount.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    val notBought = items.filter { !it.isBought }
    val bought    = items.filter { it.isBought }

    Column(
        modifier = Modifier.fillMaxSize().background(BgPage).systemBarsPadding()
    ) {
        // Заголовок
        Column(
            modifier = Modifier.background(Color.White).fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🛒", fontSize = 26.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Список покупок",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(text = "Залишилось купити: ", style = TextStyle(fontSize = 14.sp, color = TextSecondary))
                Text(text = "$remaining", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OrangeAccent))
            }
        }

        // Форма додавання
        Row(
            modifier = Modifier.background(Color.White).fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Назва товару...", color = TextSecondary) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F0F0),
                    unfocusedContainerColor = Color(0xFFF0F0F0),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                textStyle = TextStyle(fontSize = 16.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    if (inputText.isNotBlank()) {
                        viewModel.addItem(inputText)
                        inputText = ""
                        keyboard?.hide()
                    }
                })
            )
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { viewModel.addItem(inputText); inputText = ""; keyboard?.hide() },
                enabled = inputText.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeAccent,
                    disabledContainerColor = Color(0xFFDDDDDD)
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text(text = "Додати", style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Список
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (notBought.isNotEmpty()) {
                item(key = "header_not_bought") { SectionHeader(text = "ПОТРІБНО КУПИТИ") }
                items(items = notBought, key = { it.id }) { item ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(200)) + slideInVertically(tween(200)),
                        exit = fadeOut(tween(200))
                    ) {
                        ShoppingItemCard(item = item, onToggle = { viewModel.toggleItem(item.id) })
                    }
                }
            }
            if (bought.isNotEmpty()) {
                item(key = "header_bought") {
                    Spacer(modifier = Modifier.height(4.dp))
                    SectionHeader(text = "КУПЛЕНО")
                }
                items(items = bought, key = { it.id }) { item ->
                    ShoppingItemCard(item = item, onToggle = { viewModel.toggleItem(item.id) })
                }
            }
            if (items.isEmpty()) {
                item(key = "empty") {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Список порожній 🛍️\nДодайте перший товар!",
                            style = TextStyle(fontSize = 16.sp, color = TextSecondary, lineHeight = 24.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
            color = SectionLabel
        ),
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
    )
}