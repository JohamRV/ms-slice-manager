package pe.edu.pucp.msslicemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.msslicemanager.entity.SliceManager;

import java.util.List;

@Repository
public interface SliceManagerRepository extends JpaRepository<SliceManager, Integer> {

    @Query(value = "SELECT * FROM PSTK_SLICE s \n" +
            "INNER JOIN PSTK_USER u ON u.FK_SLICE_ID = s.SLICE_ID \n" +
            "WHERE u.EMAIL = ?1", nativeQuery = true)
    List<SliceManager> findByUsername(String username);

}
