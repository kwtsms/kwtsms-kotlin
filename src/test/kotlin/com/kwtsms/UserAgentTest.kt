/*
 * FILE:      src/test/kotlin/com/kwtsms/UserAgentTest.kt
 * CREATED:   2026-11-05 14:20 +03
 * TASK:      #939 (int-measure-signups)
 * AUTHOR:    M Alhassoun
 * WHAT:      Unit tests for the User-Agent the client sends.
 * WHY:       kwtSMS counts accounts per integration from the User-Agent; it must be
 *            kwtsms-kotlin/<build.gradle.kts version> and an app token must never inject headers.
 * USAGE:     bash ./gradlew test --tests com.kwtsms.UserAgentTest
 * DEPENDS:   no network, no credentials.
 */
package com.kwtsms

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserAgentTest {

    @Test
    fun `version equals build gradle`() {
        val line = File("build.gradle.kts").readLines().first { it.startsWith("version = ") }
        assertEquals(line.substringAfter('"').substringBefore('"'), KwtSMS.VERSION)
    }

    @Test
    fun `default user agent is the library token`() {
        val sms = KwtSMS("kotlin_username", "kotlin_password", "TEST", true, "")
        assertEquals("kwtsms-kotlin/${KwtSMS.VERSION}", sms.userAgent)
        assertTrue(Regex("^kwtsms-kotlin/[0-9][0-9A-Za-z.+-]*$").matches(sms.userAgent))
        assertEquals(sms.userAgent, KwtSMS("kotlin_username", "kotlin_password").userAgent)
    }

    @Test
    fun `app token is prepended`() {
        val sms = KwtSMS("kotlin_username", "kotlin_password", logFile = "", appName = "kwtsms-wordpress/3.5.2")
        assertEquals("kwtsms-wordpress/3.5.2 kwtsms-kotlin/${KwtSMS.VERSION}", sms.userAgent)
    }

    @Test
    fun `sanitizer removes CR LF and non-ASCII and limits length`() {
        val ua = KwtSMS.buildUserAgent("my app\r\nX-Injected: 1")
        assertFalse(ua.contains('\r') || ua.contains('\n'))
        assertFalse(ua.contains(" X-Injected"))
        assertEquals("my-appX-Injected:-1 kwtsms-kotlin/${KwtSMS.VERSION}", ua)
        assertEquals(64, KwtSMS.buildUserAgent("a".repeat(200)).split(' ')[0].length)
        assertEquals("kwtsms-kotlin/${KwtSMS.VERSION}", KwtSMS.buildUserAgent(" \r\n "))
        assertEquals("shop/1.0 kwtsms-kotlin/${KwtSMS.VERSION}", KwtSMS.buildUserAgent("كوshop/1.0"))
    }
}
