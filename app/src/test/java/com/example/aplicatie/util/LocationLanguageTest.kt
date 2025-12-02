package com.example.aplicatie.util

import org.junit.Assert.assertEquals
import org.junit.Test

class LocationLanguageTest {

    @Test
    fun `romanian countries map to ro`() {
        assertEquals("ro", LocationLanguage.mapCountryToLang("RO"))
        assertEquals("ro", LocationLanguage.mapCountryToLang("ro"))
        assertEquals("ro", LocationLanguage.mapCountryToLang("Md")) // case-insensitive
    }

    @Test
    fun `german speaking countries map to de`() {
        assertEquals("de", LocationLanguage.mapCountryToLang("DE"))
        assertEquals("de", LocationLanguage.mapCountryToLang("at"))
        assertEquals("de", LocationLanguage.mapCountryToLang("Ch"))
    }

    @Test
    fun `unknown country defaults to en`() {
        assertEquals("en", LocationLanguage.mapCountryToLang("FR"))
        assertEquals("en", LocationLanguage.mapCountryToLang("US"))
        assertEquals("en", LocationLanguage.mapCountryToLang(null))
    }
}
