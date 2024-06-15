package com.video.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.video.app.R
import com.video.app.config.CONSTANT
import com.video.app.states.objects.AppInitializerState
import com.video.app.states.viewmodels.UserViewModel
import com.video.app.ui.screens.components.BtnText
import com.video.app.ui.screens.components.Heading
import com.video.app.ui.theme.AppColor
import kotlinx.coroutines.launch

class UpdateAvatarScreen {
    @Composable
    fun Screen(userViewModel: UserViewModel= AppInitializerState.userViewModel) {
        var imageSelected by remember {
            mutableStateOf<Uri?>(null)
        }
        val singlePhotoPickerLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
                imageSelected = it
            }
        var enabledChooseBtn by remember {
            mutableStateOf(true)
        }
        var enabledSubmitBtn by remember {
            mutableStateOf(true)
        }
        val scope = rememberCoroutineScope()
        Scaffold(
            containerColor = AppColor.background
        ) {
            Column(modifier = Modifier.padding(it)) {
                val painterErr = painterResource(id = R.drawable.user_icon1)
                val avatarCurrent = userViewModel.userCurrent?.value?.imageURL
                Box(modifier = Modifier) {
                    val bgAvatar = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .blur(15.dp)
                    Box(modifier = bgAvatar) {
                        AsyncImage(
                            model = imageSelected?:avatarCurrent,
                            contentDescription = null,
                            modifier = bgAvatar,
                            contentScale = ContentScale.Crop,
                            placeholder = painterErr,
                            error = painterErr
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val avatarModifier = Modifier
                            .size(300.dp)
                            .clip(CircleShape)
                            .border(1.dp, AppColor.primary_content, CircleShape)
                        Box(modifier = avatarModifier) {
                            AsyncImage(
                                model = imageSelected?:avatarCurrent,
                                contentDescription = null,
                                modifier = avatarModifier,
                                contentScale = ContentScale.Crop,
                                placeholder = painterErr,
                                error = painterErr
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp, 0.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    BtnText(enabled = enabledChooseBtn, onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }, text = "Choose photo")
                    if (imageSelected != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        BtnText(
                            enabled = enabledSubmitBtn,
                            onClick = {
                                scope.launch {
                                    enabledChooseBtn = false
                                    enabledSubmitBtn = false
                                    userViewModel.updateAvatar(imageSelected!!) {
                                        enabledChooseBtn = true
                                        enabledSubmitBtn = true
                                    }
                                }
                            }, text = "Update",
                            buttonColor = AppColor.primary_content
                        )
                    }
                }
            }
        }
    }
}