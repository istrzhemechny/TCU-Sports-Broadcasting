package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberListDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.CrewMemberNotFoundException;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.DuplicateEmailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CrewMemberService {

    private final CrewMemberRepository crewMemberRepository;

    public CrewMemberService(CrewMemberRepository crewMemberRepository) {
        this.crewMemberRepository = crewMemberRepository;
    }

    public CrewMemberResponseDto addCrewMember(String token, CrewMemberDto dto) {
        if (crewMemberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException(dto.getEmail());
        }

        CrewMember crewMember = new CrewMember();
        crewMember.setFirstName(dto.getFirstName());
        crewMember.setLastName(dto.getLastName());
        crewMember.setEmail(dto.getEmail());
        crewMember.setPhoneNumber(dto.getPhoneNumber());
        crewMember.setPassword(dto.getPassword()); // consider hashing in the future
        crewMember.setRole(dto.getRole());
        crewMember.setPosition(dto.getPosition());

        CrewMember saved = crewMemberRepository.save(crewMember);

        return new CrewMemberResponseDto(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getPhoneNumber(),
                saved.getRole(),
                saved.getPosition()
        );
    }

    public CrewMemberResponseDto findById(Long id) {
        CrewMember crewMember = crewMemberRepository.findById(id)
                .orElseThrow(() -> new CrewMemberNotFoundException(id));

        return new CrewMemberResponseDto(
                crewMember.getId(),
                crewMember.getFirstName(),
                crewMember.getLastName(),
                crewMember.getEmail(),
                crewMember.getPhoneNumber(),
                crewMember.getRole(),
                crewMember.getPosition()
        );
    }

    public List<CrewMemberListDto> findAll() {
        return crewMemberRepository.findAll().stream()
                .map(c -> new CrewMemberListDto(
                        c.getId(),
                        c.getFirstName() + " " + c.getLastName(),
                        c.getEmail(),
                        c.getPhoneNumber()
                ))
                .collect(Collectors.toList());
    }

    public void deleteCrewMember(Long userId) {
        CrewMember member = crewMemberRepository.findById(userId)
                .orElseThrow(() -> new CrewMemberNotFoundException(userId));

        crewMemberRepository.delete(member); // Cascade delete handles availabilities
    }

}
