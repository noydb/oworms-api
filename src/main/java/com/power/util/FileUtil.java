package com.power.util;

import com.power.domain.PartOfSpeech;
import com.power.domain.Word;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtil {

    public static Word convertRowToWord(final XSSFSheet worksheet, int rowIndex) {
        XSSFRow row = worksheet.getRow(rowIndex);

        final Word word = new Word();

        if (row == null) {
            return null;
        }

        //****** column order follows Word class field order ******//

        final String theWord = FileUtil.getCellValue(row.getCell(0));
        word.setTheWord(theWord);

        final String definition = FileUtil.getCellValue(row.getCell(1));
        word.setDefinition(definition);

        final String partOfSpeech = FileUtil.getCellValue(row.getCell(2));
        word.setPartOfSpeech(PartOfSpeech.getPartOfSpeech(partOfSpeech));

        final String pronunciation = FileUtil.getCellValue(row.getCell(3));
        word.setPronunciation(pronunciation);

        final String origin = FileUtil.getCellValue(row.getCell(4));
        word.setOrigin(origin);

        final String exampleUsage = FileUtil.getCellValue(row.getCell(5));
        word.setExampleUsage(exampleUsage);

        final boolean haveLearnt = row.getCell(6).getBooleanCellValue();
        word.setHaveLearnt(haveLearnt);

        final LocalDateTime creationDate = row.getCell(7).getLocalDateTimeCellValue();
        if (creationDate == null) {
            // doesn't set time....
            word.setCreationDate(LocalDateTime.now());
        } else {
            word.setCreationDate(creationDate);
        }

        final String createdBy = FileUtil.getCellValue(row.getCell(8));
        word.setCreatedBy(createdBy);

        final double timesViewed = row.getCell(9).getNumericCellValue();
        word.setTimesViewed((int) timesViewed);

        return word;
    }

    public static String convertWordToRow(Word word) {
        return String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                getRowValue(word.getTheWord()),
                WordUtil.wrapTextIfCommaPresent(word.getDefinition()),
                word.getPartOfSpeech().getLabel(),
                getRowValue(word.getPronunciation()),
                getRowValue(word.getOrigin()),
                getRowValue(word.getExampleUsage()),
                word.isHaveLearnt(),
                word.getCreationDate().toString(),
                getRowValue(word.getCreatedBy()),
                word.getTimesViewed()
        );
    }

    private static String getRowValue(String value) {
        if (WordUtil.isBlank(value)) {
            return "";
        }

        return WordUtil.wrapTextIfCommaPresent(value);
    }

    public static String getCSVName() {
        return "words-backup-" + getBackupTimestamp();
    }

    public static String getBackupTimestamp() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return LocalDate.now().format(format);
    }

    public static String getCellValue(final XSSFCell cell) {
        if (cell == null || WordUtil.isBlank(cell.getStringCellValue())) {
            return null;
        }

        return cell.getStringCellValue();
    }

}
