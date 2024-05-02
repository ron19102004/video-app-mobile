package com.video.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.video.app.Navigate
import com.video.app.config.CONSTANT
import com.video.app.ui.screens.layouts.MainLayout
import com.video.app.states.viewmodels.CategoryAndCountryViewModel
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.AppColor

class HomeScreen {
    private lateinit var categoryAndCountryViewModel: CategoryAndCountryViewModel
    private var btnTagSelected = mutableIntStateOf(0)

    @Composable
    fun Screen(
        userViewModel: UserViewModel,
        categoryAndCountryViewModel: CategoryAndCountryViewModel
    ) {
        this.categoryAndCountryViewModel = categoryAndCountryViewModel
        MainLayout(userViewModel = userViewModel) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(0.dp, 10.dp)
                    .clip(shape = CircleShape)
                    .clickable {
                        Navigate(Router.SearchScreen)
                    },
                shape = CircleShape,
                placeholder = { Text(text = "Search...", color = Color.DarkGray) },
                trailingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                enabled = false,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = AppColor.background,
                    disabledTrailingIconColor = Color.DarkGray,
                    disabledIndicatorColor = Color.DarkGray
                )
            )
            LazyColumn {
                item {
                    CategoriesTagBar()
                    Spacer(modifier = Modifier.height(10.dp))
                    VideoList()
                }
            }
        }
    }

    @Composable
    private fun CategoriesTagBar() {
        val categories =
            categoryAndCountryViewModel.categories.asFlow().collectAsState(initial = emptyList())
        if (categories != null) {
            LazyRow {
                categories.value?.let { list ->
                    items(list.size) { it ->
                        ElevatedButton(
                            onClick = {
                                btnTagSelected.value = it
                            },
                            modifier = Modifier,
                            shape = CircleShape,
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = if (btnTagSelected.value == it) AppColor.background_container
                                else Color.Transparent
                            )
                        ) {
                            Text(
                                text = "${categories.value!![it].name}", style = TextStyle(
                                    color = AppColor.primary_text,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            }
        }
    }

    @Composable
    private fun VideoList() {

    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen().Screen(userViewModel = viewModel(), categoryAndCountryViewModel = viewModel())
}