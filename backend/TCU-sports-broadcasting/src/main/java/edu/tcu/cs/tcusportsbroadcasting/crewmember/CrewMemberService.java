package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import edu.tcu.cs.tcusportsbroadcasting.availability.AvailabilityRepository;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberListDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.CrewMemberNotFoundException;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.DuplicateEmailException;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.invite.dto.InviteResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.crewschedule.CrewScheduleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.invite.InviteToken;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.invite.InviteTokenRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CrewMemberService implements UserDetailsService {

    private final CrewMemberRepository crewMemberRepository;

    private final CrewScheduleRepository crewScheduleRepository;

    private final AvailabilityRepository availabilityRepository;

    private final PasswordEncoder passwordEncoder;

    private final InviteTokenRepository inviteTokenRepository;

    public CrewMemberService(CrewMemberRepository crewMemberRepository, CrewScheduleRepository crewScheduleRepository, AvailabilityRepository availabilityRepository, PasswordEncoder passwordEncoder, InviteTokenRepository inviteTokenRepository) {
        this.crewMemberRepository = crewMemberRepository;
        this.crewScheduleRepository = crewScheduleRepository;
        this.availabilityRepository = availabilityRepository;
        this.passwordEncoder = passwordEncoder;
        this.inviteTokenRepository = inviteTokenRepository;
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
        crewMember.setPassword(this.passwordEncoder.encode(dto.getPassword())); // now hashed
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

        // Delete related crew schedules first
        crewScheduleRepository.deleteAllByCrewMember_Id(userId);

        // Optionally delete related availability records
        availabilityRepository.deleteAllByCrewMember_Id(userId);

        // Now it's safe to delete the user so no error
        crewMemberRepository.delete(member);
    }


    public List<InviteResponseDto> inviteCrewMembers(List<String> emails) {
        List<InviteResponseDto> invites = new ArrayList<>();

        for (String email : emails) {
            String link = "https://localhost:8080/user/auth/login"; // change to whatever front end login page is

            invites.add(new InviteResponseDto(email, link));
        }

        return invites;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.crewMemberRepository.findByEmail(email)
                .map(crewMember -> new MyCrewMemberPrincipal(crewMember))
                .orElseThrow(() -> new UsernameNotFoundException("Email " + email + " not found"));
    }
}
