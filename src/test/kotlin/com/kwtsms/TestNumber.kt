/*
 * FILE:      src/test/kotlin/com/kwtsms/TestNumber.kt
 * CREATED:   2026-11-05 09:07 +03
 * TASK:      int-testnumber
 * AUTHOR:    M Alhassoun
 * WHAT:      Shared test helper: the test phone number and its format variants.
 * WHY:       Tests must not hardcode a phone number. The number comes from the
 *            KWTSMS_TEST_NUMBER environment variable (an international Kuwait
 *            mobile, digits only, 965XXXXXXXX) and every test that needs one
 *            is skipped when it is unset or malformed.
 * USAGE:     KWTSMS_TEST_NUMBER=965XXXXXXXX ./gradlew test
 *            CI reads it from the repository secret of the same name.
 *            Tests call TestNumber.get() or TestNumber.variant("kind"), then
 *            compare with TestNumber.assertSame so a failure never prints the
 *            number. Never log the value.
 * DEPENDS:   JUnit 5 (Assumptions) and kotlin-test, already test dependencies.
 */
package com.kwtsms

import org.junit.jupiter.api.Assumptions.assumeTrue
import kotlin.test.assertTrue

internal object TestNumber {

    /** For values only passed through unchanged (for example a mocked response echo). */
    const val PLACEHOLDER = "965XXXXXXXX"

    private const val SKIP_REASON = "KWTSMS_TEST_NUMBER not set (digits only, 965 plus 8 digits)"

    // [0-9], not \d: keeps the check to Latin digits only.
    private val VALUE: String? = System.getenv("KWTSMS_TEST_NUMBER")?.trim()
        ?.takeIf { Regex("^965[0-9]{8}$").matches(it) }

    /** KWTSMS_TEST_NUMBER, or skips the running test when it is unset or malformed. */
    fun get(): String {
        assumeTrue(VALUE != null, SKIP_REASON)
        return VALUE!!
    }

    /** Latin digits to Arabic-Indic (U+0660-0669). */
    fun toArabicIndic(s: String): String = shift(s, '٠')

    /** Latin digits to Extended Arabic-Indic / Persian (U+06F0-06F9). */
    fun toPersian(s: String): String = shift(s, '۰')

    private fun shift(s: String, zero: Char): String =
        s.map { c -> if (c in '0'..'9') zero + (c - '0') else c }.joinToString("")

    /** The test number written in the format named by kind. Skips when unset. */
    fun variant(kind: String): String {
        val n = get()
        val cc = n.substring(0, 3)
        val a = n.substring(3, 7)
        val b = n.substring(7)
        return when (kind) {
            "plain" -> n
            "plus" -> "+$n"
            "00" -> "00$n"
            "0" -> "0$n"
            "spaces" -> "$cc $a $b"
            "dashes" -> "$cc-$a-$b"
            "dots" -> "$cc.$a.$b"
            "slashes" -> "$cc/$a/$b"
            "parens" -> "($cc) $a$b"
            "mixed" -> "+$cc (${n.substring(3, 6)}) ${n.substring(6, 8)}-${n.substring(8)}"
            "padded" -> "  $n  "
            "arabic" -> toArabicIndic(n)
            "persian" -> toPersian(n)
            else -> throw IllegalArgumentException("unknown variant kind: $kind")
        }
    }

    /** Assert equality without printing either value (they may hold the number). */
    fun assertSame(expected: String?, actual: String?) {
        assertTrue(expected == actual, "phone number does not match the expected value (values hidden)")
    }
}
