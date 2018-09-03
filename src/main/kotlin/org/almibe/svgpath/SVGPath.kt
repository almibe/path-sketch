/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.svgpath

class SVGPath(val commands: List<SVGCommand>) {
    fun computeStart(): Pair<Double, Double> {
        val itr = commands.iterator()
        val firstCommand = itr.next()
        var x = 0.0
        var y = 0.0
        when (firstCommand) {
            is Move -> { x = firstCommand.x; y = firstCommand.y }
            is RelativeMove -> { x = firstCommand.x; y = firstCommand.y }
            else -> throw RuntimeException("Unexpected first command $firstCommand")
        }
        while (itr.hasNext()) {
            val next = itr.next()
            val pathDimensions = next.pathDimensions()
            if (pathDimensions.x0 < x) {
                x = pathDimensions.x0
            }
            if (pathDimensions.y0 < y) {
                y = pathDimensions.y0
            }
        }

        return x to y
    }

    fun translate(right: Double, down: Double): SVGPath {
        return SVGPath(this.commands.map {
            it.translate(right, down)
        }.toList())
    }

    fun scale(scaleBy: Double): SVGPath {
        return SVGPath(this.commands.map {
            it.scale(scaleBy)
        }.toList())
    }

    fun toPathString(): String {
        val sb = StringBuilder()
        this.commands.forEach {
            sb.append(it.toPathString() + " ")
        }
        return sb.toString().trim()
    }
}

data class PathDimensions(val x0: Double, val y0: Double, val x1: Double, val y1: Double)
