package stroom.statistics.impl.hbase.internal;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.docref.DocRef;
import stroom.kafka.pipeline.KafkaProducer;
import stroom.kafka.pipeline.KafkaProducerFactory;
import stroom.kafka.pipeline.KafkaProducerRecord;
import stroom.statistics.api.InternalStatisticEvent;
import stroom.statistics.api.InternalStatisticKey;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ExtendWith(MockitoExtension.class)
class TestStroomStatsInternalStatisticsService {
    private static final String DOC_REF_TYPE_1 = "myDocRefType1";
    private static final String DOC_REF_TYPE_2 = "myDocRefType2";
    private static final Logger LOGGER = LoggerFactory.getLogger(TestStroomStatsInternalStatisticsService.class);

    @Captor
    private ArgumentCaptor<Consumer<Throwable>> exceptionHandlerCaptor;
    @Mock
    private KafkaProducer mockStroomKafkaProducer;
    @Mock
    private KafkaProducerFactory mockStroomKafkaProducerFactory;

    @Test
    void putEvents_multipleEvents() {
        final HBaseStatisticsConfig hBaseStatisticsConfig = new HBaseStatisticsConfig();
        hBaseStatisticsConfig.setDocRefType(DOC_REF_TYPE_1);
        hBaseStatisticsConfig.getKafkaTopicsConfig().setCount("MyTopic");

        Mockito.when(mockStroomKafkaProducerFactory.createProducer(Mockito.any()))
                .thenReturn(Optional.of(mockStroomKafkaProducer));
        StroomStatsInternalStatisticsService stroomStatsInternalStatisticsService =
                new StroomStatsInternalStatisticsService(
                        mockStroomKafkaProducerFactory,
                        hBaseStatisticsConfig);

        //assemble test data
        InternalStatisticEvent event1 = InternalStatisticEvent.createPlusOneCountStat(
                InternalStatisticKey.MEMORY, 0, Collections.emptyMap());
        InternalStatisticEvent event2 = InternalStatisticEvent.createPlusOneCountStat(
                InternalStatisticKey.MEMORY, 1, Collections.emptyMap());
        InternalStatisticEvent event3 = InternalStatisticEvent.createPlusOneCountStat(
                InternalStatisticKey.MEMORY, 1, Collections.emptyMap());
        DocRef docRefA = new DocRef(DOC_REF_TYPE_1, UUID.randomUUID().toString(), "myStat1");
        DocRef docRefB = new DocRef(DOC_REF_TYPE_2, UUID.randomUUID().toString(), "myStat2");
        Map<DocRef, List<InternalStatisticEvent>> map = Map.of(
                docRefA, Arrays.asList(event1, event2),
                docRefB, Collections.singletonList(event3));

        stroomStatsInternalStatisticsService.putEvents(map);

        //two different doc refs so two calls to producer
        Mockito.verify(mockStroomKafkaProducer, Mockito.times(2))
                .sendAsync(Mockito.any(KafkaProducerRecord.class), Mockito.any());
    }

    @Test
    void putEvents_largeBatch() {
        final HBaseStatisticsConfig hBaseStatisticsConfig = new HBaseStatisticsConfig();
        hBaseStatisticsConfig.setDocRefType(DOC_REF_TYPE_1);
        hBaseStatisticsConfig.getKafkaTopicsConfig().setCount("MyTopic");
        hBaseStatisticsConfig.setEventsPerMessage(10);

        Mockito.when(mockStroomKafkaProducerFactory.createProducer(Mockito.any()))
                .thenReturn(Optional.of(mockStroomKafkaProducer));
        StroomStatsInternalStatisticsService stroomStatsInternalStatisticsService =
                new StroomStatsInternalStatisticsService(
                        mockStroomKafkaProducerFactory,
                        hBaseStatisticsConfig
                );

        //assemble test data
        DocRef docRefA = new DocRef(DOC_REF_TYPE_1, UUID.randomUUID().toString(), "myStat1");
        DocRef docRefB = new DocRef(DOC_REF_TYPE_2, UUID.randomUUID().toString(), "myStat2");
        Map<DocRef, List<InternalStatisticEvent>> map = Map.of(
                docRefA, createNEvents(InternalStatisticKey.MEMORY, 100),
                docRefB, createNEvents(InternalStatisticKey.CPU, 15));

        stroomStatsInternalStatisticsService.putEvents(map);

        //two different doc refs and batch size of 10,
        //so kafka msg count is 10 for A and 2 for B, thus 12
        Mockito.verify(mockStroomKafkaProducer, Mockito.times(12))
                .sendAsync(Mockito.any(KafkaProducerRecord.class), Mockito.any());
    }

    private List<InternalStatisticEvent> createNEvents(final InternalStatisticKey key, final int count) {

        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> InternalStatisticEvent.createPlusOneCountStat(key, i, Collections.emptyMap()))
                .collect(Collectors.toList());
    }

    @Test
    void putEvents_exception() {
        final HBaseStatisticsConfig hBaseStatisticsConfig = new HBaseStatisticsConfig();
        hBaseStatisticsConfig.setDocRefType(DOC_REF_TYPE_1);
        hBaseStatisticsConfig.getKafkaTopicsConfig().setCount("MyTopic");

        Mockito.when(mockStroomKafkaProducerFactory.createProducer(Mockito.any()))
                .thenReturn(Optional.of(mockStroomKafkaProducer));
        StroomStatsInternalStatisticsService stroomStatsInternalStatisticsService =
                new StroomStatsInternalStatisticsService(
                        mockStroomKafkaProducerFactory,
                        hBaseStatisticsConfig
                );

        //assemble test data
        InternalStatisticEvent event = InternalStatisticEvent.createPlusOneCountStat(
                InternalStatisticKey.MEMORY, 0, Collections.emptyMap());
        DocRef docRef = new DocRef(DOC_REF_TYPE_1, UUID.randomUUID().toString(), "myStat");
        Map<DocRef, List<InternalStatisticEvent>> map = Map.of(docRef, Collections.singletonList(event));

        //exercise the service
        stroomStatsInternalStatisticsService.putEvents(map);

        //ensure sendAsync is called
        Mockito.verify(mockStroomKafkaProducer)
                .sendAsync(Mockito.any(KafkaProducerRecord.class), exceptionHandlerCaptor.capture());

        //create an exception in the handler
        exceptionHandlerCaptor.getValue()
                .accept(new RuntimeException("Test Exception inside mockStroomKafkaProducer"));

        //the exception handler should just  log so nothing needs to be asserted
    }
}