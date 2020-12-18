package com.ironone.data.repositories

import android.provider.ContactsContract
import com.ironone.data.db.ContactsDAO
import com.ironone.data.entities.ContactDataEntitiesMapper
import com.ironone.data.entities.ContactEntityDataMapper
import com.ironone.data.entities.mapToEntity
import com.ironone.domain.entities.ContactEntity
import com.ironone.domain.repositories.ContactsRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

class ContactsRepositoryImpl(private val dao: ContactsDAO): ContactsRepository {

    private val dataEntityMapper = ContactDataEntitiesMapper()
    private val entityDataMapper = ContactEntityDataMapper()

    @FlowPreview
    override suspend fun getAllContacts(): Flow<List<ContactEntity>> {
        return dao.getAllContacts().flatMapConcat {data ->
            val dataEntity = dataEntityMapper.mapToEntity(data)
            flowOf(dataEntity)
        }
    }

    override suspend fun getContactForId(id: Int): ContactEntity {
        return dao.getContactForId(id).mapToEntity()
    }

    override suspend fun deleteContactWithId(id: Int) {
        dao.deleteContactWithId(id)
    }

    override suspend fun insertContact(contact: ContactEntity) {
        dao.insert(entityDataMapper.mapToData(contact))
    }

    override suspend fun updateContact(contact: ContactEntity) {
        dao.update(entityDataMapper.mapToData(contact))
    }
}