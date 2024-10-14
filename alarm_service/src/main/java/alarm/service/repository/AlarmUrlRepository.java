package alarm.service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import alarm.service.entity.AlarmUrl;

@Repository
public interface AlarmUrlRepository extends JpaRepository<AlarmUrl, UUID>{
	Optional<AlarmUrl> findByUserId(Long userId);
}
