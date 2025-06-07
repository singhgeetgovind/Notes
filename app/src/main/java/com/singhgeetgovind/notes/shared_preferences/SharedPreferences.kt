package com.singhgeetgovind.notes.shared_preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object{
        const val TAG = "SharedPreferences"
    }
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("Theme", MODE_PRIVATE)
    private val edit: SharedPreferences.Editor by lazy { sharedPreferences.edit() }

    inline fun <reified T> fetchSharedPrefData(keyName:String):T?{
        val values = (sharedPreferences.all.filter { it.key == keyName }.values.firstOrNull())
        Log.d(TAG, "fetchSharedPrefData: $keyName $values ")
        return if(values is T){
            values
        }else null
    }
    fun <T> saveSharedPrefData(keyName:String,values:T):Boolean {
        return when(values) {
            is Long-> { edit.putLong(keyName, values).apply()
                true
            }
            is Int-> { edit.putInt(keyName, values).apply()
                true
            }
            is String-> { edit.putString(keyName, values).apply()
                true
            }
            is Float-> { edit.putFloat(keyName, values).apply()
                true
            }
            else->{
                false
            }
        }
    }
    fun clearPreferences() : Boolean{
        edit.clear().apply()
        return sharedPreferences.all.isEmpty().apply{
            Log.d(TAG, "clearPreferences: $this")
        }
    }
}