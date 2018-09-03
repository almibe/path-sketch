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
    private val numberChars = "-0123456789".toCharArray()
    private val doubleChars = "-0123456789.".toCharArray()
    private val ignoreChars = " ,\n\t".toCharArray()

    fun parse(path: String): SVGPath {
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
        return SVGPath(commands)
    }

    private fun readCommand(commandChar: Char, itr: CharIterator): SVGCommand {
        val numberOfArgs = commandChars[commandChar]!!
        val args = mutableListOf<Double>()
        for (i in 1..numberOfArgs) {
            val number = readNumber(itr, null) ?: throw RuntimeException("Error reading $commandChar $i")
            args.add(number)
        }
        return createCommand(commandChar, args)
    }

    private fun readRepeatedCommand(commandChar: Char, currentChar: Char, itr: CharIterator): SVGCommand {
        val numberOfArgs = commandChars[commandChar]!!
        val args = mutableListOf<Double>()
        val firstArg = readNumber(itr, currentChar) ?: throw RuntimeException("Error reading $commandChar 1")
        args.add(firstArg)
        for (i in 2..numberOfArgs) {
            val number = readNumber(itr, null) ?: throw RuntimeException("Error reading $commandChar $i")
            args.add(number)
        }
        return createCommand(commandChar, args)
    }

    private fun readNumber(itr: CharIterator, firstChar: Char?): Double? {
        val sb = StringBuilder()
        if (firstChar != null) {
            sb.append(firstChar)
        }
        readNumbers@ while (itr.hasNext()) {
            val next = itr.next()
            when (next) {
                in doubleChars -> sb.append(next)
                in ignoreChars -> {
                    if (sb.isNotEmpty()) {
                        break@readNumbers
                    }
                }
                else -> break@readNumbers
            }
        }
        return sb.toString().toDoubleOrNull()
    }

    private fun createCommand(commandChar: Char, args: List<Double>): SVGCommand {
        return when (commandChar) {
            'M' -> Move(args[0], args[1])
            'm' -> RelativeMove(args[0], args[1])
            'L' -> Line(args[0], args[1])
            'l' -> RelativeLine(args[0], args[1])
            'H' -> HorizontalLine(args[0])
            'h' -> RelativeHorizontalLine(args[0])
            'V' -> VerticalLine(args[0])
            'v' -> RelativeVerticalLine(args[0])
            'Z' -> ClosePath()
            'z' -> ClosePath()
            'C' -> CubicBezierCurve(args[0], args[1], args[2], args[3], args[4], args[5])
            'c' -> RelativeCubicBezierCurve(args[0], args[1], args[2], args[3], args[4], args[5])
            'Q' -> QuadraticBezierCurve(args[0], args[1], args[2], args[3])
            'q' -> RelativeQuadraticBezierCurve(args[0], args[1], args[2], args[3])
            'S' -> SmoothCubicBezierCurve(args[0], args[1],args[2], args[3])
            's' -> SmoothRelativeCubicBezierCurve(args[0], args[1], args[2], args[3])
            'T' -> SmoothQuadraticBezierCurve(args[0], args[1])
            't' -> SmoothRelativeQuadraticBezierCurve(args[0], args[1])
            'A' -> Arc(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
            'a' -> RelativeArc(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
            else -> throw RuntimeException("Unexpected command char - $commandChar")
        }
    }
}
