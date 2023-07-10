package pe.edu.pucp.msslicemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.msslicemanager.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM pucp_stack.pstk_user u\n" +
            "WHERE u.FK_SLICE_ID = ?1", nativeQuery = true)
    List<User> findBySliceId(Integer sliceId);

}
