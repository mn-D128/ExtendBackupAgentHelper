package d128.work.extendbackupagenthelper

import android.app.backup.BackupAgentHelper
import android.app.backup.BackupDataInput
import android.app.backup.BackupDataOutput
import android.content.Context
import android.os.ParcelFileDescriptor
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

class MyBackupAgent: BackupAgentHelper() {

    companion object {
        private val ENTITY_KEY = "ENTITY_KEY"
    }

    override fun onBackup(oldState: ParcelFileDescriptor?, data: BackupDataOutput?, newState: ParcelFileDescriptor?) {
        val context = this as Context

        val buffer: ByteArray = ByteArrayOutputStream().run {
            DataOutputStream(this).apply {
                writeInt(MyPreferenceManager.getValue(context))
            }
            toByteArray()
        }

        data?.apply {
            writeEntityHeader(ENTITY_KEY, buffer.size)
            writeEntityData(buffer, buffer.size)
        }
    }

    override fun onRestore(data: BackupDataInput?, appVersionCode: Int, newState: ParcelFileDescriptor?) {
        val context = this as Context

        data?.apply {
            while (readNextHeader()) {
                when(key) {
                    ENTITY_KEY -> {
                        val dataBuf = ByteArray(dataSize).also {
                            readEntityData(it, 0, dataSize)
                        }

                        ByteArrayInputStream(dataBuf).also {
                            DataInputStream(it).apply {
                                MyPreferenceManager.setValue(context, readInt())
                            }
                        }

                        MyPreferenceManager.setRestore(context, false)
                    }
                    else -> skipEntityData()
                }
            }
        }
    }

}