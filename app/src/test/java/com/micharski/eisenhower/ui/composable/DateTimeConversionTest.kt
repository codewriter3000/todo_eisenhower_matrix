package com.micharski.eisenhower.ui.composable

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.TimeZone

class DateTimeConversionTest {

    private val testDate = LocalDate.of(2026, 7, 25)

    // This mimics the logic in MyDatePickerDialog and DateTimePickerDialog
    private fun toMillis(date: LocalDate): Long {
        return date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    private fun fromMillis(millis: Long): LocalDate {
        return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
    }

    @Test
    fun testConversionInNewYork() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"))
        runTest()
    }

    @Test
    fun testConversionInSydney() {
        TimeZone.setDefault(TimeZone.getTimeZone("Australia/Sydney"))
        runTest()
    }

    @Test
    fun testConversionInHonolulu() {
        TimeZone.setDefault(TimeZone.getTimeZone("Pacific/Honolulu"))
        runTest()
    }

    @Test
    fun testConversionInUTC() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        runTest()
    }

    private fun runTest() {
        val millis = toMillis(testDate)
        val resultDate = fromMillis(millis)

        // Verify that the date remains the same regardless of the default TimeZone
        assertEquals("Date mismatch in ${TimeZone.getDefault().id}", testDate, resultDate)
        
        // Verify that the millis correspond to UTC midnight of that date
        // 2026-07-25 00:00:00 UTC = 1784937600000 ms
        assertEquals("Millis mismatch in ${TimeZone.getDefault().id}", 1784937600000L, millis)
    }
}
