package com.rostov.transport.telegrambot.gateway;

import com.rostov.transport.telegrambot.model.Bus;

import java.util.List;
import java.util.Map;

public interface BusGateway {

    Map<String, List<Integer>> getBusNumbersList();

    List<Bus> getBusListByCode(String code);

    List<Bus> getBusListByCode(List<String> codeList);
}
