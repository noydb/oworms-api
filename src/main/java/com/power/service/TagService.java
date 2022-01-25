package com.power.service;

import com.power.domain.Tag;
import com.power.domain.Word;
import com.power.dto.TagDTO;
import com.power.error.OWormException;
import com.power.error.OWormExceptionType;
import com.power.mapper.TagMapper;
import com.power.repository.TagRepository;
import com.power.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository repository;
    private final WordRepository wordRepository;
    private final HelperService helperService;

    public TagService(final TagRepository repository,
                      final WordRepository wordRepository,
                      final HelperService helperService) {
        this.repository = repository;
        this.wordRepository = wordRepository;
        this.helperService = helperService;
    }

    public void updateTagsForWord(Long wordId, List<Long> tagIds, String permissionKey) {
        helperService.checkPermission(permissionKey);

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
    public TagDTO create(final TagDTO tagDTO, String permissionKey) {
        helperService.checkPermission(permissionKey);
        helperService.checkActionLimit();

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
        helperService.checkRetrievalLimit();

        final List<Tag> tags = repository.findAll();

        // TODO: figure out
//        for (Tag tag : tags) {
//            List<Word> words = wordRepository.findAllByTagsContains(tag);
//            tag.setWordCount(words.size());
//        }

        return TagMapper.mapTo(tags);
    }

    public TagDTO retrieve(final Long tagId) {
        helperService.checkRetrievalLimit();

        final Tag tag = findById(tagId);

        return TagMapper.map(tag);
    }

    public TagDTO update(Long tagId, TagDTO updatedTag, String permissionKey) {
        helperService.checkPermission(permissionKey);
        helperService.checkActionLimit();

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

}
