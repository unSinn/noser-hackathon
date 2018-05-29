import io.restassured.specification.RequestSpecification

fun RequestSpecification.When(): RequestSpecification {
    return this.`when`()
}