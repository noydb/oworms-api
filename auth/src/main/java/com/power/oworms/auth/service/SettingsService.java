package com.power.oworms.auth.service;

import com.power.oworms.auth.domain.AppSettings;
import com.power.oworms.auth.repository.SettingsRepository;
import com.power.oworms.auth.repository.UserRepository;
import com.power.oworms.common.error.OWormException;
import com.power.oworms.common.error.OWormExceptionType;
import com.power.oworms.common.util.Utils;
import com.power.oworms.mail.dto.DailyReportDTO;
import com.power.oworms.mail.service.EmailService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        this.bucket = Bucket.builder().addLimit(Bandwidth.classic(100, Refill.greedy(100, Duration.ofDays(1)))).build();
    }

    public void permit(String username, String bananaArg) {
        AppSettings settings = getSettings();

        if (!Utils.areEqual(bananaArg, settings.getBanana())) {
            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "You cannot do that");
        }

        userRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "That user does not exist"));
    }

    public void doDailyAdmin(String username, String bananaArg) {
        consumeToken();
        userRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "That user does not exist"));

        AppSettings settings = repository.findAll().get(0);
        if (!Utils.areEqual(settings.getBanana(), bananaArg)) {
            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "You cannot do that");
        }

        LocalDate now = LocalDate.now();
        if (now.isEqual(settings.getDateTime().toLocalDate())) {
            // settings have been configured for today already. do not send mail
            return;
        }

        String bananaEnc = sendNewBanana();
        settings.setBanana(bananaEnc);
        settings.setDateTime(LocalDateTime.now());

        repository.save(settings);
    }

    private AppSettings getSettings() {
        List<AppSettings> allSettings = repository.findAll();
        if (allSettings.isEmpty()) {
            throw new OWormException(OWormExceptionType.FAILURE, "A settings row must exist in the DB");
        }

        // will only ever be one
        AppSettings settings = allSettings.get(0);

        // settings are relevant for today, return it
        LocalDate now = LocalDate.now();
        if (now.isEqual(settings.getDateTime().toLocalDate())) {
            return settings;
        }

        // configure settings for new day
        String banana = sendNewBanana();
        settings.setBanana(banana);
        settings.setDateTime(LocalDateTime.now());

        repository.save(settings);

        return settings;
    }

    private void consumeToken() {
        if (!bucket.tryConsume(1)) {
            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }

    private String sendNewBanana() {
        String uuid = UUID.randomUUID().toString();
        String timestamp = LocalDateTime.now().toString();
        String banana = uuid + ":" + timestamp;

        String eatLink = eatBananaLink.replace("{bna}", banana);
        DailyReportDTO dailyReport = new DailyReportDTO(banana, eatLink);
        dailyReport.setEatBananaLink(eatLink);

        emailService.sendDailyReport(dailyReport);

        return banana;
    }
}
