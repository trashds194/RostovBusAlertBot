package com.rostov.transport.telegrambot.gateway.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rostov.transport.telegrambot.gateway.BusGateway;
import com.rostov.transport.telegrambot.model.Bus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static com.rostov.transport.telegrambot.contants.BusApiConstants.GET_BUS_LIST_URL;
import static com.rostov.transport.telegrambot.contants.BusApiConstants.POST_BUS_LIST_URL;

@Component
@RequiredArgsConstructor
public class BusGatewayImpl implements BusGateway {

    private final TypeReference<Map<String, List<Integer>>> MAP_TYPE_REF = new TypeReference<>() {
    };
    private final TypeReference<List<Bus>> LIST_BUS_TYPE_REF = new TypeReference<>() {
    };

    private final RestTemplate template;
    private final ObjectMapper mapper;

    @Override
    public Map<String, List<Integer>> getBusNumbersList() {
        try {
            String response = template.getForObject(GET_BUS_LIST_URL, String.class);
            return mapper.readValue(response, MAP_TYPE_REF);
            // TODO throw normal exception
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Bus> getBusListByCode(String code) {
        return getBusListByCodes(List.of(code));
    }

    @Override
    public List<Bus> getBusListByCode(List<String> codeList) {
        return getBusListByCodes(codeList);
    }

    private List<Bus> getBusListByCodes(List<String> codeList) {
        try {
            Map<String, List<String>> busMap = Map.of("bus", codeList);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject body = new JSONObject(busMap);
            HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

            String response = template.postForObject(POST_BUS_LIST_URL, request, String.class);
            return mapper.readValue(response, LIST_BUS_TYPE_REF);
            // TODO
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
