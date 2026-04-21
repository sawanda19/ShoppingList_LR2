package com.example.shoppinglist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.model.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ShoppingViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<ShoppingItem>>(
        listOf(
            ShoppingItem(name = "Яйця", quantity = 10, isBought = true),
            ShoppingItem(name = "Масло вершкове", quantity = 1, isBought = true),
            ShoppingItem(name = "Молоко 1л", quantity = 1),
            ShoppingItem(name = "Хліб чорний", quantity = 2),
            ShoppingItem(name = "Яблука", quantity = 1),
            ShoppingItem(name = "Сир твердий", quantity = 1),
        )
    )
    val items: StateFlow<List<ShoppingItem>> = _items

    val remainingCount: StateFlow<Int> = _items
        .map { list -> list.count { !it.isBought } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    fun addItem(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return
        _items.value = _items.value + ShoppingItem(name = trimmed)
    }

    fun toggleItem(id: String) {
        _items.value = _items.value.map { item ->
            if (item.id == id) item.copy(isBought = !item.isBought) else item
        }
    }

    fun removeItem(id: String) {
        _items.value = _items.value.filter { it.id != id }
    }
}