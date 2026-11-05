import com.kwtsms.KwtSMS

/**
 * Bulk SMS: send to >200 numbers with automatic batching.
 *
 * The client handles:
 * - Splitting numbers into batches of 200
 * - 0.5s delay between batches (respects API rate limit)
 * - Retrying ERR013 (queue full) with exponential backoff
 * - Deduplicating numbers before sending
 * - Aggregating results across batches
 */
fun main() {
    val sms = KwtSMS.fromEnv()

    // Your own opted-in recipients, one number per line (e.g. 965XXXXXXXX).
    // Never generate number ranges: generated numbers belong to real people.
    val numbers = java.io.File("recipients.txt").readLines().map { it.trim() }.filter { it.isNotEmpty() }

    println("Sending to ${numbers.size} numbers...")
    val result = sms.sendBulk(numbers, "Bulk message from kwtSMS Kotlin client")

    println("Result: ${result.result}")
    println("Batches: ${result.batches}")
    println("Numbers sent: ${result.numbers}")
    println("Credits used: ${result.pointsCharged}")
    println("Balance after: ${result.balanceAfter}")
    println("Message IDs: ${result.msgIds}")

    if (result.errors.isNotEmpty()) {
        println("\nBatch errors:")
        for (error in result.errors) {
            println("  Batch ${error.batch}: ${error.code} - ${error.description}")
        }
    }

    if (result.invalid.isNotEmpty()) {
        println("\nInvalid numbers:")
        for (entry in result.invalid) {
            println("  ${entry.input}: ${entry.error}")
        }
    }
}
