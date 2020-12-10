package ru.pdt.st.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.pdt.st.addressbook.model.ContactData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ContactDataGenerator {

  @Parameter(names = "-c", description = "Group count")
  public int count;

  @Parameter(names = "-f", description = "Target file")
  public String file;

  @Parameter(names = "-d", description = "Data format")
  public String format;

  public static void main(String[] args) throws IOException {
    ContactDataGenerator generator = new ContactDataGenerator();
    // new JCommander(generator, args); - Deprecated API usage
    JCommander jCommander = JCommander.newBuilder().addObject(generator).build();
    try {
      jCommander.parse(args);
    } catch (ParameterException ex) {
      // Вывод на консоль описания доступных опций запуска теста
      jCommander.usage();
      return;
    }
    generator.run();
  }

  private void run() throws IOException {
    List<ContactData> contacts = generateContacts(count);
    if (format.equals("json")) {
      saveAsJson(contacts, new File(file));
    } else {
      System.out.println("Unrecognized format " + format);
    }
  }

  private void saveAsJson(List<ContactData> contacts, File file) throws IOException {
    // System.out.println(new File(".").getAbsolutePath());
    Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    String json = gson.toJson(contacts);
    try (Writer writer = new FileWriter(file)) {
      writer.write(json);
    }
  }

  private List<ContactData> generateContacts(int count) {
    List<ContactData> contacts = new ArrayList<ContactData>();
    for (int i = 0; i < count; i++) {
      contacts.add(new ContactData()
              .withFirstName(String.format("First_Name %s", i + 1))
              .withLastName(String.format("Last_Name %s", i + 1))
              .withAddress(String.format("123456, г.Москва, Московский пр-т, д.%s, стр.1", i + 1))
              .withMobilePhone("+7 (123) 123-45-67")
              .withWorkPhone("+7 (456) 123-45-67")
              .withEmail(String.format("mail_%s@gmail.com", i + 1))
              .withEmail2(String.format("mail2_%s@gmail.com", i + 1))
              .withGroup("Group_Name"));
    }
    return contacts;
  }

}
