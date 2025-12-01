package vpunko.spotify.core.service.aop;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import vpunko.spotify.core.exception.KafkaExceptionCounted;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaErrorCountingAspect {

    private final MeterRegistry meterRegistry;

    @Around("@annotation(kafkaExceptionCounted)")
    public Object countErrors(ProceedingJoinPoint joinPoint,
                              KafkaExceptionCounted kafkaExceptionCounted) throws Throwable {

        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            String counterName = kafkaExceptionCounted.value();

            log.error("⚠️ Kafka listener exception. Incrementing counter '{}'", counterName, ex);

            Counter counter = meterRegistry.counter(counterName);
            counter.increment();

            throw ex;
        }
    }
}
