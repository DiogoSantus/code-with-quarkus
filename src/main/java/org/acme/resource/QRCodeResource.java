package org.acme.resource;

import com.google.zxing.WriterException;
import org.acme.qrcodeservice.QRCode;
import org.acme.qrcodeservice.QRCodeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;


@Path("/qrcodes")
public class QRCodeResource {

    @Inject
    QRCodeService qrCodeService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateQRCode(QRCode qrCodeData) {
        qrCodeService.createQRCode(qrCodeData.getData(), 400, 400)
                .subscribe().with(
                        fileName -> System.out.println("QR code generated and saved as: " + fileName),
                        failure -> System.err.println("Failed to generate QR code: " + failure.getMessage())
                );
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Produces("application/json")
    public Response getAllQRCodeFiles() {
        List<String> generatedQRCodes = qrCodeService.getAllQRCodeFiles()
                .await().indefinitely();
        return Response.ok(generatedQRCodes).build();
    }

}