package alarm.service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import alarm.service.entity.Alarm;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, UUID>{

	List<Alarm> findByUserId(long userId);

}
