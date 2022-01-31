package com.power.oworms.auth.service;

import com.power.oworms.auth.domain.AppSettings;
import com.power.oworms.auth.repository.SettingsRepository;
import com.power.oworms.auth.repository.UserRepository;
import com.power.oworms.common.error.OWormException;
import com.power.oworms.common.error.OWormExceptionType;
import com.power.oworms.common.util.Utils;
import com.power.oworms.mail.dto.BucketOverflowDTO;
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
import java.time.OffsetDateTime;
import java.time.ZoneId;
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

    public void permit(String username, String bananaArg) {
        AppSettings settings = getSettings();

        if (!Utils.areEqual(bananaArg, settings.getBanana())) {
            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "You cannot do that");
        }

        userRepository
                .findByUsername(username)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "That user does not exist"));
    }

    public void doDailyAdmin(String username, String bananaArg) {
        consumeToken("daily admin");
        userRepository
                .findByUsername(username)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "That user does not exist"));

        AppSettings settings = repository.findAll().get(0);
        // if we do this then server cannot backup automatically
        // because it will always need the day's banana
//        if (!Utils.areEqual(settings.getBanana(), bananaArg)) {
//            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "You cannot do that");
//        }

        LocalDate now = LocalDate.now();

        long noOfDaysBetween = ChronoUnit.DAYS.between(now, settings.getDateTime());
        if (noOfDaysBetween < 7) {
            // settings have been configured for the week. do not send mail
            return;
        }

        String bananaEnc = sendNewBanana();
        settings.setBanana(bananaEnc);
        settings.setDateTime(OffsetDateTime.now(ZoneId.of("Africa/Johannesburg")));

        repository.save(settings);
    }

    private AppSettings getSettings() {
        List<AppSettings> allSettings = repository.findAll();
        if (allSettings.isEmpty()) {
            throw new OWormException(OWormExceptionType.FAILURE, "A settings row must exist in the DB");
        }

        // will only ever be one
        AppSettings settings = allSettings.get(0);

        // settings are relevant for this week
        LocalDate now = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(now, settings.getDateTime());
        if (daysBetween < 7) {
            return settings;
        }

        // configure settings for new day
        String banana = sendNewBanana();
        settings.setBanana(banana);
        settings.setDateTime(OffsetDateTime.now(ZoneId.of("Africa/Johannesburg")));

        repository.save(settings);

        return settings;
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

    private void consumeToken(String context) {
        if (!bucket.tryConsume(1)) {
            emailService.sendBucketOverflow(new BucketOverflowDTO(this.getClass().getName(), context));

            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}
