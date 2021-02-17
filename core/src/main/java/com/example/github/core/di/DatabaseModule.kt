package com.example.github.core.di

import android.content.Context
import androidx.room.Room
import com.example.github.core.data.source.local.room.UserDao
import com.example.github.core.data.source.local.room.UserDatabase
import dagger.Module
import dagger.Provides
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): UserDatabase {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("github".toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java, "User.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    fun provideTourismDao(database: UserDatabase): UserDao = database.userDao()
}