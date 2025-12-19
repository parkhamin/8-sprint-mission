package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> binaryContents;

    public JCFBinaryContentRepository() {
        this.binaryContents = new HashMap<>();
    }
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        this.binaryContents.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(this.binaryContents.get(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return this.binaryContents.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.binaryContents.remove(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return this.binaryContents.values().stream()
                .filter(binaryContent -> ids.contains(binaryContent.getId()))
                .toList();
    }
}
