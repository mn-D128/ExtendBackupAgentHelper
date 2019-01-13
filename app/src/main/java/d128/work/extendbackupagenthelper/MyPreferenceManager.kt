package d128.work.extendbackupagenthelper

import android.content.Context
import android.content.SharedPreferences

object MyPreferenceManager {

    private val NAME = "PREF"

    private val KEY = "KEY"
    private val KEY_CAN_RESTORE = "KEY_CAN_RESTORE"

    private val LOCK = Object()

    private fun getPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun getValue(context: Context): Int {
        synchronized(LOCK) {
            val pref = getPreference(context)
            return pref.getInt(KEY, 0)
        }
    }

    fun setValue(context: Context, value: Int) {
        synchronized(LOCK) {
            val pref = getPreference(context)
            val editor = pref.edit()
            editor.putInt(KEY, value)
            editor.commit()
        }
    }

    fun canRestore(context: Context): Boolean {
        synchronized(LOCK) {
            val pref = getPreference(context)
            return pref.getBoolean(KEY_CAN_RESTORE, true)
        }
    }

    fun setRestore(context: Context, canRestore: Boolean) {
        synchronized(LOCK) {
            val pref = getPreference(context)
            val editor = pref.edit()
            editor.putBoolean(KEY_CAN_RESTORE, canRestore)
            editor.commit()
        }
    }

}