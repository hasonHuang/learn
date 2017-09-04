package com.hason.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class Bird {
    private Long id;

    // 学名
    @NotNull
    private String scientificName;

    // 种类
    @NotNull
    private String specie;

    // 数量
    @NotNull
    @Max(104000)
    private Double mass;

    // 长度
    @Max(210)
    private Integer length;

    public Long getId() {
        return id;
    }

    public Bird setId(Long id) {
        this.id = id;
        return this;
    }

    public String getScientificName() {
        return scientificName;
    }

    public Bird setScientificName(String scientificName) {
        this.scientificName = scientificName;
        return this;
    }

    public String getSpecie() {
        return specie;
    }

    public Bird setSpecie(String specie) {
        this.specie = specie;
        return this;
    }

    public Double getMass() {
        return mass;
    }

    public Bird setMass(Double mass) {
        this.mass = mass;
        return this;
    }

    public Integer getLength() {
        return length;
    }

    public Bird setLength(Integer length) {
        this.length = length;
        return this;
    }
}
