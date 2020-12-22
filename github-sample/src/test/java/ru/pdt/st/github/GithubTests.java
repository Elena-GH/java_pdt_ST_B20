package ru.pdt.st.github;

import com.google.common.collect.ImmutableMap;
import com.jcabi.aspects.Immutable;
import com.jcabi.github.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class GithubTests {

  @Test
  public void testCommits() throws IOException {
    // Установка соединения
    Github github = new RtGithub("f578528f28f9edc3c0c966ec4b5b397d6fd72291");
    // Получение списка всех commit'ов по проекту
    RepoCommits commits = github.repos().get(new Coordinates.Simple("Elena-GH", "java_pdt_ST_B20")).commits();
    // Получаем информацию о commit'е и выводим ее на консоль
    // Для получения всех commit'ов в iterate передается пустой набор пар (maping)
    for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String>().build())) {
      System.out.println(new RepoCommit.Smart(commit).message());
    }
  }
}
