package com.oworms.word.controller;

import com.oworms.word.controller.api.FileAPI;
import com.oworms.util.LogUtil;
import com.oworms.word.service.FileService;
import com.oworms.word.util.FileUtil;
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

    @PostMapping(
            value = "/excel",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> readExcel(@RequestParam("excel_file") MultipartFile excelFile,
                                          @RequestParam("u") String u,
                                          @RequestParam("bna") String banana) {
        LogUtil.log("Parsing excel");

        fileService.writeWordsInSpreadsheetToDB(excelFile, u, banana);

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping(
            produces = MediaType.MULTIPART_FORM_DATA_VALUE,
            path = "/csv"
    )
    public ResponseEntity<Resource> getCSV() {
        LogUtil.log("Generating CSV");

        InputStreamResource isResource = fileService.getWordsInCSV();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + FileUtil.getCSVName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(isResource);
    }
}
