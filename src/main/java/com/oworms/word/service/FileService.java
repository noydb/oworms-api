package com.oworms.word.service;

import com.oworms.auth.service.SettingsService;
import com.oworms.error.OWormException;
import com.oworms.error.OWormExceptionType;
import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.service.EmailService;
import com.oworms.word.domain.Tag;
import com.oworms.word.domain.Word;
import com.oworms.word.repository.TagRepository;
import com.oworms.word.repository.WordRepository;
import com.oworms.word.util.FileUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final WordRepository repository;
    private final TagRepository tagRepo;
    private final SettingsService ss;
    private final Bucket bucket;
    private final EmailService emailService;

    public FileService(final WordRepository repository,
                       final TagRepository tagRepo,
                       final SettingsService ss,
                       final EmailService emailService) {
        this.repository = repository;
        this.tagRepo = tagRepo;
        this.ss = ss;
        this.emailService = emailService;
        this.bucket = Bucket
                .builder()
                .addLimit(Bandwidth.classic(50, Refill.greedy(50, Duration.ofDays(1)))).build();
    }

    public InputStreamResource getWordsInCSV() {
        consumeToken("get csv");

        try {
            final File file = new File(FileUtil.getCSVName());
            final FileWriter fileWriter = new FileWriter(file);

            final String FILE_HEADERS = "word,definition,partOfSpeech,pronunciation,origin," +
                    "exampleUsage,note,tags,creationDate,createdBy,timesViewed";
            fileWriter.append(FILE_HEADERS).append("\n");

            List<Word> words = repository.findAll();
            for (Word word : words) {
                fileWriter.append(FileUtil.convertWordToRow(word));
            }

            fileWriter.flush();
            fileWriter.close();

            final InputStream input = new FileInputStream(file);

            return new InputStreamResource(input);
        } catch (IOException e) {
            throw new OWormException(
                    OWormExceptionType.CSV_WRITE_FAILURE,
                    "Error while writing words in database to CSV",
                    e.getMessage()
            );
        }
    }

    public void writeWordsInSpreadsheetToDB(final MultipartFile excelFile, final String u, final String banana) {
        consumeToken("write to db");
        ss.permit(u, banana);

        try {
            final XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
            final XSSFSheet sheet = workbook.getSheetAt(0);

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

    private List<Tag> getTags(final Word word) {
        final List<Tag> tags = new ArrayList<>();

        for (final Tag tag : word.getTags()) {
            final Optional<Tag> tagOpt = tagRepo.findByNameIgnoreCase(tag.getName());

            if (tagOpt.isPresent()) {
                tags.add(tagOpt.get());
            } else {
                tags.add(tagRepo.save(tag));
            }
        }

        return tags;
    }

    private boolean wordExists(final String theWord) {
        return repository.findByTheWordIgnoreCase(theWord).isPresent();
    }

    private void consumeToken(final String context) {
        if (!bucket.tryConsume(1)) {
            emailService.sendBucketOverflow(new BucketOverflowDTO(this.getClass().getName(), context));

            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}
