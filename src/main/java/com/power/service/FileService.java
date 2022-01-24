package com.power.service;

import com.power.domain.Tag;
import com.power.domain.Word;
import com.power.error.OWormException;
import com.power.error.OWormExceptionType;
import com.power.repository.TagRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final WordRepository repository;
    private final HelperService helperService;
    private final TagRepository tagRepo;

    public FileService(final WordRepository repository,
                       final HelperService helperService,
                       final TagRepository tagRepo) {
        this.repository = repository;
        this.helperService = helperService;
        this.tagRepo = tagRepo;
    }

    public InputStreamResource getWordsInCSV() {
        try {
            File file = new File(FileUtil.getCSVName());
            FileWriter fileWriter = new FileWriter(file);

            final String FILE_HEADERS = "word,definition,partOfSpeech,pronunciation,origin," +
                    "exampleUsage,note,tags,creationDate,createdBy,timesViewed";
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

                word.setTags(getTags(word));

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

    private List<Tag> getTags(Word word) {
        List<Tag> tags = new ArrayList<>();

        for (Tag tag : word.getTags()) {
            Optional<Tag> tagOpt = tagRepo.findByNameIgnoreCase(tag.getName());

            if (tagOpt.isPresent()) {
                tags.add(tagOpt.get());
            } else {
                tags.add(tagRepo.save(tag));
            }
        }

        return tags;
    }

    private boolean wordExists(String theWord) {
        return repository.findByTheWordIgnoreCase(theWord).isPresent();
    }
}
