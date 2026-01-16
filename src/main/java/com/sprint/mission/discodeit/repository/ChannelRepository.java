package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  // PUBLIC 타입이거나, 주어진 ID 리스트에 포함된 채널만 조회
  @Query("SELECT c FROM Channel c WHERE c.type = 'PUBLIC' OR c.id IN :subscribedIds")
  List<Channel> findAllPublicOrSubscribed(@Param("subscribedIds") List<UUID> subscribedIds);
}
