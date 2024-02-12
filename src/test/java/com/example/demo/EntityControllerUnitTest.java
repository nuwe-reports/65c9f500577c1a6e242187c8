import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.controllers.AppointmentController;
import com.example.demo.repositories.AppointmentRepository;
import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AppointmentController.class)
class EntityControllerUnitTest {

    @MockBean
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRetrieveAllAppointmentsSuccessfully() throws Exception {
        List<Appointment> appointments = Arrays.asList(
                new Appointment(new Patient(), new Doctor(), new Room(), LocalDateTime.now(), LocalDateTime.now().plusHours(1)),
                new Appointment(new Patient(), new Doctor(), new Room(), LocalDateTime.now(), LocalDateTime.now().plusHours(2))
        );
        when(appointmentRepository.findAll()).thenReturn(appointments);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(appointments.size()));
    }

    @Test
    void shouldRetrieveAppointmentByIdSuccessfully() throws Exception {
        Appointment appointment = new Appointment(new Patient(), new Doctor(), new Room(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/appointments/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startsAt").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.finishesAt").isNotEmpty());
    }

    @Test
    void shouldCreateAppointmentSuccessfully() throws Exception {
        Appointment appointmentToCreate = new Appointment(new Patient(), new Doctor(), new Room(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointmentToCreate);

        String requestBody = objectMapper.writeValueAsString(appointmentToCreate);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void shouldDeleteAppointmentSuccessfully() throws Exception {
        Appointment appointmentToDelete = new Appointment(new Patient(), new Doctor(), new Room(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointmentToDelete));
        doNothing().when(appointmentRepository).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/appointments/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
