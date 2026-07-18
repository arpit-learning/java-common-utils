package dev.arpit.learning.commonUtils.utils;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import dev.arpit.learning.commonUtils.constants.CommonUtilLogConstants;
import dev.arpit.learning.logger.core.ILogger;
import dev.arpit.learning.logger.core.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import lombok.NonNull;

public class CSVUtils {
  private static final ILogger logger = LoggerFactory.getLogger(CSVUtils.class);

  public static boolean isNotEmptyRow(@NonNull String[] row) {
    for (String val : row) {
      if (!val.trim().isEmpty()) {
        return true;
      }
    }
    return false;
  }

  private static @NonNull String[] collectHeadersOrdered(
      @NonNull List<LinkedHashMap<String, String>> flatJson) {
    LinkedHashSet<String> headers = new LinkedHashSet<>();
    for (Map<String, String> linkedMap : flatJson) {
      headers.addAll(linkedMap.keySet());
    }
    return headers.toArray(new String[0]);
  }

  private static @NonNull String[] getRowOrdered(
      @NonNull String[] headers, @NonNull LinkedHashMap<String, String> map) {
    List<String> items = new ArrayList<>();

    for (String header : headers) {
      String value = map.get(header) == null ? "" : map.get(header).replace(",", "_");
      items.add(value);
    }

    return items.toArray(new String[0]);
  }

  private static @NonNull List<@NonNull String[]> getRowsOrdered(
      @NonNull String[] headers, @NonNull List<LinkedHashMap<String, String>> maps) {
    List<@NonNull String[]> rows = new ArrayList<>();

    for (LinkedHashMap<String, String> map : maps) {
      rows.add(getRowOrdered(headers, map));
    }

    return rows;
  }

  private static @NonNull Map<String, String> toRowMap(
      @NonNull String[] headers, @NonNull String[] row) {
    Map<String, String> rowMap = new LinkedHashMap<>();

    int length = Math.min(headers.length, row.length);
    for (int i = 0; i < length; i++) {
      rowMap.put(headers[i], row[i]);
    }

    for (int i = length; i < headers.length; i++) {
      rowMap.put(headers[i], "");
    }

    return rowMap;
  }

  public static void generateCSV(
      @NonNull String absoluteFilePath,
      @NonNull String[] headers,
      @NonNull List<@NonNull String[]> data,
      boolean shouldAppend) {
    try {
      CsvMapper csvMapper = new CsvMapper();
      CsvSchema.Builder schemaBuilder = CsvSchema.builder();
      for (String header : headers) {
        schemaBuilder.addColumn(header);
      }
      CsvSchema schema = schemaBuilder.build();

      File file = new File(absoluteFilePath);
      boolean fileAlreadyExists = file.exists() && file.length() > 0;

      if (isNotEmptyRow(headers) && (!shouldAppend || !fileAlreadyExists)) {
        schema = schema.withHeader();
      }

      try (Writer writer =
              new OutputStreamWriter(
                  new FileOutputStream(file, shouldAppend), StandardCharsets.UTF_8);
          SequenceWriter sequenceWriter = csvMapper.writer(schema).writeValues(writer)) {
        for (String[] row : data) {
          if (isNotEmptyRow(row)) {
            sequenceWriter.write(toRowMap(headers, row));
          }
        }
      }
    } catch (Exception e) {
      logger.error(CommonUtilLogConstants.WRITING_CSV_ERR, e);
    }
  }

  public static void generateCSV(
      @NonNull String absoluteFilePath,
      @NonNull List<LinkedHashMap<String, String>> flatJson,
      boolean shouldAppend) {
    String[] headers = collectHeadersOrdered(flatJson);
    List<@NonNull String[]> data = getRowsOrdered(headers, flatJson);
    generateCSV(absoluteFilePath, headers, data, shouldAppend);
  }
}
