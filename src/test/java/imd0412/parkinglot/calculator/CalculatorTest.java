package imd0412.parkinglot.calculator;

import static org.junit.Assert.assertThat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import imd0412.parkinglot.Constants;
import imd0412.parkinglot.ParkingLotType;

@RunWith( Parameterized.class )
public class CalculatorTest{
	//Configuração
	private static Calculator calculator;
	private static float parkingCostCurrent;
	private static float parkingCostExpected;
	
	private int year;
	private int month;
	private int dayOfMonth;
	private int hour;
	private int minute;
	
	private long days;
	private long hours;
	private long minutes;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		calculator = new Calculator();
		parkingCostCurrent = 0.0f;
		parkingCostExpected = 0.0f;
	}
	
	@Parameters( name = "{3}: {0} - {1} - {2}" )
	public static Collection<Object[]> data(){

		
		return Arrays.asList( new Object[][] {
			{ ParkingLotType.ShortTerm, "2017.04.08 12:30", "2017.04.08 13:29", "Classe_1" },
			{ ParkingLotType.ShortTerm, "2017.04.08 12:30", "2017.04.08 13:31", "Classe_2" },
			{ ParkingLotType.ShortTerm, "2017.04.08 12:30", "2017.04.10 13:29", "Classe_3" },
			{ ParkingLotType.ShortTerm, "2017.04.08 12:30", "2017.04.18 13:29", "Classe_4" },
			{ ParkingLotType.LongTerm, "2017.04.08 12:30", "2017.04.09 10:29", "Classe_5" },
			{ ParkingLotType.LongTerm, "2017.04.12 12:30", "2017.04.19 13:29", "Classe_6" },
			{ ParkingLotType.LongTerm, "2017.04.08 12:30", "2017.04.15 12:31", "Classe_7" },
			{ ParkingLotType.LongTerm, "2017.04.08 12:30", "2017.05.15 12:31", "Classe_8" },
			{ ParkingLotType.VIP, "2017.04.08 12:30", "2017.04.12 12:31", "Classe_9" },
			{ ParkingLotType.VIP, "2017.04.08 12:30", "2017.04.18 12:31", "Classe_10" },
			{ ParkingLotType.VIP, "2017.04.08 12:30", "2017.04.23 12:31", "Classe_11" },
			
			
		});
	}	
	
	@Parameter(0)
	public ParkingLotType parkingLotParking;
	@Parameter(1)
	public String checkin;
	@Parameter(2)
	public String checkout;
	
	public void calculateParkingCostTemplate() {		
		//Execução
		parkingCostCurrent = calculator.calculateParkingCost(checkin, checkout, parkingLotParking);
		
		try{
			// Transformar de String para objeto data
			LocalDateTime checkinTime = LocalDateTime.parse(checkin, Constants.DATE_FORMATTER);
			LocalDateTime checkoutTime = LocalDateTime.parse(checkout, Constants.DATE_FORMATTER);

			System.out.printf("Checkin %s, Checkout %s\n", checkinTime, checkoutTime);

			// Extrair dados do objeto data
			year = checkinTime.getYear();
			month = checkinTime.getMonth().getValue();
			dayOfMonth = checkinTime.getDayOfMonth();
			hour = checkinTime.getHour();
			minute = checkinTime.getMinute();
			//System.out.printf("Data formatada com os dados extraídos: %d-%d-%d %d:%d\n", year, month, dayOfMonth, hour, minute);

			// Calcular a diferença entre dois objetos data
			Duration duration = Duration.between(checkinTime, checkoutTime);
			days = duration.toDays();
			hours = duration.toHours();
			minutes = duration.toMinutes();
			//System.out.printf("Permanência de: %d dias, ou %d horas, ou %d minutos.\n", days, hours, minutes);
		
		}catch (DateTimeParseException exc){
			System.err.printf("%s is not parsable!%n", checkin);
			throw exc;
		}

		parkingCostExpected = 0.0f;
		if( parkingLotParking == ParkingLotType.ShortTerm ) {
			if( minutes >= 1440 && hours <= 168 ) {				
				parkingCostExpected = 50 * days;
				
			}else if( minutes > 10080 ) {
				parkingCostExpected = 30 * days;
			}
			
			parkingCostExpected += 8.0f + 2 * hours;
			
		}else if( parkingLotParking == ParkingLotType.LongTerm ) {
			if( minutes > 43200 ) {				
				parkingCostExpected = 500 * days + 30 * days;
				
			}else if( minutes > 10080 && minutes <= 43200  ) {
				parkingCostExpected = 30 * days;
				
			}else if( minutes > 1440 && minutes <= 10080 ) {
				parkingCostExpected = 50 * days;
				
			}			
			
			parkingCostExpected += 70;
			
		}else {
			if( minutes > 21600 ) {
				parkingCostExpected = 80 * days;
				
			}else if( minutes > 10080 && minutes <= 21600  ) {
				parkingCostExpected = 100 * days;
				
			}
			
			parkingCostExpected += 500;
			
		}		
		
	}
	
	
	//Validação
	@After
	public void tearDown() {
		assertThat( parkingCostCurrent, is(parkingCostExpected) );
		
	
	}
}
