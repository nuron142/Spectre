package `in`.sunil.spectre.util

/**
 * Created by Sunil on 7/2/17.
 */

fun String?.isNotEmpty(block: (String) -> Unit) {
    if (this != null && this.isNotEmpty()) {
        block(this)
    }
}

fun String?.isEmpty(): Boolean {
    return this == null || this.length == 0
}

fun String?.isNotEmpty(): Boolean {
    return this?.length ?: 0 > 0
}