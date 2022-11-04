package com.oworms.mail.service;

import com.oworms.common.error.OWormException;
import com.oworms.common.error.OWormExceptionType;
import com.oworms.mail.config.MailContentBuilder;
import com.oworms.mail.config.MailProperties;
import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.dto.EmailWordDTO;
import com.oworms.mail.dto.NewBnaDTO;
import com.oworms.mail.dto.NewWordEmailDTO;
import com.oworms.mail.dto.UpdatedWordEmailDTO;
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
    private static final String UUID = "{uuid}";

    @Autowired
    public EmailService(final JavaMailSender mailSender,
                        final MailContentBuilder mailContentBuilder,
                        final MailProperties properties) {
        this.mailSender = mailSender;
        this.mailContentBuilder = mailContentBuilder;
        this.properties = properties;
    }

    public void sendNewBna(NewBnaDTO newBan) {
        if (properties.isDisabled()) {
            return;
        }

        String[] recipients = properties.getRecipients().split(",");

        MimeMessagePreparator messagePrep = (MimeMessage mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);

            messageHelper.setFrom(BOT);
            messageHelper.setTo(recipients[0]);
            messageHelper.setSubject(newBan.getTitle());
            messageHelper.setBcc(recipients);

            String messageContent = mailContentBuilder.build(newBan, NewBnaDTO.TEMPLATE);

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

    public void sendNewWordEmail(final String title, final EmailWordDTO wordDTO) {
        if (properties.isDisabled()) {
            return;
        }

        final NewWordEmailDTO newWordEmailDTO = getNewWordEmailDTO(title, wordDTO);

        MimeMessagePreparator messagePrep = (MimeMessage mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);

            messageHelper.setFrom(BOT);
            messageHelper.setTo(wordDTO.getRecipients());
            messageHelper.setSubject(title);
            messageHelper.setBcc(wordDTO.getRecipients());

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
        String retrievalLink = properties.getRetrievalLink().replace(UUID, wordDTO.getUuid());
        String editLink = properties.getEditLink().replace(UUID, wordDTO.getUuid());

        NewWordEmailDTO newWordEmailDTO = new NewWordEmailDTO(title, wordDTO, retrievalLink);
        newWordEmailDTO.setEditLink(editLink);

        return newWordEmailDTO;
    }

    public void sendUpdateWordEmail(UpdatedWordEmailDTO updatedWordEmail) {
        if (properties.isDisabled()) {
            return;
        }

        final String retrievalLink = properties.getRetrievalLink().replace(UUID, updatedWordEmail.getOld().getUuid());
        updatedWordEmail.setRetrievalLink(retrievalLink);

        final String editLink = properties.getEditLink().replace(UUID, updatedWordEmail.getOld().getUuid());
        updatedWordEmail.setEditLink(editLink);

        String[] recipients = properties.getRecipients().split(",");

        MimeMessagePreparator messagePrep = (MimeMessage mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, ENCODING);

            messageHelper.setFrom(BOT);
            messageHelper.setTo(recipients[0]);
            messageHelper.setSubject(updatedWordEmail.getTitle());
            messageHelper.setBcc(recipients);

            String messageContent = mailContentBuilder.build(updatedWordEmail, UpdatedWordEmailDTO.TEMPLATE);

            messageHelper.setText(messageContent, true);
        };

        try {
            mailSender.send(messagePrep);
        } catch (MailException e) {
            throw new OWormException(OWormExceptionType.EMAIL_SEND_FAILURE, "Failed to send update word email");
        }
    }
}
