package ru.pdt.st.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Collections {

  public static void main(String[] args) {

    System.out.println("Списки. Обращение к элементу списка с помощью указателя:");
    List<String> languages = Arrays.asList("Java", "C#", "Phyton", "PHP");

    for (String l : languages) {
      System.out.println("Я хочу выучить " + l);
    }
    
    System.out.println("Списки. Список объектов произвольного типа (не желательно):");
    List languagesObjectList = Arrays.asList("Java", "C#", "Phyton", "PHP");

    for (Object l : languagesObjectList) {
      System.out.println("Я хочу выучить " + l);
    }

    System.out.println("Списки. Обращение к элементу списка по индексу с помощью метода get(i):");
    List<String> languagesList = Arrays.asList("Java", "C#", "Phyton", "PHP");

    for (int i = 0; i < languagesList.size(); i++) {
      System.out.println("Я хочу выучить " + languagesObjectList.get(i));
    }

    System.out.println("Списки. Метод преобразования массива в списки Arrays.asList:");
    List<String> languagesArraysAsList = Arrays.asList("Java", "C#", "Phyton", "PHP");

    for (String l : languagesArraysAsList) {
      System.out.println("Я хочу выучить " + l);
    }

    System.out.println("Списки. Объявление интерфейса List. Создание экземпляра класса ArrayList:");
    List<String> languagesArrayList = new ArrayList<>();
    languagesArrayList.add("Java");
    languagesArrayList.add("C#");
    languagesArrayList.add("Phyton");

    for (String l : languagesArrayList) {
      System.out.println("Я хочу выучить " + l);
    }

    String[] langs = {"Java", "C#", "Phyton", "PHP"};

    System.out.println("Массивы. Переход на использование ссылки на элемент массива:");
    for (String l : langs) {
      System.out.println("Я хочу выучить " + l);
    }

    System.out.println("Массивы. Инициализация и заполнение массива. Обращение по индексу элемента массива:");
    for (int i = 0; i < langs.length; i++) {
      System.out.println("Я хочу выучить " + langs[i]);
    }

  }
}
