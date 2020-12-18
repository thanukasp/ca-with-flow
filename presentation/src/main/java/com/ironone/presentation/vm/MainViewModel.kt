package com.ironone.presentation.vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ironone.domain.repositories.ContactsRepository
import com.ironone.domain.usecases.ContactsUseCase
import com.ironone.presentation.common.EntitiesToContactsMapper
import com.ironone.presentation.common.mapToContact
import com.ironone.presentation.common.mapToEntity
import com.ironone.presentation.entities.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf

class MainViewModel(repository: ContactsRepository) : BaseViewModel(repository) {

    private val contactUseCase = ContactsUseCase(repository)
    private val entitiesToContactsMapper = EntitiesToContactsMapper()

    private var contactsListLD = MutableLiveData<List<Contact>>()
    private var contactLD = MutableLiveData<Contact>()

    @FlowPreview
    override fun init() {
        super.init()
        getContacts()
    }

    @FlowPreview
    private fun getContacts() {
        viewModelScope.launch {
            updateContactList()
        }
    }

    @FlowPreview
    private suspend fun updateContactList() {
        contactUseCase.getAllContacts().flatMapConcat {data ->
            val contacts = entitiesToContactsMapper.mapToContacts(data)
            flowOf(contacts)
        }.collect { contacts ->
            withContext(Dispatchers.Main) {
                contactsListLD.postValue(contacts)
            }
        }
    }

    fun getContactForId(id: Int) {
        viewModelScope.launch {
            val contact = contactUseCase.getContactForId(id).mapToContact()
            withContext(Dispatchers.Main) {
                contactLD.postValue(contact)
            }
        }
    }

    fun deleteContact(id: Int) {
        viewModelScope.launch {
            contactUseCase.deleteContactWithId(id)
            //updateContactList()
        }
    }

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            contactUseCase.insertContact(contact.mapToEntity())
            //updateContactList()
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            contactUseCase.updateContact(contact.mapToEntity())
            //updateContactList()
        }
    }

    fun getContactsListLiveData() = contactsListLD
    fun getContactLiveData() = contactLD

}