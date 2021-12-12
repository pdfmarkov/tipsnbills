package com.tagall.tipsnbills.module.responses;

import com.tagall.tipsnbills.module.Characteristic;
import lombok.Data;

import java.util.List;

@Data
public class AvgResponseDto {

    List<Characteristic> characteristics;

    public AvgResponseDto(List<Characteristic> characteristics) {
        this.characteristics = characteristics;
    }
}
