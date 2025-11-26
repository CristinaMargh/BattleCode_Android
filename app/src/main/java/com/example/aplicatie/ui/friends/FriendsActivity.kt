package com.example.aplicatie.ui.friends

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
// Am înlocuit și lăsat doar importul pentru AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicatie.data.User
import com.example.aplicatie.data.UserRepository
import com.example.aplicatie.ui.theme.AplicatieTheme

class FriendsActivity : ComponentActivity() {

    private val repo = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val currentUser = prefs.getString("username", "ANONIM") ?: "ANONIM"

        setContent {
            AplicatieTheme {
                FriendsScreen(
                    currentUsername = currentUser,
                    repo = repo,
                    onBack = { finish() },
                    onError = { msg ->
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

// Adăugăm adnotarea @OptIn pentru a folosi TopAppBar din Material3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FriendsScreen(
    currentUsername: String,
    repo: UserRepository,
    onBack: () -> Unit,
    onError: (String) -> Unit
) {
    var friendName by remember { mutableStateOf("") }
    var friends by remember { mutableStateOf<List<User>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var adding by remember { mutableStateOf(false) }

    val lilac = Color(0xFF98A5D6)

    // load friends once la intrare
    LaunchedEffect(currentUsername) {
        repo.getFriends(
            currentUsername,
            onResult = {
                friends = it
                loading = false
            },
            onError = {
                onError(it)
                loading = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Friends", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // CORECȚIE: S-a înlocuit Icons.Default.ArrowBack cu Icons.AutoMirrored.Filled.ArrowBack
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lilac)
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add friends by username",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = friendName,
                    onValueChange = { friendName = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Friend username") },
                    singleLine = true
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (friendName.isBlank()) return@Button
                        adding = true
                        repo.addFriend(
                            currentUsername,
                            friendName.trim()
                        ) { ok, err ->
                            adding = false
                            if (!ok) {
                                onError(err ?: "Failed to add friend")
                            } else {
                                // reload list după ce adaugi
                                repo.getFriends(
                                    currentUsername,
                                    onResult = { friends = it },
                                    onError = { onError(it) }
                                )
                                friendName = ""
                            }
                        }
                    },
                    enabled = !adding
                ) {
                    Text(if (adding) "Adding..." else "Add")
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Your friends",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            if (loading) {
                CircularProgressIndicator()
            } else if (friends.isEmpty()) {
                Text("No friends yet. Try adding one! 😊")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(friends) { friend ->
                        FriendRow(friend)
                    }
                }
            }
        }
    }
}

@Composable
private fun FriendRow(friend: User) {
    // poți adapta culorile după tema ta
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4E5BA6),
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = friend.username,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "High score: ${friend.highScore}",
                fontSize = 14.sp
            )

            val badges = mutableListOf<String>()
            if (badges.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Badges: ${badges.joinToString()}",
                    fontSize = 13.sp
                )
            }
        }
    }
}