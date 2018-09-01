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

interface SVGCommand {
    fun pathDimensions(): PathDimensions
    fun translate(right: Double, down: Double): SVGCommand
    fun scale(scaleBy: Double): SVGCommand
    fun toPathString(): String
}

data class Move(val x: Double, val y: Double): SVGCommand {
    override fun toPathString(): String = "M $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = Move(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = Move(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class RelativeMove(val x: Double, val y: Double): SVGCommand {
    override fun toPathString(): String = "m $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeMove(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = RelativeMove(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class Line(val x: Double, val y: Double): SVGCommand {
    override fun toPathString(): String = "L $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = Line(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = Line(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class RelativeLine(val x: Double, val y: Double): SVGCommand {
    override fun toPathString(): String = "l $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeLine(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = RelativeLine(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class HorizontalLine(val x: Double): SVGCommand {
    override fun toPathString(): String = "H $x"
    override fun translate(right: Double, down: Double): SVGCommand = HorizontalLine(x + right)
    override fun scale(scaleBy: Double): SVGCommand = HorizontalLine(x * scaleBy)
    override fun pathDimensions() = PathDimensions(x, Double.MAX_VALUE, x, Double.MIN_VALUE)
}

data class RelativeHorizontalLine(val x: Double): SVGCommand {
    override fun toPathString(): String = "h $x"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeHorizontalLine(x + right)
    override fun scale(scaleBy: Double): SVGCommand = RelativeHorizontalLine(x * scaleBy)
    override fun pathDimensions() = PathDimensions(x, Double.MAX_VALUE, x, Double.MIN_VALUE)
}

data class VerticleLine(val y: Double): SVGCommand {
    override fun toPathString(): String = "V $y"
    override fun translate(right: Double, down: Double): SVGCommand = VerticleLine(y + down)
    override fun scale(scaleBy: Double): SVGCommand = VerticleLine(y * scaleBy)
    override fun pathDimensions() = PathDimensions(Double.MAX_VALUE, y, Double.MIN_VALUE, y)
}

data class RelativeVerticleLine(val y: Double): SVGCommand {
    override fun toPathString(): String = "v $y"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeVerticleLine(y + down)
    override fun scale(scaleBy: Double): SVGCommand = RelativeVerticleLine(y * scaleBy)
    override fun pathDimensions() = PathDimensions(Double.MAX_VALUE, y, Double.MIN_VALUE, y)
}

class ClosePath: SVGCommand {
    override fun toPathString(): String = "z"
    override fun translate(right: Double, down: Double): SVGCommand = ClosePath()
    override fun scale(scaleBy: Double): SVGCommand = ClosePath()
    override fun pathDimensions() = PathDimensions(Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE)
}

data class CubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double, val x2: Double, val y2: Double): SVGCommand {
    override fun toPathString(): String = "C $x0 $y0 $x1 $y1 $x2 $y2"
    override fun translate(right: Double, down: Double): SVGCommand = CubicBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down, x2 + right, y2 + down)
    override fun scale(scaleBy: Double): SVGCommand = CubicBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy, x2 * scaleBy, y2 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1, x2).min()!!,listOf(y0, y1, y2).min()!!,
                listOf(x0, x1, x2).max()!!,listOf(y0, y1, y2).max()!!)
}

data class RelativeCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double, val x2: Double, val y2: Double): SVGCommand {
    override fun toPathString(): String = "c $x0 $y0 $x1 $y1 $x2 $y2"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeCubicBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down, x2 + right, y2 + down)
    override fun scale(scaleBy: Double): SVGCommand = RelativeCubicBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy, x2 * scaleBy, y2 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1, x2).min()!!,listOf(y0, y1, y2).min()!!,
            listOf(x0, x1, x2).max()!!,listOf(y0, y1, y2).max()!!)
}

data class SmoothCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand {
    override fun toPathString(): String = "S $x0 $y0 $x1 $y1"
    override fun translate(right: Double, down: Double): SVGCommand = SmoothCubicBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down)
    override fun scale(scaleBy: Double): SVGCommand = SmoothCubicBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1).min()!!,listOf(y0, y1).min()!!,
            listOf(x0, x1).max()!!,listOf(y0, y1).max()!!)
}

data class SmoothRelativeCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand {
    override fun toPathString(): String = "s $x0 $y0 $x1 $y1"
    override fun translate(right: Double, down: Double): SVGCommand = SmoothRelativeCubicBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down)
    override fun scale(scaleBy: Double): SVGCommand = SmoothRelativeCubicBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1).min()!!,listOf(y0, y1).min()!!,
            listOf(x0, x1).max()!!,listOf(y0, y1).max()!!)
}

data class QuadraticeBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand {
    override fun toPathString(): String = "Q $x0 $y0 $x1 $y1"
    override fun translate(right: Double, down: Double): SVGCommand = QuadraticeBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down)
    override fun scale(scaleBy: Double): SVGCommand = QuadraticeBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1).min()!!,listOf(y0, y1).min()!!,
            listOf(x0, x1).max()!!,listOf(y0, y1).max()!!)
}

data class RelativeQuadraticeBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand {
    override fun toPathString(): String = "q $x0 $y0 $x1 $y1"
    override fun translate(right: Double, down: Double): SVGCommand = RelativeQuadraticeBezierCurve(x0 + right, y0 + down, x1 + right, y1 + down)
    override fun scale(scaleBy: Double): SVGCommand = RelativeQuadraticeBezierCurve(x0 * scaleBy, y0 * scaleBy, x1 * scaleBy, y1 * scaleBy)
    override fun pathDimensions() = PathDimensions(listOf(x0, x1).min()!!,listOf(y0, y1).min()!!,
            listOf(x0, x1).max()!!,listOf(y0, y1).max()!!)
}

data class SmoothQuadraticeBezierCurve(val x: Double, val y: Double): SVGCommand {
    override fun toPathString(): String = "T $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = SmoothQuadraticeBezierCurve(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = SmoothQuadraticeBezierCurve(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class SmoothRelativeQuadraticeBezierCurve(val x: Double, val y: Double): SVGCommand {
    override fun toPathString(): String = "t $x $y"
    override fun translate(right: Double, down: Double): SVGCommand = SmoothRelativeQuadraticeBezierCurve(x + right, y + down)
    override fun scale(scaleBy: Double): SVGCommand = SmoothRelativeQuadraticeBezierCurve(x * scaleBy, y * scaleBy)
    override fun pathDimensions() = PathDimensions(x, y, x, y)
}

data class Arc(val rx: Double, val ry: Double, val xRotation: Double, val largeArcFlag: Double,
               val sweepFlag: Double, val x: Double, val y: Double): SVGCommand {
    override fun toPathString(): String = TODO()
    override fun translate(right: Double, down: Double): SVGCommand = TODO()
    override fun scale(scaleBy: Double): SVGCommand = TODO()
    override fun pathDimensions() = TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

data class RelativeArc(val rx: Double, val ry: Double, val xRotation: Double, val largeArcFlag: Double,
                       val sweepFlag: Double, val x: Double, val y: Double): SVGCommand {
    override fun toPathString(): String = TODO()
    override fun translate(right: Double, down: Double): SVGCommand = TODO()
    override fun scale(scaleBy: Double): SVGCommand = TODO()
    override fun pathDimensions() = TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
