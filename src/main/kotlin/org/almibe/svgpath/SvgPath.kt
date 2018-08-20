/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.svgpath

class SvgPath {
}

interface SVGCommand

data class Move(val x: Double, val y: Double): SVGCommand
data class RelativeMove(val x: Double, val y: Double): SVGCommand

data class Line(val x: Double, val y: Double): SVGCommand
data class RelativeLine(val x: Double, val y: Double): SVGCommand

data class HorizontalLine(val x: Double): SVGCommand
data class RelativeHorizontalLine(val x: Double): SVGCommand

data class VerticleLine(val y: Double): SVGCommand
data class RelativeVerticleLine(val y: Double): SVGCommand

class ClosePath(): SVGCommand

data class CubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double, val x2: Double, val y2: Double): SVGCommand
data class RelativeCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double, val x2: Double, val y2: Double): SVGCommand

data class SmoothCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand
data class SmoothRelativeCubicBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand

data class QuadraticeBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand
data class RelativeQuadraticeBezierCurve(val x0: Double, val y0: Double, val x1: Double, val y1: Double): SVGCommand

data class SmoothQuadraticeBezierCurve(val x0: Double, val y0: Double): SVGCommand
data class SmoothRelativeQuadraticeBezierCurve(val x0: Double, val y0: Double): SVGCommand

data class Arc(val rx: Double, val ry: Double, val xRotation: Double, val largeArcFlag: Double,
               val sweepFlag: Double, val x: Double, val y: Double): SVGCommand
data class RelativeArc(val rx: Double, val ry: Double, val xRotation: Double, val largeArcFlag: Double,
                       val sweepFlag: Double, val x: Double, val y: Double): SVGCommand
