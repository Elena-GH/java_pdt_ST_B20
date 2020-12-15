package ru.pdt.st.mantis.model;

public class MailMessage {

  // Получатель сообщения
  public String to;
  // Текст сообщения
  public String text;

  public MailMessage(String to, String text) {
    this.to = to;
    this.text = text;
  }

}
