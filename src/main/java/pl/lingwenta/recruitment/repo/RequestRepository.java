package pl.lingwenta.recruitment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lingwenta.recruitment.api.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
