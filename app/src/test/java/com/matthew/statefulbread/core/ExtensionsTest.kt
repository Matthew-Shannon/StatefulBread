package com.matthew.statefulbread.core

import org.junit.Assert
import org.junit.Test

class ExtensionsTest: BaseTest() {

    @Test fun tag() {
        val tag = TAG
        Assert.assertEquals("ASDF ExtensionsTest", tag)
    }

}