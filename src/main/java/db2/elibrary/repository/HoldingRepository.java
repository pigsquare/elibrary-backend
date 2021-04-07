package db2.elibrary.repository;

import db2.elibrary.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoldingRepository extends JpaRepository<Holding, Integer> {
}
