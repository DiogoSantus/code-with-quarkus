package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class QRCodeResourceTest {

    @Test
    public void testGenerateQRCode() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"data\": \"Test QR Code Data\"}")
                .when()
                .post("/qrcodes")
                .then()
                .statusCode(201);
    }

    @Test
    public void testGetAllQRCodeFiles() {
        given()
                .when()
                .get("/qrcodes")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }
}