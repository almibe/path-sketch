/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.svgpath

class SVGPathParser {
    private val commandChars: Map<Char, Int> = mutableMapOf(
            'M' to 2,
            'm' to 2,
            'L' to 2,
            'l' to 2,
            'H' to 1,
            'h' to 1,
            'V' to 1,
            'v' to 1,
            'Z' to 0,
            'z' to 0,
            'C' to 6,
            'c' to 6,
            'Q' to 4,
            'q' to 4,
            'S' to 4,
            's' to 4,
            'T' to 2,
            't' to 2,
            'A' to 7,
            'a' to 7
    )
    private val numberChars = "0123456789".toCharArray()
    private val ignoreChars = " ,\n\t".toCharArray()

    fun parse(path: String): List<SVGCommand> {
        val itr = path.toCharArray().iterator()
        val commands = mutableListOf<SVGCommand>()
        var lastCommand = ' '
        while (itr.hasNext()) {
            val next = itr.next()
            val nextCommand = when (next) {
                in commandChars.keys -> {
                    lastCommand = next
                    readCommand(next, itr)
                }
                in numberChars -> {
                    readRepeatedCommand(lastCommand, next, itr)
                }
                in ignoreChars -> {
                    null
                }
                else -> throw RuntimeException("Unexpected char - $next")
            }
            if (nextCommand != null) {
                commands.add(nextCommand)
            }
        }
        return commands
    }

    private fun readCommand(currentChar: Char, itr: CharIterator): SVGCommand {
        val commandChar = currentChar
        TODO()
    }

    private fun readRepeatedCommand(command: Char, currentChar: Char, itr: CharIterator): SVGCommand {
        TODO()
    }

    private fun readNumber(itr: CharIterator): Double {
        TODO()
    }
}
