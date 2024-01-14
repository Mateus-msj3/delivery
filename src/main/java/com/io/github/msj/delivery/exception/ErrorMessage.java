package com.io.github.msj.delivery.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    private int statusCode;

    private Date date;

    private String message;

    private String description;

}