package com.leecoder.stockchart.room

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.leecoder.stockchart.room.entity.KrxSymbolEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Provider

class AppDatabaseCallback @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dbProvider: Provider<AppDatabase>,
) : RoomDatabase.Callback() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        scope.launch {
            try {
                val iscds = mutableListOf<KrxSymbolEntity>()

                context.assets.open("krxsymbol.csv")
                    .bufferedReader(Charset.forName("MS949"))
                    .useLines { lines ->
                        lines.drop(1).forEach { line ->
                            val spl = line.split(",".toRegex())

                            if (spl.isNotEmpty()) {
                                val code = spl[0]
                                val name = spl[1]

                                iscds += KrxSymbolEntity(
                                    code = code.trim('"'),
                                    name = name.trim('"'),
                                )
                            }
                        }
                    }

                if (iscds.isNotEmpty()) {
                    dbProvider.get().krxSymbolDao().insertAll(iscds)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}