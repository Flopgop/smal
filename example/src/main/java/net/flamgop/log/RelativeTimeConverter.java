package net.flamgop.log;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Plugin(name = "RelativeTimeConverter", category = "Converter")
@ConverterKeys({"uptime"})
public final class RelativeTimeConverter extends LogEventPatternConverter {

    private RelativeTimeConverter(String[] options) {
        super("RelativeTime", "relativeTime");
    }

    public static RelativeTimeConverter newInstance(final String[] options) {
        return new RelativeTimeConverter(options);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        Optional<Instant> startInstant = ProcessHandle.current().info().startInstant();
        if (startInstant.isEmpty()) {
            toAppendTo.append("unknown");
            return;
        }

        Duration uptime = Duration.ofMillis(startInstant.get().until(Instant.now(), ChronoUnit.MILLIS));
        toAppendTo.append(String.format("%02d:%02d:%02d.%03d", uptime.toHours(), uptime.toMinutesPart(), uptime.toSecondsPart(), uptime.toMillisPart()));
    }
}
