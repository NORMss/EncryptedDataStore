package ru.normno.myencrypteddatastore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.normno.myencrypteddatastore.ui.theme.MyEncryptedDataStoreTheme

private val Context.dataStore by dataStore(
    fileName = "user-preferences",
    serializer = UserPreferencesSerializer,
)

//private val SECRET_TOKEN = (1..1000).map {
//    (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
//}.joinToString(separator = "")

private const val SECRET_TOKEN = "Hello, Crypto!"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            var text by remember {
                mutableStateOf("")
            }

            MyEncryptedDataStoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Button(
                            onClick = {
                                scope.launch(Dispatchers.IO) {
                                    dataStore.updateData {
                                        UserPreferences(
                                            token = SECRET_TOKEN
                                        )
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "Encrypt"
                            )
                        }
                        Button(
                            onClick = {
                                scope.launch(Dispatchers.IO) {
                                    text = dataStore.data.first().token ?: "null"
                                }
                            }
                        ) {
                            Text(
                                text = "Decrypt"
                            )
                        }
                        Text(
                            text = text
                        )
                    }
                }
            }
        }
    }
}