package ru.pdt.st.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import ru.pdt.st.addressbook.model.GroupData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class GroupDataGenerator {

  @Parameter(names = "-c", description = "Group count")
  public int count;

  @Parameter(names = "-f", description = "Target file")
  public String file;

  public static void main(String[] args) throws IOException {
    GroupDataGenerator generator = new GroupDataGenerator();
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
    List<GroupData> groups = generateGroups(count);
    save(groups, new File(file));
  }

  private void save(List<GroupData> groups, File file) throws IOException {
    System.out.println(new File(".").getAbsolutePath());
    Writer writer = new FileWriter(file);
    for (GroupData group : groups) {
      writer.write(String.format("%s;%s;%s\n", group.getName(), group.getHeader(), group.getFooter()));
    }
    // Принудительное завершения записи в файл данных из кеша
    writer.close();
  }

  private List<GroupData> generateGroups(int count) {
    List<GroupData> groups = new ArrayList<GroupData>();
    for (int i = 0; i < count; i++) {
      groups.add(new GroupData()
              .withName(String.format("Group_Name_%s", i + 1))
              .withHeader(String.format("Group_Header_%s", i + 1))
              .withFooter(String.format("Group_Footer_%s", i + 1)));
    }
    return groups;
  }
}
