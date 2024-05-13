package com.video.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.video.app.Navigate
import com.video.app.R
import com.video.app.api.models.CreatePlaylistDto
import com.video.app.api.models.PlaylistModel
import com.video.app.api.models.Privacy
import com.video.app.api.models.UserModel
import com.video.app.api.models.VIP
import com.video.app.config.CONSTANT
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.screens.layouts.MainLayout
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.states.viewmodels.VideoAndPlaylistViewModel
import com.video.app.ui.screens.components.BtnImgText
import com.video.app.ui.screens.components.BtnText
import com.video.app.ui.screens.components.Input
import com.video.app.ui.screens.components.PullToRefreshLazyColumn
import com.video.app.ui.theme.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MyProfileScreen {
    private lateinit var userViewModel: UserViewModel
    private lateinit var videoAndPlaylistViewModel: VideoAndPlaylistViewModel
    private lateinit var userCurrent: State<UserModel?>;
    private lateinit var vip: State<VIP?>
    private var openCancelVipAccount = mutableStateOf(false)
    private var urVipAccountOpen = mutableStateOf(false)
    private var openAddPlaylist = mutableStateOf(false)
    private var openOptionOnLongLickPlaylist = mutableStateOf(false)
    private var idPlaylistSelectedToDelete = mutableLongStateOf(0L);

    @Composable
    fun Screen(
        userViewModel: UserViewModel,
        videoAndPlaylistViewModel: VideoAndPlaylistViewModel
    ) {
        this.userViewModel = userViewModel
        this.videoAndPlaylistViewModel = videoAndPlaylistViewModel
        userCurrent = userViewModel.userCurrent.asFlow().collectAsState(initial = null)
        vip = userViewModel.vip.asFlow().collectAsState(initial = null)
        if (userViewModel.isLoggedIn) {
            if (userCurrent != null) {
                Loaded()
                ModalBottomSheets()
            } else LoadError()
        }
    }

    @Composable
    private fun Loaded() {
        var isRefreshing by remember {
            mutableStateOf(false)
        }
        val refresh: () -> Unit = {
            userViewModel.loadUserFormToken { isRefreshing = false }
            videoAndPlaylistViewModel.loadMyPlaylist()
        }
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
            HorizontalDivider(color = AppColor.background_container)
            Spacer(modifier = Modifier.height(10.dp))
            PullToRefreshLazyColumn<Any>(isRefreshing = isRefreshing,
                modifier = Modifier
                    .padding(10.dp, 0.dp)
                    .fillMaxWidth(),
                onRefresh = {
                    userViewModel.viewModelScope.launch {
                        isRefreshing = true
                        delay(1000L)
                        refresh()
                    }
                },
                contentFix = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val painterImage = painterResource(id = R.drawable.account_icon)
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
                                    val confirmedIconModifier = Modifier.size(20.dp)
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
                                    fontStyle = FontStyle.Italic, color = AppColor.second_text
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    TagSettingContainer()
                    Spacer(modifier = Modifier.height(10.dp))
                    PlaylistContainer()
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = AppColor.background_container)
                    Spacer(modifier = Modifier.height(10.dp))
                    OptionsAccount()
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = AppColor.background_container)
                })
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ModalBottomSheets() {
        val cancelVipAccountState = rememberModalBottomSheetState()
        val addPlaylistState = rememberModalBottomSheetState()
        val optionOnLongClickPlaylist = rememberModalBottomSheetState()
        if (openCancelVipAccount.value) {
            ModalBottomSheet(
                onDismissRequest = { openCancelVipAccount.value = false },
                sheetState = cancelVipAccountState,
                containerColor = AppColor.background,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Heading(text = "Are you sure for Cancel VIP?", size = CONSTANT.UI.TEXT_SIZE.LG)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BtnText(
                            onClick = { openCancelVipAccount.value = false },
                            text = "Not",
                            modifier = Modifier.width(100.dp),
                            height = 40.dp,
                            buttonColor = Color.Red
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        BtnText(
                            onClick = {
                                openCancelVipAccount.value = false
                                userViewModel.cancelVIP()
                                urVipAccountOpen.value = false
                            },
                            text = "Sure",
                            modifier = Modifier.width(100.dp),
                            height = 40.dp,
                            buttonColor = Color.Blue
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        if (openAddPlaylist.value) {
            var name by remember {
                mutableStateOf("")
            }
            var isPublic by remember {
                mutableStateOf(false)
            }
            ModalBottomSheet(
                onDismissRequest = { openAddPlaylist.value = false },
                sheetState = addPlaylistState,
                containerColor = AppColor.background,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Input(
                        value = name, onValueChange = {
                            name = it
                        }, label = "Playlist's name", placeholder = "Healing"
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Heading(
                            text = "Public",
                            size = CONSTANT.UI.TEXT_SIZE.SM,
                            fontWeight = FontWeight.Normal
                        )
                        Checkbox(
                            checked = isPublic,
                            onCheckedChange = {
                                isPublic = it
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AppColor.primary_content
                            )
                        )
                    }
                    BtnText(onClick = {
                        if (name.isBlank()) {
                            Toast.makeText(
                                userViewModel.context,
                                "Name must not blank",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            videoAndPlaylistViewModel.addPlaylist(
                                CreatePlaylistDto(
                                    name,
                                    isPublic
                                )
                            ) {
                                openAddPlaylist.value = false
                            }
                        }
                    }, text = "Add")
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        if (openOptionOnLongLickPlaylist.value) {
            ModalBottomSheet(
                onDismissRequest = { openOptionOnLongLickPlaylist.value = false },
                sheetState = optionOnLongClickPlaylist,
                containerColor = AppColor.background
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Heading(text = "Playlist Options", size = CONSTANT.UI.TEXT_SIZE.MD)
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        item {
                            BtnImgText(
                                onClick = {
                                    videoAndPlaylistViewModel.deletePlaylist(
                                        idPlaylistSelectedToDelete.value
                                    ) {
                                        openOptionOnLongLickPlaylist.value = false
                                    }
                                },
                                text = "Delete",
                                painter = painterResource(
                                    id = R.drawable.add
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
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
                    },
                    text = "TFA ${if (userViewModel.tfa) "on" else "off"}",
                    painter = painterResource(id = R.drawable.security),
                    enabled = activeTFABtn
                )
                Spacer(modifier = Modifier.width(5.dp))
                TagSetting(
                    onClick = {

                    },
                    text = "Confirm setting",
                    painter = painterResource(id = R.drawable.confirmed),
                    enabled = true
                )
                Spacer(modifier = Modifier.width(5.dp))
                TagSetting(
                    onClick = {
                        Navigate(Router.UpdateAvatarScreen)
                    },
                    text = "Change avatar",
                    painter = painterResource(id = R.drawable.user_icon1),
                    enabled = true
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
                    painter = painter, contentDescription = null, modifier = iconModifier
                )
            }
            Spacer(modifier = Modifier.width(1.dp))
            Text(text = text, color = AppColor.primary_text)
        }
    }

    @Composable
    private fun PlaylistContainer() {
        val playlists =
            videoAndPlaylistViewModel.myPlaylist.asFlow().collectAsState(initial = emptyList())
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Heading(text = "Playlists", size = CONSTANT.UI.TEXT_SIZE.MD)
                IconButton(onClick = { openAddPlaylist.value = true }) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        tint = AppColor.primary_text
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            LazyRow {
                playlists?.value?.size?.let { pls ->
                    items(pls) { it ->
                        val playlist = playlists?.value?.get(it)
                        if (playlist != null) {
                            PlaylistCard(
                                index = it,
                                playlistModel = playlist,
                                onClick = { playlistId, playlistIndex ->
                                    Navigate(
                                        Router.PlaylistVideoScreen.setArgs(
                                            playlistId,
                                            playlistIndex
                                        )
                                    )
                                }, onLongClick = {
                                    idPlaylistSelectedToDelete.value = it
                                    openOptionOnLongLickPlaylist.value = true
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun PlaylistCard(
        index: Int,
        playlistModel: PlaylistModel,
        onClick: (playlistId: Long, playlistIndex: Int) -> Unit,
        onLongClick: (playlistId: Long) -> Unit = {}
    ) {
        val painterImage = painterResource(id = R.drawable.video_bg)
        Column(modifier = Modifier
            .width(150.dp)
            .combinedClickable(
                onClick = { onClick(playlistModel?.id ?: 0, index) },
                onLongClick = { onLongClick(playlistModel?.id ?: 0) }
            )) {
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
                    text = playlistModel?.name.toString(), style = TextStyle(
                        fontWeight = FontWeight.SemiBold, color = AppColor.primary_text
                    ), maxLines = 2, overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (playlistModel.privacy == Privacy.PRIVATE) "Private" else "Public",
                    style = TextStyle(
                        fontSize = 10.sp, color = AppColor.second_text
                    )
                )
            }
        }
    }

    @Composable
    private fun OptionsAccount() {
        Column {
            OptionAccountCard(painter = painterResource(id = R.drawable.movie),
                "Your movies",
                onClick = { Navigate(Router.MyVideoScreen) })
            OptionAccountCard(
                painter = painterResource(id = R.drawable.vip), "Your VIP account", onClick = {
                    if (vip != null && vip.value?.active == true) {
                        urVipAccountOpen.value = !urVipAccountOpen.value
                    } else Navigate(Router.VIPRegisterScreen)
                }, hasMore = true, isMore = urVipAccountOpen.value
            )
            if (urVipAccountOpen.value && vip != null) {
                Column(modifier = Modifier.padding(30.dp, 0.dp, 0.dp, 0.dp)) {
                    OptionAccountCard(
                        text = "Cancel VIP account",
                        onClick = { openCancelVipAccount.value = true },
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
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
                .clickable { onClick() }, verticalAlignment = Alignment.CenterVertically
        ) {
            if (painter != null) {
                val iconModifier = Modifier.size(30.dp)
                Box(modifier = iconModifier) {
                    Image(
                        painter = painter, contentDescription = null, modifier = iconModifier
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = text, fontWeight = fontWeight, color = AppColor.primary_text)
            if (hasMore) {
                if (isMore) Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = AppColor.primary_text
                )
                else Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AppColor.primary_text
                )
            }
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