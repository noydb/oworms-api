package com.power.oworms.mail.service;

import com.power.oworms.common.error.OWormException;
import com.power.oworms.common.error.OWormExceptionType;
import com.power.oworms.mail.config.MailContentBuilder;
import com.power.oworms.mail.config.MailProperties;
import com.power.oworms.mail.dto.BucketOverflowDTO;
import com.power.oworms.mail.dto.DailyReportDTO;
import com.power.oworms.mail.dto.EmailWordDTO;
import com.power.oworms.mail.dto.NewWordEmailDTO;
import com.power.oworms.mail.dto.UpdatedWordEmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;
    private final MailProperties properties;
    private static final String BOT = "bot@oworms.com";
    private static final String ENCODING = "UTF-8";

    @Autowired
    public EmailService(final JavaMailSender mailSender,
                        final MailContentBuilder mailContentBuilder,
                        final MailProperties properties) {
        this.mailSender = mailSender;
        this.mailContentBuilder = mailContentBuilder;
        this.properties = properties;
    }

    public void sendDailyReport(DailyReportDTO dailyReport) {
        if (properties.isDisabled()) {
            return;
        }

        String[] recipients = properties.getRecipients().split(",");

        MimeMessagePreparator messagePrep = (MimeMessage mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);

            messageHelper.setFrom(BOT);
            messageHelper.setTo(recipients[0]);
            messageHelper.setSubject(dailyReport.getTitle());
            messageHelper.setBcc(recipients);

            String messageContent = mailContentBuilder.build(dailyReport, DailyReportDTO.TEMPLATE);

            messageHelper.setText(messageContent, true);
        };

        try {
            mailSender.send(messagePrep);
        } catch (MailException e) {
            throw new OWormException(OWormExceptionType.EMAIL_SEND_FAILURE, "Failed to send report email");
        }
    }

    public void sendBucketOverflow(BucketOverflowDTO bucketOverflowDTO) {
        if (properties.isDisabled()) {
            return;
        }

        String[] recipients = properties.getRecipients().split(",");

        MimeMessagePreparator messagePrep = (MimeMessage mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);

            messageHelper.setFrom(BOT);
            messageHelper.setTo(recipients[0]);
            messageHelper.setSubject(bucketOverflowDTO.getTitle());
            messageHelper.setBcc(recipients);

            String messageContent = mailContentBuilder.build(bucketOverflowDTO, BucketOverflowDTO.TEMPLATE);

            messageHelper.setText(messageContent, true);
        };

        try {
            mailSender.send(messagePrep);
        } catch (MailException e) {
            throw new OWormException(OWormExceptionType.EMAIL_SEND_FAILURE, "Failed to send report email");
        }
    }

    public void sendNewWordEmail(String title, EmailWordDTO wordDTO) {
        if (properties.isDisabled()) {
            return;
        }

        NewWordEmailDTO newWordEmailDTO = getNewWordEmailDTO(title, wordDTO);

        String[] recipients = properties.getRecipients().split(",");

        MimeMessagePreparator messagePrep = (MimeMessage mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);

            messageHelper.setFrom(BOT);
            messageHelper.setTo(recipients[0]);
            messageHelper.setSubject(title);
            messageHelper.setBcc(recipients);

            String messageContent = mailContentBuilder.build(newWordEmailDTO, NewWordEmailDTO.TEMPLATE);

            messageHelper.setText(messageContent, true);
        };

        try {
            mailSender.send(messagePrep);
        } catch (MailException e) {
            throw new OWormException(OWormExceptionType.EMAIL_SEND_FAILURE, "Failed to send new word email");
        }
    }

    private NewWordEmailDTO getNewWordEmailDTO(String title, EmailWordDTO wordDTO) {
        String retrievalLink = properties.getRetrievalLink().replace("{id}", String.valueOf(wordDTO.getId()));

        return new NewWordEmailDTO(title, wordDTO, retrievalLink);
    }

    public void sendUpdateWordEmail(String title, EmailWordDTO oldWord, EmailWordDTO updatedWord) {
        if (properties.isDisabled()) {
            return;
        }

        String[] recipients = properties.getRecipients().split(",");

        MimeMessagePreparator messagePrep = (MimeMessage mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);

            messageHelper.setFrom(BOT);
            messageHelper.setTo(recipients[0]);
            messageHelper.setSubject(title);
            messageHelper.setBcc(recipients);

            String messageContent = mailContentBuilder.build(
                    getUpdateWordEmailDTO(title, oldWord, updatedWord),
                    UpdatedWordEmailDTO.TEMPLATE
            );

            messageHelper.setText(messageContent, true);
        };

        try {
            mailSender.send(messagePrep);
        } catch (MailException e) {
            throw new OWormException(OWormExceptionType.EMAIL_SEND_FAILURE, "Failed to send update word email");
        }
    }

    private UpdatedWordEmailDTO getUpdateWordEmailDTO(String title, EmailWordDTO oldWord, EmailWordDTO updatedWord) {
        String retrievalLink = properties.getRetrievalLink().replace("{id}", String.valueOf(oldWord.getId()));

        return new UpdatedWordEmailDTO(title, oldWord, updatedWord, retrievalLink);
    }
}
