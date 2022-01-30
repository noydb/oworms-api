package com.power.oworms.word.service;

import com.power.oworms.common.error.OWormException;
import com.power.oworms.common.error.OWormExceptionType;
import com.power.oworms.word.domain.Tag;
import com.power.oworms.word.domain.Word;
import com.power.oworms.word.dto.TagDTO;
import com.power.oworms.word.mapper.TagMapper;
import com.power.oworms.word.repository.TagRepository;
import com.power.oworms.word.repository.WordRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository repository;
    private final WordRepository wordRepository;
    private final Bucket bucket;

    public TagService(final TagRepository repository,
                      final WordRepository wordRepository) {
        this.repository = repository;
        this.wordRepository = wordRepository;
        this.bucket = Bucket.builder().addLimit(Bandwidth.classic(300, Refill.greedy(300, Duration.ofDays(1)))).build();
    }

    public void updateTagsForWord(Long wordId, List<Long> tagIds) {
        consumeToken();

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
        consumeToken();

        if (tagExists(tagDTO)) {
            throw new OWormException(OWormExceptionType.WORD_EXISTS, "That tag already exists");
        }

        Tag tag = TagMapper.map(tagDTO);

        tag = repository.save(tag);

        return TagMapper.map(tag);
    }

    private boolean tagExists(TagDTO tagDTO) {
        return repository.findByNameIgnoreCase(tagDTO.getName()).isPresent();
    }

    public List<TagDTO> retrieveAll(String name) {
        consumeToken();

        final List<Tag> tags = repository.findAll();

        // TODO: figure out
//        for (Tag tag : tags) {
//            List<Word> words = wordRepository.findAllByTagsContains(tag);
//            tag.setWordCount(words.size());
//        }

        return TagMapper.mapTo(tags);
    }

    public TagDTO retrieve(final Long tagId) {
        // check limit

        final Tag tag = findById(tagId);

        return TagMapper.map(tag);
    }

    public TagDTO update(Long tagId, TagDTO updatedTag) {
        consumeToken();

        Tag tag = findById(tagId);

        boolean alreadyExists = repository.findByNameIgnoreCaseAndIdNot(updatedTag.getName(), tagId).isPresent();
        if (alreadyExists) {
            throw new OWormException(OWormExceptionType.WORD_EXISTS, "That tag already exists");
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

    private void consumeToken() {
        if (!bucket.tryConsume(1)) {
            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }

}
