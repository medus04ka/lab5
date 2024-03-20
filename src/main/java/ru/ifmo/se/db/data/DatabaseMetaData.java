package ru.ifmo.se.db.data;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DatabaseMetaData {
    private Class<?> clazz;
    private LocalDateTime localDateTime;
    private Long size;
}
