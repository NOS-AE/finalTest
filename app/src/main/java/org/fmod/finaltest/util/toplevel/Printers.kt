package org.fmod.finaltest.util.toplevel

import android.util.Log
import android.widget.Toast
import org.fmod.finaltest.MyApp

fun toast(msg: String) = Toast.makeText(MyApp.appContext, msg, Toast.LENGTH_SHORT).show()

fun log(msg: String) = Log.d("MyApp", msg)

fun networkLog(msg: String) = Log.d("MyApp-Network", msg)

fun localLog(msg: String) = Log.d("MyApp-Local", msg)

fun poiLog(msg: String) = Log.d("MyApp-POI", msg)