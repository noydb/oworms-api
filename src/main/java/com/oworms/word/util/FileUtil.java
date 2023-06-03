package com.oworms.word.util;

import com.oworms.util.Utils;
import com.oworms.word.domain.PartOfSpeech;
import com.oworms.word.domain.Tag;
import com.oworms.word.domain.Word;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtil {

    public static Word convertRowToWord(final XSSFSheet worksheet, int rowIndex) {
        XSSFRow row = worksheet.getRow(rowIndex);

        final Word word = new Word();

        if (row == null) {
            return null;
        }

        //****** column order follows Word class field order ******//

        final String theWord = FileUtil.getCellStringValue(row.getCell(0));
        if (Utils.isBlank(theWord)) {
            return null; // parser sometimes detects blank rows. we kill it here if that happens
        }

        word.setTheWord(theWord);

        final String definition = FileUtil.getCellStringValue(row.getCell(1));
        word.setDefinition(definition);

        final String partOfSpeech = FileUtil.getCellStringValue(row.getCell(2));
        word.setPartOfSpeech(PartOfSpeech.getPartOfSpeech(partOfSpeech));

        final String pronunciation = FileUtil.getCellStringValue(row.getCell(3));
        word.setPronunciation(pronunciation);

        final String origin = FileUtil.getCellStringValue(row.getCell(4));
        word.setOrigin(origin);

        final String exampleUsage = FileUtil.getCellStringValue(row.getCell(5));
        word.setExampleUsage(exampleUsage);

        final String note = FileUtil.getCellStringValue(row.getCell(6));
        word.setNote(note);

        final String tagString = FileUtil.getCellStringValue(row.getCell(7));
        word.setTags(FileUtil.getTags(tagString));

        final String creationDate = row.getCell(8).getStringCellValue();
        if (creationDate != null) {
            word.setCreationDate(OffsetDateTime.parse(creationDate));
        }

        final String createdBy = FileUtil.getCellStringValue(row.getCell(9));
        word.setCreatedBy(createdBy);

        final double timesViewed = row.getCell(10).getNumericCellValue();
        word.setTimesViewed((int) timesViewed);

        return word;
    }

    public static String convertWordToRow(Word word) {
        StringBuilder tagS = new StringBuilder();

        List<Tag> tags = word.getTags();
        for (Tag tag : tags) {
            tagS.append(tag.getName()).append("-");
        }

        return String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                getRowValue(word.getTheWord()),
                getRowValue(word.getDefinition()),
                word.getPartOfSpeech().getLabel(),
                getRowValue(word.getPronunciation()),
                getRowValue(word.getOrigin()),
                getRowValue(word.getExampleUsage()),
                getRowValue(word.getNote()),
                tagS,
                getRowValue(word.getCreationDate().toString()),
                getRowValue(word.getCreatedBy()),
                word.getTimesViewed()
        );
    }

    private static String getRowValue(String value) {
        if (Utils.isBlank(value)) {
            return "";
        }

        return Utils.wrapTextIfCommaPresent(value);
    }

    public static String getCSVName() {
        return "words-backup-" + getBackupTimestamp();
    }

    public static String getBackupTimestamp() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return LocalDate.now().format(format);
    }

    private static List<Tag> getTags(String tagString) {
        if (Utils.isBlank(tagString)) {
            return Collections.emptyList();
        }

        String[] tagNames = tagString.split("-");

        List<Tag> tags = new ArrayList<>();

        for (String tagName : tagNames) {
            tags.add(new Tag(tagName.toLowerCase().trim()));
        }

        return tags;
    }

    private static String getCellStringValue(XSSFCell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                double doubleVal = cell.getNumericCellValue();
                return String.valueOf(doubleVal);
            case STRING:
                return cell.getStringCellValue();
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            case BLANK:
                return "";
            case FORMULA:
                return cell.getCellFormula();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "error getting string from cell";
        }
    }
}
