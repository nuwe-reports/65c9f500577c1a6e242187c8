import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;

@DataJpaTest
class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoomRepository roomRepository;

    @AfterEach
    void tearDown() {
        entityManager.clear();
    }

    @Test
    void testDoctorEntity() {
        Doctor doctor = new Doctor("Dr. John Doe", "Cardiology");
        entityManager.persistAndFlush(doctor);

        Doctor retrievedDoctor = doctorRepository.findById(doctor.getId()).orElse(null);

        assertThat(retrievedDoctor).isNotNull();
        assertThat(retrievedDoctor).isEqualTo(doctor);
    }

    @Test
    void testPatientEntity() {
        Patient patient = new Patient("John Smith", "john@example.com", "123456789");
        entityManager.persistAndFlush(patient);

        Patient retrievedPatient = patientRepository.findById(patient.getId()).orElse(null);

        assertThat(retrievedPatient).isNotNull();
        assertThat(retrievedPatient).isEqualTo(patient);
    }

    @Test
    void testRoomEntity() {
        Room room = new Room("Room101");
        entityManager.persistAndFlush(room);

        Room retrievedRoom = roomRepository.findById(room.getRoomName()).orElse(null);

        assertThat(retrievedRoom).isNotNull();
        assertThat(retrievedRoom).isEqualTo(room);
    }
}
