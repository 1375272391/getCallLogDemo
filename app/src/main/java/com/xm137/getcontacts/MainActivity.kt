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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
fun DisplayTest(modifier: Modifier) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        val permissionState = rememberPermissionState(
            permission = Manifest.permission.READ_CALL_LOG
        )

        if (permissionState.status.isGranted) {
            Text("已取得读取通话记录读取权限")
            Log.i("TAG", "READ_CALL_LOG is GRANTED")
        }
            else {
            Text("未授予读取通话记录读取权限")
            Button(
                onClick = {
                    permissionState.launchPermissionRequest()
                }
            ) {
                Text("Request Permission")
            }
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (permissionState.status.isGranted) {
                val cursor : Cursor? = context.contentResolver.query(
                    CallLog.Calls.CONTENT_URI.buildUpon()
                        .appendQueryParameter(LIMIT_PARAM_KEY, "1")
                        .build(),
                    null, null, null, null)
                cursor?.use {
                    while (it.moveToNext()) {
                        val id = it.getInt(it.getColumnIndexOrThrow("_id"))
                        val formattedNumber = it.getString(it.getColumnIndexOrThrow("formatted_number"))
                        val duration = it.getInt(it.getColumnIndexOrThrow("duration"))
                        val number = it.getString(it.getColumnIndexOrThrow("number"))
                        val date = it.getLong(it.getColumnIndexOrThrow("date"))
                        val type = it.getInt(it.getColumnIndexOrThrow("type"))
                        Text("id: $id")
                        Text(formattedNumber)
                        Text("dur: $duration")
                        Text("Number: $number")
                        Text("date: $date")
                        Text("Type: $type")
                    }
                }
            }
        }
    }
}


