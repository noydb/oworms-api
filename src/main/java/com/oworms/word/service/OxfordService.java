package com.oworms.word.service;

import com.oworms.auth.service.SettingsService;
import com.oworms.error.OWormException;
import com.oworms.error.OWormExceptionType;
import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.service.EmailService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
public class OxfordService {

    private final EmailService emailService;
    private final SettingsService ss;

    private final Bucket bucket;

    @Value("${oxford.api.url}")
    private String oxfordApiURL;

    @Value("${oxford.app.id}")
    private String oxfordAppId;

    @Value("${oxford.app.key}")
    private String oxfordAppKey;

    private final static RestTemplate REST_TEMPLATE = new RestTemplate();

    public OxfordService(final EmailService emailService, final SettingsService ss) {
        this.emailService = emailService;
        this.ss = ss;
        this.bucket = Bucket
                .builder()
                .addLimit(Bandwidth.classic(200, Refill.greedy(200, Duration.ofDays(1)))).build();
    }

    @Transactional
    public ResponseEntity<String> oxfordRetrieve(String theWord, String u, String banana) {
        consumeToken("Retrieve word from oxford API");
        ss.permit(u, banana);

        HttpHeaders headers = new HttpHeaders();
        headers.set("app_id", oxfordAppId);
        headers.set("app_key", oxfordAppKey);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String url = oxfordApiURL.replace("{theWord}", theWord);

        try {
            return REST_TEMPLATE.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (RestClientException e) {
            throw new OWormException(OWormExceptionType.FAILURE, "Error while searching Oxford API", e.getMessage());
        }
    }

    private void consumeToken(final String context) {
        if (!bucket.tryConsume(1)) {
            emailService.sendBucketOverflow(new BucketOverflowDTO(this.getClass().getName(), context));

            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}
