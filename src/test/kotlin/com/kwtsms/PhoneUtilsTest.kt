package com.kwtsms

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

// 2026-11-05 09:07 +03: Kuwait numbers come from KWTSMS_TEST_NUMBER via TestNumber (skips when unset);
// pass-through values use the 965XXXXXXXX placeholder; other samples no longer embed the old Kuwait sample.
// Why: the old hardcoded sample belongs to a real person. Task: int-testnumber
class PhoneUtilsTest {

    // ──────────────────────────────────────────────
    // normalizePhone()
    // ──────────────────────────────────────────────

    @Test
    fun `normalizePhone - plus prefix stripped`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("plus")))
    }

    @Test
    fun `normalizePhone - double zero prefix stripped`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("00")))
    }

    @Test
    fun `normalizePhone - spaces stripped`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("spaces")))
    }

    @Test
    fun `normalizePhone - dashes stripped`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("dashes")))
    }

    @Test
    fun `normalizePhone - dots stripped`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("dots")))
    }

    @Test
    fun `normalizePhone - parentheses stripped`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("parens")))
    }

    @Test
    fun `normalizePhone - slashes stripped`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("slashes")))
    }

    @Test
    fun `normalizePhone - mixed separators`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("mixed")))
    }

    @Test
    fun `normalizePhone - Arabic-Indic digits converted`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("arabic")))
    }

    @Test
    fun `normalizePhone - Extended Arabic-Indic digits converted`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("persian")))
    }

    @Test
    fun `normalizePhone - leading zeros stripped`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("00")))
    }

    @Test
    fun `normalizePhone - single leading zero stripped`() {
        assertEquals("512345678", PhoneUtils.normalizePhone("0512345678"))
    }

    @Test
    fun `normalizePhone - empty string`() {
        assertEquals("", PhoneUtils.normalizePhone(""))
    }

    @Test
    fun `normalizePhone - only spaces`() {
        assertEquals("", PhoneUtils.normalizePhone("   "))
    }

    @Test
    fun `normalizePhone - only letters`() {
        assertEquals("", PhoneUtils.normalizePhone("abcdef"))
    }

    @Test
    fun `normalizePhone - only zeros`() {
        assertEquals("", PhoneUtils.normalizePhone("0000"))
    }

    @Test
    fun `normalizePhone - Saudi trunk prefix stripped (9660559)`() {
        assertEquals("966559876543", PhoneUtils.normalizePhone("9660559876543"))
    }

    @Test
    fun `normalizePhone - Saudi with plus and trunk prefix`() {
        assertEquals("966559876543", PhoneUtils.normalizePhone("+9660559876543"))
    }

    @Test
    fun `normalizePhone - Saudi with 00 and trunk prefix`() {
        assertEquals("966559876543", PhoneUtils.normalizePhone("009660559876543"))
    }

    @Test
    fun `normalizePhone - UAE trunk prefix stripped (97105x)`() {
        assertEquals("971501234567", PhoneUtils.normalizePhone("9710501234567"))
    }

    @Test
    fun `normalizePhone - Egypt trunk prefix stripped (2001x)`() {
        // +20 0 1012345678 -> strip +, strip trunk 0 after cc 20
        assertEquals("201012345678", PhoneUtils.normalizePhone("+2001012345678"))
    }

    @Test
    fun `normalizePhone - Kuwait no trunk prefix (no change)`() {
        TestNumber.assertSame(TestNumber.get(), PhoneUtils.normalizePhone(TestNumber.variant("plain")))
    }

    // ──────────────────────────────────────────────
    // findCountryCode()
    // ──────────────────────────────────────────────

    @Test
    fun `findCountryCode - 3-digit code (Kuwait 965)`() {
        assertEquals("965", PhoneUtils.findCountryCode(TestNumber.PLACEHOLDER))
    }

    @Test
    fun `findCountryCode - 2-digit code (Egypt 20)`() {
        assertEquals("20", PhoneUtils.findCountryCode("201012345678"))
    }

    @Test
    fun `findCountryCode - 1-digit code (USA 1)`() {
        assertEquals("1", PhoneUtils.findCountryCode("12125551234"))
    }

    @Test
    fun `findCountryCode - unknown code returns null`() {
        assertNull(PhoneUtils.findCountryCode("99999999999"))
    }

    // ──────────────────────────────────────────────
    // validatePhoneFormat()
    // ──────────────────────────────────────────────

    @Test
    fun `validatePhoneFormat - valid Kuwait number`() {
        val (valid, error) = PhoneUtils.validatePhoneFormat(TestNumber.get())
        assertTrue(valid)
        assertNull(error)
    }

    @Test
    fun `validatePhoneFormat - Kuwait wrong length`() {
        val (valid, error) = PhoneUtils.validatePhoneFormat(TestNumber.get().dropLast(1)) // 7 local digits, needs 8
        assertFalse(valid)
        assertTrue(error!!.contains("Kuwait"))
        assertTrue(error.contains("8 digits"))
    }

    @Test
    fun `validatePhoneFormat - Kuwait wrong mobile prefix`() {
        val (valid, error) = PhoneUtils.validatePhoneFormat("96518765432") // starts with 1, invalid
        assertFalse(valid)
        assertTrue(error!!.contains("Kuwait"))
        assertTrue(error.contains("must start with"))
    }

    @Test
    fun `validatePhoneFormat - Saudi valid`() {
        val (valid, _) = PhoneUtils.validatePhoneFormat("966559876543")
        assertTrue(valid)
    }

    @Test
    fun `validatePhoneFormat - Saudi wrong mobile prefix`() {
        val (valid, error) = PhoneUtils.validatePhoneFormat("966159876543") // starts with 1
        assertFalse(valid)
        assertTrue(error!!.contains("Saudi Arabia"))
    }

    @Test
    fun `validatePhoneFormat - unknown country passes through`() {
        val (valid, _) = PhoneUtils.validatePhoneFormat("99912345678")
        assertTrue(valid)
    }

    @Test
    fun `validatePhoneFormat - Belgium length only (no mobile prefix check)`() {
        val (valid, _) = PhoneUtils.validatePhoneFormat("32471234567") // 9 local digits
        assertTrue(valid)
    }

    // ──────────────────────────────────────────────
    // validatePhoneInput()
    // ──────────────────────────────────────────────

    @Test
    fun `validatePhoneInput - valid Kuwait number`() {
        val (valid, error, normalized) = PhoneUtils.validatePhoneInput(TestNumber.variant("plain"))
        assertTrue(valid)
        assertNull(error)
        TestNumber.assertSame(TestNumber.get(), normalized)
    }

    @Test
    fun `validatePhoneInput - valid with plus prefix`() {
        val (valid, error, normalized) = PhoneUtils.validatePhoneInput(TestNumber.variant("plus"))
        assertTrue(valid)
        assertNull(error)
        TestNumber.assertSame(TestNumber.get(), normalized)
    }

    @Test
    fun `validatePhoneInput - valid with 00 prefix`() {
        val (valid, error, normalized) = PhoneUtils.validatePhoneInput(TestNumber.variant("00"))
        assertTrue(valid)
        assertNull(error)
        TestNumber.assertSame(TestNumber.get(), normalized)
    }

    @Test
    fun `validatePhoneInput - minimum valid length (7 digits)`() {
        // Use a number that doesn't match any country code (e.g. 999...)
        val (valid, error, _) = PhoneUtils.validatePhoneInput("9991234")
        assertTrue(valid)
        assertNull(error)
    }

    @Test
    fun `validatePhoneInput - maximum valid length (15 digits)`() {
        // Use a number that doesn't match any country code
        val (valid, error, _) = PhoneUtils.validatePhoneInput("999123456789012")
        assertTrue(valid)
        assertNull(error)
    }

    @Test
    fun `validatePhoneInput - empty string`() {
        val (valid, error, _) = PhoneUtils.validatePhoneInput("")
        assertFalse(valid)
        assertEquals("Phone number is required", error)
    }

    @Test
    fun `validatePhoneInput - blank string`() {
        val (valid, error, _) = PhoneUtils.validatePhoneInput("   ")
        assertFalse(valid)
        assertEquals("Phone number is required", error)
    }

    @Test
    fun `validatePhoneInput - email address`() {
        val (valid, error, _) = PhoneUtils.validatePhoneInput("user@example.com")
        assertFalse(valid)
        assertTrue(error!!.contains("email address"))
    }

    @Test
    fun `validatePhoneInput - no digits`() {
        val (valid, error, _) = PhoneUtils.validatePhoneInput("abcdef")
        assertFalse(valid)
        assertTrue(error!!.contains("no digits found"))
    }

    @Test
    fun `validatePhoneInput - too short (6 digits)`() {
        val (valid, error, _) = PhoneUtils.validatePhoneInput("123456")
        assertFalse(valid)
        assertTrue(error!!.contains("too short"))
        assertTrue(error.contains("6 digits"))
    }

    @Test
    fun `validatePhoneInput - too long (16 digits)`() {
        val (valid, error, _) = PhoneUtils.validatePhoneInput("1234567890123456")
        assertFalse(valid)
        assertTrue(error!!.contains("too long"))
        assertTrue(error.contains("16 digits"))
    }

    @Test
    fun `validatePhoneInput - Arabic digits converted and validated`() {
        // The test number written in Arabic-Indic digits (11 digits, valid)
        val (valid, error, normalized) = PhoneUtils.validatePhoneInput(TestNumber.variant("arabic"))
        assertTrue(valid)
        assertNull(error)
        TestNumber.assertSame(TestNumber.get(), normalized)
    }

    @Test
    fun `validatePhoneInput - whitespace trimmed before validation`() {
        val (valid, error, normalized) = PhoneUtils.validatePhoneInput(TestNumber.variant("padded"))
        assertTrue(valid)
        assertNull(error)
        TestNumber.assertSame(TestNumber.get(), normalized)
    }

    @Test
    fun `validatePhoneInput - Saudi trunk prefix stripped and valid`() {
        val (valid, error, normalized) = PhoneUtils.validatePhoneInput("+9660559876543")
        assertTrue(valid)
        assertNull(error)
        assertEquals("966559876543", normalized)
    }

    @Test
    fun `validatePhoneInput - Kuwait invalid mobile prefix rejected`() {
        val (valid, error, _) = PhoneUtils.validatePhoneInput("96518765432")
        assertFalse(valid)
        assertTrue(error!!.contains("Kuwait"))
    }

    @Test
    fun `validatePhoneInput - Saudi wrong length rejected`() {
        val (valid, error, _) = PhoneUtils.validatePhoneInput("96655987") // too few local digits
        assertFalse(valid)
        assertTrue(error!!.contains("Saudi Arabia"))
    }

    // ──────────────────────────────────────────────
    // deduplicatePhones()
    // ──────────────────────────────────────────────

    @Test
    fun `deduplicatePhones - removes duplicates preserving order`() {
        val input = listOf(TestNumber.PLACEHOLDER, "96512345678", TestNumber.PLACEHOLDER, "96512340000", "96512345678")
        val result = PhoneUtils.deduplicatePhones(input)
        assertEquals(listOf(TestNumber.PLACEHOLDER, "96512345678", "96512340000"), result)
    }

    @Test
    fun `deduplicatePhones - no duplicates returns same list`() {
        val input = listOf(TestNumber.PLACEHOLDER, "96512345678")
        val result = PhoneUtils.deduplicatePhones(input)
        assertEquals(input, result)
    }

    @Test
    fun `deduplicatePhones - empty list`() {
        val result = PhoneUtils.deduplicatePhones(emptyList())
        assertEquals(emptyList(), result)
    }
}
