package com.literatura.apiGutendex.service;

public interface IConvertData {
    <T> T convertData(String json, Class<T> tClass);
}
