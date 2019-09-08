package com.lapism.androidx.search.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lapism.androidx.search.BuildConfig


@Database(entities = [Search::class], version = BuildConfig.VERSION_CODE, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchDao(): SearchDao

    override fun close() {
        super.close()
        sDb?.close()
        sDb = null
    }

    companion object {

        private var sDb: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase? {
            if (sDb == null) {
                val c = context.applicationContext
                sDb = Room.databaseBuilder(c, AppDatabase::class.java, "searchDatabase").build()
            }
            return sDb
        }

    }

}


//volatile, synchornized, kotlini vveci

//         private val instances = AtomicInteger(0)          instances.incrementAndGet()
// @field:PrimaryKey(autoGenerate = true)
//.addMigrations(getDefaultMigration(c)/*Tady staci pridavat prislusne migrace, pokud nejake budou. Defaultni muze zustat, pripadne ji to overridne*/
//).fallbackToDestructiveMigration().build();
// allow queries on the main thread. Don't do this on a real app! See PersistenceBasicSample for an example.
//.allowMainThreadQueries()
/*private static Migration getDefaultMigration(final Context context) {
        int oldCode = CommentPrefs.getInstance(context).get(CommentPrefs.ROOM_VERSION, 1);
        final int newCode = BuildConfig.VERSION_CODE;

        return new Migration(oldCode, newCode) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {
                CommentPrefs.getInstance(context).put(CommentPrefs.ROOM_VERSION, newCode);
            }
        };
    }
    }*/

/*private static User addUser(final AppDatabase db, User user) {
        db.userDao().insertAll(user);
        return user;
    }

    private static void populateWithTestData(AppDatabase db) {
        User user = new User();
        user.setFirstName("Ajay");
        user.setLastName("Saini");
        user.setAge(25);
        addUser(db, user);
    }*/