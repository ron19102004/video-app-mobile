package com.video.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.video.app.Navigate
import com.video.app.screens.layouts.MainLayout
import com.video.app.states.viewmodels.UserViewModel

class HomeScreen {
    private lateinit var userViewModel: UserViewModel;

    @Composable
    fun Screen(userViewModel: UserViewModel) {
        this.userViewModel = userViewModel;
        MainLayout(userViewModel=userViewModel)  {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(shape = CircleShape)
                    .clickable {
                        Navigate(Router.SearchScreen)
                    },
                shape = CircleShape,
                placeholder = { Text(text = "Search...") },
                trailingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                enabled = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen().Screen(userViewModel = viewModel())
}