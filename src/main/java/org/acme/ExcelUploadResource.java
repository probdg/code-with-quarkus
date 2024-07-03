package org.acme;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@Path("/upload")
public class ExcelUploadResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(@MultipartForm FileUploadForm form) {
        try (InputStream inputStream = form.fileData) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            StringBuilder data = new StringBuilder();
            for (Row row : sheet) {
                for (Cell cell : row) {
                    data.append(cell.toString()).append("\t");
                }
                data.append("\n");
            }
            return Response.ok(data.toString()).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File processing failed").build();
        }
    }
}