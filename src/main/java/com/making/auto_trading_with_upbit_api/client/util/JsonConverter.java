package com.making.auto_trading_with_upbit_api.client.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JsonConverter {

    private final ObjectMapper objectMapperForUpbit;

    /**
     * JSON 객체를 지정된 타입으로 변환합니다.
     *
     * @param data  JSON 노드
     * @param clazz 변환할 타입 클래스
     * @param <T>   변환할 타입
     * @return 파싱된 객체
     * @throws RuntimeException 파싱 실패 시
     */
    public <T> T toObject(final JsonNode data, final Class<T> clazz) {
        if (data == null) {
            throw new RuntimeException("Cannot parse null data to " + clazz.getSimpleName());
        }

        return convertNode(data, clazz);
    }

    /**
     * JSON 배열을 지정된 타입의 리스트로 파싱합니다.
     *
     * @param data  JSON 배열 노드
     * @param clazz 변환할 타입 클래스
     * @param <T>   변환할 타입
     * @return 파싱된 객체 리스트
     * @throws RuntimeException 파싱 실패 시
     */
    public <T> List<T> toList(final JsonNode data, final Class<T> clazz) {
        if (data == null) {
            throw new RuntimeException("Cannot parse null data to List<" + clazz.getSimpleName() + ">");
        }

        if (!data.isArray()) {
            throw new RuntimeException("Expected array but got " + data.getNodeType() + " for List<" + clazz.getSimpleName() + ">");
        }

        final List<T> result = new ArrayList<>();

        for (final JsonNode node : data) {
            final T item = convertNode(node, clazz);
            result.add(item);
        }

        return result;
    }

    private <T> T convertNode(final JsonNode node, final Class<T> clazz) {
        try {
            return objectMapperForUpbit.treeToValue(node, clazz);
        } catch (final Exception e) {
            log.error("Failed to parse {} from node: {}", clazz.getSimpleName(), node, e);
            throw new RuntimeException("Failed to parse " + clazz.getSimpleName(), e);
        }
    }
}