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
            val startPos = next.startPos()
            if (startPos.first < x) {
                x = startPos.first
            }
            if (startPos.second < y) {
                y = startPos.second
            }
        }

        return x to y
    }


}

interface SVGCommand {
    fun startPos(): Pair<Double, Double>
}

data class Move(val x: Double, val y: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return x to y
    }
}

data class RelativeMove(val x: Double, val y: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return x to y
    }
}

data class Line(val x: Double, val y: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return x to y
    }
}

data class RelativeLine(val x: Double, val y: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return x to y
    }
}

data class HorizontalLine(val x: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return x to Double.MAX_VALUE
    }
}

data class RelativeHorizontalLine(val x: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return x to Double.MAX_VALUE
    }
}

data class VerticleLine(val y: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return Double.MAX_VALUE to y
    }
}

data class RelativeVerticleLine(val y: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return Double.MAX_VALUE to y
    }
}

class ClosePath: SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return Double.MAX_VALUE to Double.MAX_VALUE
    }
}

data class CubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double, val x2: Double, val y2: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return listOf(x0, x1, x2).min()!! to listOf(y0, y1, y2).min()!!
    }
}

data class RelativeCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double, val x2: Double, val y2: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return listOf(x0, x1, x2).min()!! to listOf(y0, y1, y2).min()!!
    }
}

data class SmoothCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return listOf(x0, x1).min()!! to listOf(y0, y1).min()!!
    }
}

data class SmoothRelativeCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return listOf(x0, x1).min()!! to listOf(y0, y1).min()!!
    }
}

data class QuadraticeBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return listOf(x0, x1).min()!! to listOf(y0, y1).min()!!
    }
}

data class RelativeQuadraticeBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return listOf(x0, x1).min()!! to listOf(y0, y1).min()!!
    }
}

data class SmoothQuadraticeBezierCurve(val x: Double, val y: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return x to y
    }
}

data class SmoothRelativeQuadraticeBezierCurve(val x: Double, val y: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        return x to y
    }
}

data class Arc(val rx: Double, val ry: Double, val xRotation: Double, val largeArcFlag: Double,
               val sweepFlag: Double, val x: Double, val y: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

data class RelativeArc(val rx: Double, val ry: Double, val xRotation: Double, val largeArcFlag: Double,
                       val sweepFlag: Double, val x: Double, val y: Double): SVGCommand {
    override fun startPos(): Pair<Double, Double> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
