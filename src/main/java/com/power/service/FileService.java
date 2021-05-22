package com.power.service;

import com.power.domain.PartOfSpeech;
import com.power.domain.Word;
import com.power.repository.WordRepository;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class FileService {

    private final WordRepository repository;

    private static final List<String> PARTS_OF_SPEECH = Arrays.asList("Adjective", "Adverb", "Noun", "Verb", "Other");

    public FileService(WordRepository repository) {
        this.repository = repository;
    }

    public boolean writeWordsInSpreadsheetToDB(MultipartFile excelFile) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet worksheet = workbook.getSheetAt(i);

                // 2 because we ignore the headers
                for (int x = 2; x < worksheet.getPhysicalNumberOfRows(); x++) {
                    final Word word = convertColumnValuesToWord(worksheet, x, i);

                    if (null == word) {
                        break;
                    }

                    repository.save(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    private Word convertColumnValuesToWord(final XSSFSheet worksheet, int rowIndex, int sheetIndex) {
        XSSFRow row = worksheet.getRow(rowIndex);

        final Word word = new Word();

        if (row == null) {
            return null;
        }

        final XSSFCell theWordCell = row.getCell(0);
        if (null != theWordCell && !isBlank(theWordCell.getStringCellValue())) {
            word.setTheWord(theWordCell.getStringCellValue().trim());
        }

        final XSSFCell definitionCell = row.getCell(1);
        if (null != definitionCell) {
            String definition = definitionCell.getStringCellValue().toLowerCase(Locale.ROOT).trim();
            word.setDefinition(definition);
        }

        final XSSFCell originCell = row.getCell(2);
        if (null != originCell) {
            word.setOrigin(originCell.getStringCellValue().toLowerCase(Locale.ROOT).trim());
        }

        final String partOfSpeech = FileService.PARTS_OF_SPEECH.get(sheetIndex);
        word.setPartOfSpeech(PartOfSpeech.getPartOfSpeech(partOfSpeech));

        word.setCreatedBy("bp");
        word.setHaveLearnt(false);
        word.setTimesViewed(0);

        return word;
    }

}
