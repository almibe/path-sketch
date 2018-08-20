/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.svgpath

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class SvgPathSpec : StringSpec({
    val parser = SVGPathParser()

    "read move to" {
        val result = parser.parse("M 0.0, 1.0")
        result.commands.first() shouldBe Move(0.0, 1.0)
        result.commands.size shouldBe 1
    }

    "read rel move to" {
        val result = parser.parse("m 2.0,1.0 ")
        result.commands.first() shouldBe RelativeMove(2.0, 1.0)
        result.commands.size shouldBe 1
    }

    "test repeating command" {
        val result = parser.parse("m 2.0,1.0 L 0.1 2.0 4.0,6.6")
        result.commands[0] shouldBe RelativeMove(2.0, 1.0)
        result.commands[1] shouldBe Line(0.1, 2.0)
        result.commands[2] shouldBe Line(4.0, 6.6)
        result.commands.size shouldBe 3
    }
})
