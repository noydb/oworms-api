package com.oworms.word.service;

import com.oworms.common.error.OWormException;
import com.oworms.common.error.OWormExceptionType;
import com.oworms.common.util.Utils;
import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.service.EmailService;
import com.oworms.word.domain.Tag;
import com.oworms.word.domain.Word;
import com.oworms.word.dto.TagDTO;
import com.oworms.word.mapper.TagMapper;
import com.oworms.word.repository.TagRepository;
import com.oworms.word.repository.WordRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository repository;
    private final WordRepository wordRepository;
    private final EmailService emailService;
    private final Bucket bucket;

    public TagService(final TagRepository repository,
                      final WordRepository wordRepository,
                      final EmailService emailService) {
        this.repository = repository;
        this.wordRepository = wordRepository;
        this.emailService = emailService;
        this.bucket = Bucket.builder().addLimit(Bandwidth.classic(300, Refill.greedy(300, Duration.ofDays(1)))).build();
    }

    @Transactional
    public void updateTagsForWord(Long wordId, List<Long> tagIds) {
        consumeToken("update for word");

        Word word = wordRepository
                .findById(wordId)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "A word with that ID does not exist"));

        List<Tag> tags = new ArrayList<>();

        for (Long tagId : tagIds) {
            Tag tag = repository
                    .findById(tagId)
                    .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "Tag with ID " + tagId + " does not exist"));

            tags.add(tag);
        }

        word.setTags(tags);

        wordRepository.save(word);
    }

    @Transactional
    public TagDTO create(final TagDTO tagDTO) {
        consumeToken("create");

        if (tagExists(tagDTO)) {
            throw new OWormException(OWormExceptionType.ALREADY_EXISTS, "That tag already exists");
        }

        Tag tag = TagMapper.map(tagDTO);

        tag = repository.save(tag);

        return TagMapper.map(tag);
    }

    private boolean tagExists(TagDTO tagDTO) {
        List<Tag> tags = repository.findAll();

        for (Tag tag : tags) {
            if (Utils.areEqual(tag.getName(), tagDTO.getName())) {
                return true;
            }
        }

        return false;
    }

    public List<TagDTO> retrieveAll(String name) {
        consumeToken("retrieve all");

        final List<TagDTO> tags = repository.findAll()
                .stream()
                .map(tag -> {
                    final TagDTO dto = TagMapper.map(tag);
                    dto.setWordCount((int) wordRepository.countByTagsIdEquals(tag.getId()));

                    return dto;
                })
                .sorted(Comparator.comparing(TagDTO::getName))
                .collect(Collectors.toList());

        return tags;
    }

    public TagDTO retrieve(final Long tagId) {
        // check limit

        final Tag tag = findById(tagId);

        return TagMapper.map(tag);
    }

    @Transactional
    public TagDTO update(Long tagId, TagDTO updatedTag) {
        consumeToken("update");

        Tag tag = findById(tagId);

        boolean alreadyExists = repository.findByNameIgnoreCaseAndIdNot(updatedTag.getName(), tagId).isPresent();
        if (alreadyExists) {
            throw new OWormException(OWormExceptionType.ALREADY_EXISTS, "That tag already exists");
        }

        tag.setName(updatedTag.getName());

        tag = repository.save(tag);

        return TagMapper.map(tag);
    }

    private Tag findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "A tag with an ID of " + id + " does not exist"));
    }

    private void consumeToken(String context) {
        if (!bucket.tryConsume(1)) {
            emailService.sendBucketOverflow(new BucketOverflowDTO(this.getClass().getName(), context));

            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}
