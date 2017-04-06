package com.noser.dojo

import io.kotlintest.specs.StringSpec
import org.hamcrest.CoreMatchers.not


class MessagerTestStringSpec : StringSpec() {
    init {
        "Messager should return the correct Message" {
            Messager().getMessage() shouldBe "Hello World"
            Messager().getMessage() shouldBe not("Hello World2")
        }

        "Tabletest" {
            val myTable = table(
                    headers("a", "b", "result"),
                    row(1, 2, 3),
                    row(1, 1, 2)
            )
            forAll(myTable) { a, b, result ->
                a + b shouldBe result
            }
        }
    }


}
