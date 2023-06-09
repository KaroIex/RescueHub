package com.example.rescuehubproject.adopters.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class GetAdopterByIdDTO {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String phone;
}
