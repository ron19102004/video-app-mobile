package com.video.app.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.video.app.navController
import com.video.app.states.objects.UiState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.AppColor

class SearchScreen {
    private lateinit var userViewModel: UserViewModel;

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Screen(userViewModel: UserViewModel) {
        this.userViewModel = userViewModel

        var query by rememberSaveable {
            mutableStateOf("")
        }
        SearchBar(
            colors = SearchBarDefaults.colors(
                containerColor = AppColor.background,
                inputFieldColors = TextFieldDefaults.colors(
                    focusedTextColor = AppColor.primary_text,
                    unfocusedTextColor = AppColor.primary_text
                )
            ),
            query = query,
            onQueryChange = { query = it },
            onSearch = {},
            active = true,
            onActiveChange = {},
            placeholder = { Text(text = "Search...", color = AppColor.second_text) },
            trailingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = null,
                        tint = AppColor.primary_text
                    )
                }
            },
            leadingIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = AppColor.primary_text
                    )
                }
            }
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    SearchScreen().Screen(userViewModel = viewModel())
}