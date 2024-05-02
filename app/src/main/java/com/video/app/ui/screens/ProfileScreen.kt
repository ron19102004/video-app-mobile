package com.video.app.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import coil.compose.AsyncImage
import com.video.app.Navigate
import com.video.app.R
import com.video.app.api.models.PlaylistModel
import com.video.app.api.models.Privacy
import com.video.app.api.models.UserModel
import com.video.app.api.models.VIP
import com.video.app.config.CONSTANT
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.layouts.MainLayout
import com.video.app.states.objects.UiState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.theme.AppColor

class ProfileScreen {
    private lateinit var userViewModel: UserViewModel
    private lateinit var userCurrent: State<UserModel?>;
    private lateinit var vip: State<VIP?>

    @Composable
    fun Screen(userViewModel: UserViewModel) {
        this.userViewModel = userViewModel
        userCurrent = userViewModel.userCurrent.asFlow().collectAsState(initial = null)
        vip = userViewModel.vip.asFlow().collectAsState(initial = null)
        if (userViewModel.isLoggedIn) {
            if (userCurrent != null)
                Loaded()
            else
                LoadError()
        }
    }

    @Composable
    private fun Loaded() {
        MainLayout(userViewModel = userViewModel) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painterModifier = Modifier.size(30.dp)
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = painterModifier) {
                        Image(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = painterModifier
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Heading(text = "Profile", size = CONSTANT.UI.TEXT_SIZE.XL)
                    if (vip != null && vip.value?.active == true) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(modifier = painterModifier) {
                            Image(
                                painter = painterResource(id = R.drawable.vip_),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = painterModifier
                            )
                        }
                    }

                }
                IconButton(onClick = { userViewModel.logout() }) {
                    Box(modifier = painterModifier) {
                        Image(
                            painter = painterResource(id = R.drawable.logout_icon),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = painterModifier
                        )
                    }
                }
            }
            Divider(color = AppColor.background_container)
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(modifier = Modifier.padding(10.dp, 0.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val painterImage = painterResource(id = R.drawable.user_icon)
                        val imgModifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                        Box(modifier = imgModifier) {
                            AsyncImage(
                                model = userCurrent?.value?.imageURL,
                                contentDescription = null,
                                placeholder = painterImage,
                                error = painterImage,
                                contentScale = ContentScale.Crop,
                                modifier = imgModifier
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Heading(
                                    text = userCurrent?.value?.fullName.toString(),
                                    size = CONSTANT.UI.TEXT_SIZE.MD
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                if (userCurrent?.value?.confirmed == true) {
                                    val confirmedIconModifier = Modifier
                                        .size(20.dp)
                                    Box(modifier = confirmedIconModifier) {
                                        Image(
                                            painter = painterResource(id = R.drawable.confirmed),
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "@${userCurrent?.value?.username.toString()}",
                                style = TextStyle(
                                    fontStyle = FontStyle.Italic,
                                    color = AppColor.second_text
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    TagSettingContainer()
                    Spacer(modifier = Modifier.height(10.dp))
                    PlaylistView()
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = AppColor.background_container)
                    Spacer(modifier = Modifier.height(10.dp))
                    OptionsAccount()
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = AppColor.background_container)
                }
            }
        }
    }

    @Composable
    private fun TagSettingContainer() {
        var (activeTFABtn, setActiveTFABtn) = remember {
            mutableStateOf(true)
        }
        LazyRow(verticalAlignment = Alignment.CenterVertically) {
            item {
                TagSetting(
                    onClick = {
                        val activeFn: () -> Unit = {
                            setActiveTFABtn(true)
                        }
                        userViewModel.changeTFA(!userViewModel.tfa, activeFn)
                    }, text = "TFA ${if (userViewModel.tfa) "on" else "off"}",
                    painter = painterResource(id = R.drawable.security),
                    enabled = activeTFABtn
                )
                Spacer(modifier = Modifier.width(5.dp))
                TagSetting(
                    onClick = {

                    }, text = "Confirm setting",
                    painter = painterResource(id = R.drawable.confirmed),
                    enabled = true
                )
            }
        }
    }

    @Composable
    private fun PlaylistView() {
        Column {
            Heading(text = "Playlists", size = CONSTANT.UI.TEXT_SIZE.MD)
            Spacer(modifier = Modifier.height(5.dp))
            LazyRow {
                item {
                    PlaylistCard(PlaylistModel(0, "Watch later"), onClick = {})
                }
            }
        }
    }

    @Composable
    private fun PlaylistCard(playlistModel: PlaylistModel, onClick: (Long) -> Unit) {
        val painterImage = painterResource(id = R.drawable.video_bg)
        Column(modifier = Modifier
            .width(200.dp)
            .clickable { onClick(playlistModel?.id ?: 0) }) {
            val imgModifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(CONSTANT.UI.ROUNDED_INPUT_BUTTON))
            Box(modifier = imgModifier) {
                AsyncImage(
                    model = playlistModel.image,
                    contentDescription = null,
                    placeholder = painterImage,
                    error = painterImage,
                    modifier = imgModifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(10.dp, 5.dp)) {
                Text(
                    text = playlistModel?.name.toString(),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = AppColor.primary_text
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (playlistModel.privacy == Privacy.PRIVATE) "Private" else "Public",
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = AppColor.second_text
                    )
                )
            }
        }
    }

    @Composable
    private fun OptionsAccount() {
        val (urVipAccountOpen, setUrVipAccountOpen) = remember {
            mutableStateOf(false)
        }
        Column {
            OptionAccountCard(
                painter = painterResource(id = R.drawable.movie),
                "Your movies",
                onClick = {})
            OptionAccountCard(
                painter = painterResource(id = R.drawable.vip),
                "Your VIP account",
                onClick = {
                    if (vip != null && vip.value?.active == true) {
                        setUrVipAccountOpen(!urVipAccountOpen)
                    } else
                        Navigate(Router.VIPRegisterScreen)
                },
                hasMore = true,
                isMore = urVipAccountOpen
            )
            if (urVipAccountOpen) {
                YourVipAccountOption()
            }
        }
    }

    @Composable
    private fun YourVipAccountOption() {
        Column(modifier = Modifier.padding(30.dp, 0.dp, 0.dp, 0.dp)) {
            OptionAccountCard(
                text = "Cancel VIP account",
                onClick = { /*TODO*/ },
                fontWeight = FontWeight.Medium,
            )
        }
    }

    @Composable
    private fun OptionAccountCard(
        painter: Painter? = null,
        text: String,
        onClick: () -> Unit,
        padding: Dp = 0.dp,
        fontWeight: FontWeight = FontWeight.SemiBold,
        hasMore: Boolean = false,
        isMore: Boolean = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(padding)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (painter != null) {
                val iconModifier = Modifier.size(30.dp)
                Box(modifier = iconModifier) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = iconModifier
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = text, fontWeight = fontWeight, color = AppColor.primary_text)
            if (hasMore) {
                if (isMore)
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null,
                        tint = AppColor.primary_text
                    )
                else
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = AppColor.primary_text
                    )
            }
        }
    }

    @Composable
    private fun TagSetting(text: String, onClick: () -> Unit, painter: Painter, enabled: Boolean) {
        val iconModifier = Modifier.size(20.dp)
        TextButton(
            enabled = enabled,
            onClick = onClick,
            modifier = Modifier
                .clip(CircleShape)
                .height(35.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = AppColor.background_container
            )
        ) {
            Box(modifier = iconModifier) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = iconModifier
                )
            }
            Spacer(modifier = Modifier.width(1.dp))
            Text(text = text, color = AppColor.primary_text)
        }
    }

    @Composable
    private fun LoadError() {
        MainLayout(userViewModel = userViewModel) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Heading(text = "An error has occurred", size = CONSTANT.UI.TEXT_SIZE.MD)
                Spacer(modifier = Modifier.height(10.dp))
                ElevatedButton(onClick = { userViewModel.logout() }) {
                    Text(text = "Logout")
                }
            }
        }
    }
}