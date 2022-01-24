package com.power.mapper;

import com.power.domain.Tag;
import com.power.dto.TagDTO;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TagMapper {

    private TagMapper() {
    }

    public static Tag map(TagDTO tagDTO) {
        final Tag tag = new Tag();

        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());

        return tag;
    }

    public static TagDTO map(Tag tag) {
        final TagDTO tagDTO = new TagDTO();

        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());

        return tagDTO;
    }

    public static List<TagDTO> mapTo(List<Tag> tags) {
        return tags
                .stream()
                .map(TagMapper::map)
                .collect(toList());
    }

    public static List<Tag> mapFrom(List<TagDTO> tags) {
        return tags
                .stream()
                .map(TagMapper::map)
                .collect(toList());
    }
}
