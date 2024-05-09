package com.video.app.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.video.app.Navigate
import com.video.app.R
import com.video.app.api.models.VIP
import com.video.app.config.CONSTANT
import com.video.app.ui.screens.Router
import com.video.app.states.objects.NavigationState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.AppColor

@Composable
fun NavigationBarBottom(userViewModel: UserViewModel) {
    val vip = userViewModel.vip.asFlow().collectAsState(initial = VIP())
    val listItem: List<String> = listOf("Profile", "Home", "Settings")
    val listIntPainter: List<Int> = listOf(
        R.drawable.account_icon,
        if (vip.value?.active == true) R.drawable.home_icon_vip else R.drawable.home_icon,
        R.drawable.setting_icon
    )
    val listRouter: List<Router> =
        listOf(Router.MyProfileScreen, Router.HomeScreen, Router.SettingScreen)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Divider(color = AppColor.background_container)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 5.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listItem.forEachIndexed { index, label ->
                Column(
                    modifier = Modifier
                        .width(60.dp)
                        .clip(RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON))
                        .clickable {
                            if (listRouter[index].id == Router.MyProfileScreen.id && !userViewModel.isLoggedIn) {
                                Navigate(Router.LoginScreen.route)
                            } else {
                                NavigationState.navSelected = listRouter[index].id
                                Navigate(listRouter[index])
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier.size(30.dp)
                    ) {
                        Image(
                            painter = painterResource(id = listIntPainter[index]),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Text(
                        text = label, style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = CONSTANT.UI.TEXT_SIZE.SM_,
                            color = if (listRouter[index].id == NavigationState.navSelected) {
                                AppColor.primary_content
                            } else {
                                Color.DarkGray
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavBarBottomPreview() {
    NavigationBarBottom(userViewModel = viewModel())
}