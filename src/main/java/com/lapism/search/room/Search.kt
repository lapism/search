package com.lapism.search.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "search")
class Search : Parcelable {

    // *********************************************************************************************
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "icon")
    var icon = 0

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "subtitle")
    var subtitle: String? = null

    // *********************************************************************************************
    constructor()

    constructor(parcel: Parcel) {
        id = parcel.readInt()
        icon = parcel.readInt()
        title = parcel.readString()
        subtitle = parcel.readString()
    }

    // *********************************************************************************************
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(icon)
        parcel.writeString(title)
        parcel.writeString(subtitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    // *********************************************************************************************
    companion object CREATOR : Parcelable.Creator<Search> {
        override fun createFromParcel(parcel: Parcel): Search {
            return Search(parcel)
        }

        override fun newArray(size: Int): Array<Search?> {
            return arrayOfNulls(size)
        }
        // @JvmField
    }

}
