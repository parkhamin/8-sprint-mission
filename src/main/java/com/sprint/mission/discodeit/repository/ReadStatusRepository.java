package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  void deleteAllByChannelId(UUID channelId);

  @Query("SELECT rs FROM ReadStatus rs " +
      "JOIN FETCH rs.user " +
      "JOIN FETCH rs.channel " +
      "WHERE rs.user.id = :userId")
  List<ReadStatus> findAllByUserIdWithFetchJoin(@Param("userId") UUID userId);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  // 내가 어떤 채널을 읽은 읾은 상태가 존재하는지 그렇지 않은지
  boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
}
