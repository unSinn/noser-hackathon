package com.noser.hackathon

import When
import com.noser.hackathon.Config.GENOSSEN
import com.noser.hackathon.server.Match
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType.JSON
import org.junit.Test


class IntegrationTest {

    val ENEMY_NAME = "bot_THE_WEST"


    private var christoph = RequestSpecBuilder()
            .setContentType(JSON)
            .setBaseUri("https://noser-connect-four.azurewebsites.net/api/connect-four")
            // .setBaseUri("http://localhost:8080/api/connect-four")
            .addFilter(ResponseLoggingFilter())//log request and response for better debugging. You can also only log if a requests fails.
            .addFilter(RequestLoggingFilter())
            .build()

    private var genosse = RequestSpecBuilder()
            .setContentType(JSON)
            .setBaseUri("http://localhost:8000/")
            .addFilter(ResponseLoggingFilter())//log request and response for better debugging. You can also only log if a requests fails.
            .addFilter(RequestLoggingFilter())
            .build()

    @Test
    fun integrationTest() {

        deleteBoards()

        createBoards()

        // startPlaying()


    }

    private fun startPlaying() {
        given()
                .spec(genosse)
                .param("enemy", ENEMY_NAME)
                .When()
                .put("start")
                .then()
                .statusCode(200)
    }

    private fun createBoards() {
        given()
                .spec(christoph)
                .body(Match(0, GENOSSEN, ENEMY_NAME, 10))
                .When()
                .post("match")
                .then()
                .statusCode(200)
    }

    private fun deleteBoards() {
        given()
                .spec(christoph)
                .body("i want to delete everything")
                .When()
                .delete("boards")
                .then()
                .statusCode(204)
    }
}