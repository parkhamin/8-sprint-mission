package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  @Query("SELECT m FROM Message m " +
      "JOIN FETCH m.author " + // N+1 예방을 위한 Fetch Join
      "JOIN FETCH m.channel " +
      "WHERE m.channel.id = :channelId " +
      "AND (:cursor IS NULL OR m.id < :cursor) " +
      "ORDER BY m.id DESC")
  Slice<Message> findAllByCursor(@Param("channelId") UUID channelId,
      @Param("cursor") UUID cursor,
      Pageable pageable);

  void deleteAllByChannelId(UUID channelId);

  Optional<Message> findTopByChannelIdOrderByCreatedAtDesc(UUID channelId);
}
