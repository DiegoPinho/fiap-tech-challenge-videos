package com.fiap.techchallenge.diegopinho.videos.controllers.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  @Override
  public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {

    System.out.println("PASSEI NESTE MERDA");

    String dateAsString = jsonParser.getText();
    return LocalDateTime.parse(dateAsString, formatter);
  }
}
