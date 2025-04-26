package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
    boolean existsByEmail(String email);

    Optional<CrewMember> findByEmail(String email);

    // Optional<CrewMember> findByEmailAndPassword(String email, String password);
}
