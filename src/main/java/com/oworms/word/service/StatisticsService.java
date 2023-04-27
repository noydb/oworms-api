package com.oworms.word.service;

import com.oworms.common.error.OWormException;
import com.oworms.common.error.OWormExceptionType;
import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.service.EmailService;
import com.oworms.word.domain.Word;
import com.oworms.word.dto.StatisticsDTO;
import com.oworms.word.repository.WordRepository;
import com.oworms.word.util.StatsUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class StatisticsService {

    private final WordRepository repository;
    private final EmailService emailService;
    private final Bucket bucket;

    public StatisticsService(final WordRepository repository, final EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
        this.bucket = Bucket
                .builder()
                .addLimit(Bandwidth.classic(200, Refill.greedy(200, Duration.ofDays(1)))).build();
    }

    public StatisticsDTO getStatistics() {
        consumeToken();

        StatisticsDTO stats = new StatisticsDTO();

        List<Word> words = repository.findAll();

        int totalWords = (int) repository.count();
        stats.setTotalWords(totalWords);

        int totalViewsOnWords = words
                .parallelStream()
                .reduce(0, (acc, current) -> acc + current.getTimesViewed(), Integer::sum);
        stats.setTotalViewsOnWords(totalViewsOnWords);

        StatsUtil.getPartsOfSpeechStats(stats, words);
        StatsUtil.getFirstLetterStats(stats, words);
        StatsUtil.createDateWordMap(stats, words);

        return stats;
    }

    private void consumeToken() {
        if (!bucket.tryConsume(1)) {
            emailService.sendBucketOverflow(new BucketOverflowDTO(this.getClass().getName(), "statistics"));

            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}
