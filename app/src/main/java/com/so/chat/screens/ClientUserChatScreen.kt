package com.so.chat.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.so.chat.ChatTextSizeDataStore
import com.so.chat.RoundedCornerDataStore
import com.so.chat.components.ChatMessageBubble
import com.so.chat.model.message.ChatMessage
import com.so.chat.model.user.LocalUser
import com.so.chat.viewmodel.ClientUserChatViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClientUserChatScreen(
    navController: NavController,
    viewModel: ClientUserChatViewModel,
    chatId: String?,
    username: String?
) {

    val user = viewModel.user
    val users by viewModel.connectedUsers.collectAsState()

    val context = LocalContext.current

    val chatTextSizeDataStore = ChatTextSizeDataStore(context)
    val chatTextSize = chatTextSizeDataStore.getTextSize.collectAsState(9f)

    val roundedCornerDataStore = RoundedCornerDataStore(context)
    val roundedCornerValue = roundedCornerDataStore.getDp.collectAsState(0f)

    var message by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val userChat = viewModel.getUserMessages(chatId!!).collectAsState(emptyList()).value

    if (user != null){
        Scaffold (
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(username!!)
                    },
                    actions = {
                        AnimatedVisibility(
                            LocalUser(uuid = UUID.fromString(chatId!!), username = username!!) in users
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .padding(end = 16.dp)
                                    .background(Color.Green, shape = CircleShape)
                            )
                        }
                    },
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(15.dp, 15.dp, 0. dp, 0.dp))
                        .border(border = BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                        .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    TextField(
                        value = message,
                        onValueChange = {
                            message = it
                        },
                        maxLines = 3,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.padding(3.dp)
                            .weight(1f)
                    )
                    IconButton(
                        onClick = {
                            val chatMessage = ChatMessage(
                                chatId = chatId!!,
                                senderId = user.uuid.toString(),
                                receiverId = chatId,
                                timestamp = System.currentTimeMillis(),
                                content = message,
                                messageId = UUID.randomUUID().toString(),
                                contactName = username!!,
                                username = user.username
                            )
                            scope.launch {
                                viewModel.sendMessage(chatMessage)
                                message = ""
                            }
                        },
                        enabled = LocalUser(uuid = UUID.fromString(chatId!!), username = username!!) in users && message.isNotBlank()
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Default.Send, null)
                    }
                }
            }
        ){ padding ->
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = padding
            ) {
                items(items = userChat) {
                    ChatMessageBubble(
                        text = it.content!!,
                        fontSize = chatTextSize.value!!,
                        cornerRadius = roundedCornerValue.value!!,
                        sentMessage = it.senderId == user.uuid.toString()
                    )
                }
            }
        }
    }
}