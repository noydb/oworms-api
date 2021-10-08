package com.power.service;

import com.power.domain.Word;
import com.power.error.OWormException;
import com.power.error.OWormExceptionType;
import com.power.repository.WordRepository;
import com.power.util.FileUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {

    private final WordRepository repository;
    private final HelperService helperService;

    public FileService(final WordRepository repository,
                       final HelperService helperService) {
        this.repository = repository;
        this.helperService = helperService;
    }

    public InputStreamResource getWordsInCSV() {
        try {
            File file = new File(FileUtil.getCSVName());
            FileWriter fileWriter = new FileWriter(file);

            final String FILE_HEADERS = "word,definition,partOfSpeech,pronunciation,origin," +
                    "exampleUsage,note,haveLearnt,creationDate,createdBy,timesViewed";
            fileWriter.append(FILE_HEADERS).append("\n");

            List<Word> words = repository.findAll();
            for (Word word : words) {
                fileWriter.append(FileUtil.convertWordToRow(word));
            }

            fileWriter.flush();
            fileWriter.close();

            InputStream input = new FileInputStream(file);

            return new InputStreamResource(input);
        } catch (IOException e) {
            throw new OWormException(
                    OWormExceptionType.CSV_WRITE_FAILURE,
                    "Error while writing words in database to CSV",
                    e.getMessage()
            );
        }
    }

    public void writeWordsInSpreadsheetToDB(MultipartFile excelFile, String permissionKey) {
        helperService.checkPermission(permissionKey);

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);

            // 1 because we ignore the headers
            for (int x = 1; x < sheet.getPhysicalNumberOfRows(); x++) {
                final Word word = FileUtil.convertRowToWord(sheet, x);

                if (null == word) {
                    break;
                }

                // if a word already exists it will be skipped
                if (!wordExists(word.getTheWord())) {
                    repository.save(word);
                }
            }
        } catch (IOException e) {
            throw new OWormException(
                    OWormExceptionType.CSV_READ_FAILURE,
                    "Error while writing words in spreadsheet to database",
                    e.getMessage()
            );
        }
    }

    private boolean wordExists(String theWord) {
        return repository.findByTheWordIgnoreCase(theWord).isPresent();
    }
}
