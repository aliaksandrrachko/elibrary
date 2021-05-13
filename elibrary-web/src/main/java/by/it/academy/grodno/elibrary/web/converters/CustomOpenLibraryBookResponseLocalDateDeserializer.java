package by.it.academy.grodno.elibrary.web.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import static java.time.temporal.ChronoField.*;

public class CustomOpenLibraryBookResponseLocalDateDeserializer extends StdDeserializer<LocalDate> {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("[MMMM][MM][ d,][ ]yyyy[-MM]")
            .parseDefaulting(MONTH_OF_YEAR, MONTH_OF_YEAR.range().getMinimum())
            .parseDefaulting(DAY_OF_MONTH, DAY_OF_MONTH.range().getMinimum())
            .parseDefaulting(HOUR_OF_DAY, HOUR_OF_DAY.range().getMinimum())
            .parseDefaulting(MINUTE_OF_HOUR, MINUTE_OF_HOUR.range().getMinimum())
            .parseDefaulting(SECOND_OF_MINUTE, SECOND_OF_MINUTE.range().getMinimum())
            .parseDefaulting(NANO_OF_SECOND, NANO_OF_SECOND.range().getMinimum())
            .toFormatter(Locale.ENGLISH);

    public CustomOpenLibraryBookResponseLocalDateDeserializer() {
        this(null);
    }

    protected CustomOpenLibraryBookResponseLocalDateDeserializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonparser, DeserializationContext ctxt) throws IOException {
        String date = jsonparser.getText();
        return LocalDate.parse(date, DATE_TIME_FORMATTER);
    }


}
