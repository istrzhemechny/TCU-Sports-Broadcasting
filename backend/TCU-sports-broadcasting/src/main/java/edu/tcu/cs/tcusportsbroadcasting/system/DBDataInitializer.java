package edu.tcu.cs.tcusportsbroadcasting.system;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final CrewMemberRepository crewMemberRepository;

    public DBDataInitializer(CrewMemberRepository crewMemberRepository) {
        this.crewMemberRepository = crewMemberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        CrewMember cm1 = new CrewMember();
        cm1.setFirstName("John");
        cm1.setLastName("Doe");
        cm1.setEmail("john.doe@example.com");
        cm1.setPhoneNumber("1234567890");
        cm1.setPassword("P@ssw0rd");
        cm1.setRole("ADMIN");
        cm1.setPositions(Arrays.asList("Director", "Producer"));

        CrewMember cm2 = new CrewMember();
        cm2.setFirstName("Jane");
        cm2.setLastName("Smith");
        cm2.setEmail("jane.smith@example.com");
        cm2.setPhoneNumber("9876543210");
        cm2.setPassword("Secret123");
        cm2.setRole("USER");
        cm2.setPositions(Arrays.asList("Camera", "Graphics"));

        crewMemberRepository.save(cm1);
        crewMemberRepository.save(cm2);
    }
}
