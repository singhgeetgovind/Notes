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
    /*companion object{
        private const val TAG = "SharedPreferences"
    }*/
    private val TAG = "SharedPreferences"
    private val sharedPreferences = context.getSharedPreferences("Theme", MODE_PRIVATE)
    private val edit: SharedPreferences.Editor by lazy { sharedPreferences.edit() }
    init {
        Log.d(TAG, "intialize:")
    }
    fun <T> fetchSharedPrefData(keyName:String):T?{
        val values = (sharedPreferences.all.filter { it.key == keyName }.values.firstOrNull())
        Log.d(TAG, "fetchSharedPrefData: $keyName $values ")
        return (values as T)
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
        return sharedPreferences.all.isEmpty().run{
            Log.d(TAG, "clearPreferences: $this")
            this
        }
    }
}