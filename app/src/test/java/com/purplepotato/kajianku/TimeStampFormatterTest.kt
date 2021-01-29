package com.purplepotato.kajianku

import com.purplepotato.kajianku.core.util.Helpers
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeStampFormatterTest {
    @Test
    fun `should change timestamp to time format`() {
        val timestamp = 1611618575278
        assertEquals("06.49", Helpers.convertTimeStampToTimeFormat(timestamp))
    }

    @Test
    fun `should change timestamp to date format`() {
        val timestamp = 1589130000000
        assertEquals("Monday, 11 May 2020", Helpers.convertTimeStampToDateFormat(timestamp))
    }

    @Test
    fun `should change timestamp to date`(){
        val timestamp = 1589130000000
        assertEquals("11 May 2020", Helpers.convertTimeStampToDateTimeFormat(timestamp))
    }
}