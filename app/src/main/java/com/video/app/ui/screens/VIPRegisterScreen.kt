package com.video.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.video.app.R
import com.video.app.config.CONSTANT
import com.video.app.navController
import com.video.app.ui.screens.components.BtnText
import com.video.app.ui.screens.components.Heading
import com.video.app.states.objects.UiState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.AppColor

class VIPRegisterScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(userViewModel: UserViewModel) {
        var (monthSelected, setMonthSelected) = remember {
            mutableIntStateOf(0)
        }
        val listMonthOption =
            listOf("Service for 1 month", "Service for 3 months", "Service for 6 months")
        val toMonthRequest: () -> Int = {
            when (monthSelected) {
                0 -> 1
                1 -> 3
                2 -> 6
                else -> -1
            }
        }
        var activeButton by remember {
            mutableStateOf(true)
        }
        val activeBtnHandle: () -> Unit = {
            activeButton = true
        }
        Scaffold(containerColor = AppColor.background, topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = AppColor.primary_text)
                }
            }
        }) {
            LazyColumn(modifier = Modifier.padding(10.dp, it.calculateTopPadding())) {
                item {
                    val logoModifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = logoModifier) {
                            Image(
                                painter = painterResource(id = R.drawable.video_logo),
                                contentDescription = null,
                                modifier = logoModifier
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Heading(text = "VIP Registration", size = CONSTANT.UI.TEXT_SIZE.XL)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    listMonthOption.forEachIndexed { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(
                                    color = if (index == monthSelected) {
                                        AppColor.background_container
                                    } else {
                                        Color.Black
                                    },
                                    shape = RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON)
                                )
                                .clickable {
                                    setMonthSelected(index)
                                }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item, modifier = Modifier,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = CONSTANT.UI.TEXT_SIZE.MD,
                                        color = AppColor.primary_text
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ElevatedButton(
                            onClick = {
                                userViewModel.registerVIP(toMonthRequest(), activeBtnHandle)
                            },
                            enabled = activeButton,
                            shape = CircleShape,
                            modifier = Modifier,
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = AppColor.background_container
                            )
                        ) {
                            Text(
                                text = "Register".uppercase(), style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = CONSTANT.UI.TEXT_SIZE.SM,
                                    color = AppColor.primary_content
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}