package cl.duoc.flanhotel.ms_habitacion;

import cl.duoc.flanhotel.ms_habitacion.entidad.Habitacion;
import cl.duoc.flanhotel.ms_habitacion.repository.HabitacionRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DatosPruebaRunner implements CommandLineRunner {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (habitacionRepository.count() == 0) {

            Faker faker = new Faker(new Locale("es"));
            System.out.println("🏨 Iniciando la creación de habitaciones de prueba con DataFaker...");

            String[] tipos = {"Sencilla", "Doble", "Suite", "Matrimonial"};
            String[] estados = {"DISPONIBLE", "OCUPADA", "MANTENIMIENTO"};

            for (int i = 0; i < 10; i++) {
                Habitacion habitacion = new Habitacion();
                habitacion.setNumero(String.valueOf(101 + i));
                habitacion.setTipo(tipos[faker.number().numberBetween(0, tipos.length)]);
                habitacion.setEstado(estados[faker.number().numberBetween(0, estados.length)]);
                habitacion.setPrecioNoche(faker.number().numberBetween(30000, 150000));
                habitacionRepository.save(habitacion);
            }

            System.out.println("✅ ¡10 Habitaciones de prueba creadas exitosamente!");
        } else {
            System.out.println("ℹ️ La base de datos ya tiene habitaciones, omitiendo DataFaker.");
        }
    }
}