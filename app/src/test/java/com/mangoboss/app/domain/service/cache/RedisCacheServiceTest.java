package com.mangoboss.app.domain.service.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
class RedisCacheServiceTest {

    @Data
    @AllArgsConstructor
    static class TestDto {
        private Long id;
        private String name;
    }

    @Autowired
    private RedisCacheService redisCacheService;

    @Test
    void testRedisCacheStoreAndRetrieve() {
        // given
        String key = "test:test-dto-list";
        List<TestDto> originalList = List.of(
                new TestDto(1L, "Apple"),
                new TestDto(2L, "Banana")
        );

        // when
        redisCacheService.cacheObject(key, originalList, 100);
        List<TestDto> retrievedList = redisCacheService.getCachedObject(key, TestDto.class);

        for (TestDto testDto : retrievedList) {
            System.out.println("Retrieved ID: " + testDto.getId() + ", Name: " + testDto.getName());
        }

        // then
        assertThat(retrievedList).hasSize(2);
        assertThat(retrievedList).extracting("id").containsExactly(1L, 2L);
        assertThat(retrievedList).extracting("name").containsExactly("Apple", "Banana");
    }
}

