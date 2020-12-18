package com.ironone.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ironone.data.entities.ContactData
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDAO {

    @Insert
    suspend fun insert(contact: ContactData)

    @Update
    suspend fun update(contact: ContactData)

    @Query("DELETE FROM contacts_table WHERE id =:id")
    suspend fun deleteContactWithId(id: Int)

    @Query("SELECT * FROM contacts_table")
    fun getAllContacts() : Flow<List<ContactData>>

    @Query("SELECT * FROM contacts_table WHERE id = :id")
    suspend fun getContactForId(id: Int) : ContactData


}