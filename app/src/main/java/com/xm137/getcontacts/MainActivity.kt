package com.xm137.getcontacts

import android.Manifest
import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.provider.CallLog.Calls.LIMIT_PARAM_KEY
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.xm137.getcontacts.ui.theme.GetcontactsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GetcontactsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DisplayTest(
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DisplayTest(
    modifier: Modifier,
    callLogViewModel: CallLogViewModel = viewModel()
) {
    val callLogUiState by callLogViewModel.callLogState.collectAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        val permissionState = rememberPermissionState(
            permission = Manifest.permission.READ_CALL_LOG
        )

        if (permissionState.status.isGranted) {
            Text(stringResource(R.string.has_been_permissions))
            Log.i("TAG", "READ_CALL_LOG is GRANTED")
        }
            else {
            Text(stringResource(R.string.no_permissions))
            Button(
                onClick = {
                    permissionState.launchPermissionRequest()
                }
            ) {
                Text(stringResource(R.string.request_permission))
            }
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (permissionState.status.isGranted) {
                val cursor : Cursor? = context.contentResolver.query(
                    CallLog.Calls.CONTENT_URI.buildUpon()
//                        .appendQueryParameter(LIMIT_PARAM_KEY, "1")
                        .build(),
                    null, null, null, null)
                cursor?.use {
                    val data = mutableListOf<CallLogInfo>()
                    while (it.moveToNext()) {
                        data.add(
                            CallLogInfo(
                                id = it.getInt(it.getColumnIndexOrThrow("_id")),
                                formattedNumber = it.getString(it.getColumnIndexOrThrow("formatted_number")),
                                duration = it.getInt(it.getColumnIndexOrThrow("duration")),
                                number = it.getString(it.getColumnIndexOrThrow("number")),
                                date = it.getLong(it.getColumnIndexOrThrow("date")),
                                type = it.getInt(it.getColumnIndexOrThrow("type")),
                            )
                        )
                        callLogViewModel.addData(data)
                    }
                }
            }
        }
        if (callLogUiState.callLog.isNotEmpty()) {
            LazyColumn {
                item {
                    Row (
                      modifier = Modifier.padding(6.dp)
                    ) {
                        Text(stringResource(R.string.sum))
                        Text(callLogUiState.callLog.size.toString())
                    }
                }
                items(callLogUiState.callLog) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Text(stringResource(R.string.formatted_number) + it.formattedNumber)
                            Text(stringResource(R.string.duration) + it.duration.toString())
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}


data class CallLogInfo (
    val id: Int,
    val formattedNumber: String,
    val duration: Int,
    val number: String,
    val date: Long,
    val type: Int
)


//val id = it.getInt(it.getColumnIndexOrThrow("_id"))
//val formattedNumber = it.getString(it.getColumnIndexOrThrow("formatted_number"))
//val duration = it.getInt(it.getColumnIndexOrThrow("duration"))
//val number = it.getString(it.getColumnIndexOrThrow("number"))
//val date = it.getLong(it.getColumnIndexOrThrow("date"))
//val type = it.getInt(it.getColumnIndexOrThrow("type"))
//Text("id: $id")
//Text(formattedNumber)
//Text("dur: $duration")
//Text("Number: $number")
//Text("date: $date")
//Text("Type: $type")