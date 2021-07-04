package com.power.service;

import com.power.dto.NewWordEmailDTO;
import com.power.dto.WordDTO;
import com.power.mail.MailContentBuilder;
import com.power.mail.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public EmailService(final JavaMailSender mailSender,
                        final MailContentBuilder mailContentBuilder,
                        final MailProperties properties) {
        this.mailSender = mailSender;
        this.mailContentBuilder = mailContentBuilder;
        this.properties = properties;
    }

    public void sendNewWordEmail(WordDTO wordDTO) {
        NewWordEmailDTO newWordEmail = new NewWordEmailDTO();
        newWordEmail.setWord(wordDTO.getTheWord());
        newWordEmail.setDefinition(wordDTO.getDefinition());
        newWordEmail.setPartOfSpeech(wordDTO.getPartOfSpeech());

        newWordEmail.setRecipients(new String[]{});

        String retrievalLink = properties.getRetrievalLink().replace("{theWord}", wordDTO.getTheWord());
        newWordEmail.setRetrievalLink(retrievalLink);

        sendEmail(newWordEmail);
    }

    private void sendEmail(NewWordEmailDTO newWordEmail) {
        if (properties.isDisabled()) {
            return;
        }

        String[] recipients = properties.getRecipients().split(",");

        MimeMessagePreparator messagePrep = (MimeMessage mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            messageHelper.setFrom("bot@oworms.com");
            messageHelper.setTo(recipients[0]);
            messageHelper.setSubject(NewWordEmailDTO.SUBJECT);
            messageHelper.setBcc(recipients);

            String messageContent = mailContentBuilder.build(newWordEmail, NewWordEmailDTO.TEMPLATE);

            messageHelper.setText(messageContent, true);
        };

        mailSender.send(messagePrep);
    }

}
