package cl.duoc.flanhotel.ms_habitacion;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Forzamos a que esta prueba use localhost en tu PC y no busque la red de Docker
@SpringBootTest(properties = "spring.datasource.url=jdbc:mysql://localhost:3306/db_hotel_habitacion")
class MsHabitacionApplicationTests {

	@Test
	void contextLoads() {
	}

}
