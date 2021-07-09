package com.power.controller;

import com.power.controller.api.FileAPI;
import com.power.service.FileService;
import com.power.util.FileUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/o/worms/file")
public class FileController implements FileAPI {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    @PostMapping(
            value = "/excel",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> readCSV(@RequestParam("excel_file") MultipartFile excelFile,
                                        @RequestParam(value = "permission_key") String permissionKey) {
        fileService.writeWordsInSpreadsheetToDB(excelFile, permissionKey);

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping(
            produces = MediaType.MULTIPART_FORM_DATA_VALUE,
            path = "/csv"
    )
    public ResponseEntity<Resource> getCSV() {
        InputStreamResource isResource = fileService.getWordsInCSV();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + FileUtil.getCSVName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(isResource);
    }
}
