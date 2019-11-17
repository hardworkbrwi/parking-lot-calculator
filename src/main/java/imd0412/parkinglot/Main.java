package imd0412.parkinglot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Main
{
	public static void main(String[] args)
	{
		final String checkin = "2016.04.08 12:30";
		final String checkout = "2017.04.08 14:35";

		try
		{
			// Transformar de String para objeto data
			LocalDateTime checkinTime = LocalDateTime.parse(checkin, Constants.DATE_FORMATTER);
			LocalDateTime checkoutTime = LocalDateTime.parse(checkout, Constants.DATE_FORMATTER);

			System.out.printf("Checkin %s, Checkout %s\n", checkinTime, checkoutTime);

			// Extrair dados do objeto data
			int year = checkinTime.getYear();
			int month = checkinTime.getMonth().getValue();
			int dayOfMonth = checkinTime.getDayOfMonth();
			int hour = checkinTime.getHour();
			int minute = checkinTime.getMinute();
			System.out.printf("Data formatada com os dados extraídos: %d-%d-%d %d:%d\n", year, month, dayOfMonth, hour,
					minute);

			// Calcular a diferença entre dois objetos data
			Duration duration = Duration.between(checkinTime, checkoutTime);
			long days = duration.toDays();
			long hours = duration.toHours();
			long minutes = duration.toMinutes();
			System.out.printf("Permanência de: %d dias, ou %d horas, ou %d minutos.\n", days, hours, minutes);
		}
		catch (DateTimeParseException exc)
		{
			System.err.printf("%s is not parsable!%n", checkin);
			throw exc;
		}

		// Exemplo de como identificar inputs fora do padrão
		final String incorrectInput = "2011/10/09 11h20min";
		try
		{
			LocalDateTime.parse(incorrectInput, Constants.DATE_FORMATTER);
		}
		catch (DateTimeParseException exc)
		{
			System.err.printf("%s is not parsable!%n", incorrectInput);
			throw exc;
		}
	}
}