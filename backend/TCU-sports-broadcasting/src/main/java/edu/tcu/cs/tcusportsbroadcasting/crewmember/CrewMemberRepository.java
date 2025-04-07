package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
    boolean existsByEmail(String email);
}
