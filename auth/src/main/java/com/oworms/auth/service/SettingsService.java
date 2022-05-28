package com.oworms.auth.service;

import com.oworms.auth.domain.AppSettings;
import com.oworms.auth.repository.SettingsRepository;
import com.oworms.auth.repository.UserRepository;
import com.oworms.common.error.OWormException;
import com.oworms.common.error.OWormExceptionType;
import com.oworms.common.util.LogUtil;
import com.oworms.common.util.Utils;
import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.dto.NewBnaDTO;
import com.oworms.mail.service.EmailService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class SettingsService {

    private final SettingsRepository repository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final Bucket bucket;

    @Value("${mail.banana.link}")
    private String eatBananaLink;

    public SettingsService(final SettingsRepository repository,
                           final UserRepository userRepository,
                           final EmailService emailService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.bucket = Bucket.builder().addLimit(Bandwidth.classic(5, Refill.greedy(5, Duration.ofDays(1)))).build();
    }

    @Transactional
    public void permit(String username, String bananaArg) {
        AppSettings settings = getSettings();

        if (!Utils.areEqual(bananaArg, settings.getBanana())) {
            LogUtil.log(OWormExceptionType.INSUFFICIENT_RIGHTS + " You cannot do that");

            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "You cannot do that");
        }

        userRepository
                .findByUsername(username)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "That user does not exist"));
    }

    @Transactional
    public void doWeeklyAdmin(String username, String bananaArg) {
        consumeToken("weekly admin");
        userRepository
                .findByUsername(username)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "That user does not exist"));

        AppSettings settings = repository.findAll().get(0);
        // if we do this then server cannot backup automatically
        // because it will always need the day's banana
//        if (!Utils.areEqual(settings.getBanana(), bananaArg)) {
//            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "You cannot do that");
//        }

        long noOfDaysBetween = settings.getLocalDate().until(LocalDateTime.now(), ChronoUnit.DAYS);
        if (noOfDaysBetween < 6) {
            // settings have been configured for the week. do not send mail
            return;
        }

        saveNewBna(); // configure settings for new week
    }

    @Transactional
    public AppSettings getSettings() {
        List<AppSettings> allSettings = repository.findAll();
        if (allSettings.isEmpty()) {
            throw new OWormException(OWormExceptionType.FAILURE, "A settings row must exist in the DB");
        }

        // will only ever be one
        return allSettings.get(0);

        // TODO: the saving of the new settings is rolled back if an exception is thrown.
        // calling weekly admin works because the service returns a 200
        // this one fails. saveAndFlush is no help either. re-implement this
        // settings are relevant for this week
//        long daysBetween = settings.getLocalDate().until(LocalDateTime.now(), ChronoUnit.DAYS);
//        if (daysBetween < 6) {
//            return settings;
//        }
//
//        // configure settings for new week
//        return saveNewBna();
    }

    private String sendNewBanana() {
        String uuid = UUID.randomUUID().toString();
        String timestamp = LocalDateTime.now().toString();
        String banana = uuid + ":" + timestamp;

        String eatLink = eatBananaLink.replace("{bna}", banana);
        NewBnaDTO newBan = new NewBnaDTO(banana, eatLink);
        newBan.setEatBananaLink(eatLink);

        emailService.sendNewBna(newBan);

        return banana;
    }

    private void consumeToken(String context) {
        if (!bucket.tryConsume(1)) {
            emailService.sendBucketOverflow(new BucketOverflowDTO(this.getClass().getName(), context));

            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }

    @Transactional
    public AppSettings saveNewBna() {
        String banana = sendNewBanana();
        AppSettings newSettings = new AppSettings(banana);

        repository.deleteAll();
        repository.saveAndFlush(newSettings);

        return newSettings;
    }
}