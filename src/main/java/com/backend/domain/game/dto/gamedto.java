package com.backend.domain.game.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class gamedto {
    private long appid;
    private String name;
    private String developer;
    private String publisher;
    private Map<String, Integer> tags;

}
