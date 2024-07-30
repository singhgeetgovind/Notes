package com.singhgeetgovind.notes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.singhgeetgovind.notes.dao.NotesDao
import com.singhgeetgovind.notes.database.NotesDatabase
import com.singhgeetgovind.notes.model.Notes
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject

@HiltAndroidTest
class NotesDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @Inject
    lateinit var notesDatabase: NotesDatabase

    @Inject lateinit var notesDao: NotesDao
    @Before
    fun setUpDB(){
        hiltAndroidRule.inject()
    }

    @Test
    fun insertNotes_expectedSingleData() = runTest{
        val notes = Notes(1,"title","description",
           0,
            Date(System.currentTimeMillis()),0L,0)

        notesDao.addDetails(notes)
        val result = notesDao.getAllDetails().getOrAwaitValue()
        assertThat(notes,`is` (result[0]))
    }
    @After
    fun closeDB(){
        notesDatabase.close()
    }
}