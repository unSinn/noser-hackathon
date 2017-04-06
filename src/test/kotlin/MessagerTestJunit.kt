package com.noser.dojo


import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.startsWith
import org.junit.Test


class MessagerTestJunit {

    @Test
    fun getHelloWorldMessage() {
        assertThat(Messager().getMessage(), startsWith("Hello"))
    }

}