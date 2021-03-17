package com.tek.core.service;

import static java.lang.String.join;

import com.tek.core.TekCoreProperties;
import com.tek.core.util.TekDateFormatter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * Utility service to send mail.
 * <p>
 * The service uses the {@link Async} feature to assure the email is sent in a non thread-blocking
 * response for the client.
 *
 * @author MarcoPagan
 */
@Service
@Async
@RequiredArgsConstructor
@Slf4j
public class TekMailService {

  @NonNull private final TekCoreProperties coreProperties;
  @NonNull private final TekFileService fileService;
  @NonNull private final JavaMailSender mailSender;
  @NonNull private final TekDateFormatter dateFormatter;
  @NonNull private final ApplicationContext context;

  private final String newLine = System.getProperty("line.separator");

  @SuppressWarnings("unused")
  @Value("${spring.mail.username}")
  private String host;

  //TODO noreplay address handling and principal info
  //TODO see if we can provide error timestamp to file

  /**
   * Sends a {@link RuntimeException} message as an attachment, including {@link ServletWebRequest}
   * as text.
   */
  public void sendExceptionMessage(
      ServletWebRequest servletWebRequest,
      RuntimeException exception
  ) {
    val request = servletWebRequest.getRequest();
    val requestUrl = request.getRequestURL().toString();

    String[] to = new String[]{host};
    String subject = context.getApplicationName();
    String filename = fileService.createInTmpDir(
        dateFormatter.fileTimestampFormat().format(new Date()) + "_exception.txt"
    );

    String text = join("")
        .concat("Exception on: " + subject)
        .concat(newLine)
        .concat("Request URL: " + requestUrl);

    try (
        BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
        PrintWriter pWriter = new PrintWriter(out, true);
    ) {
      exception.printStackTrace(pWriter);
      sendWithAttachment(to, subject, text, filename);
    } catch (Exception ex) {
      logMailError(Arrays.toString(to), ex);
    }
  }

  /**
   * Sends a mail message with a text and an attachment
   */
  public void sendWithAttachment(String[] to, String subject, String text, String file) {
    String toArray = Arrays.toString(to);
    logMailSending(toArray);

    try {
      String addresses = join(",", to);
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresses));
      mimeMessage.setSubject(subject);

      Multipart emailContent = new MimeMultipart();

      MimeBodyPart textBodyPart = new MimeBodyPart();
      textBodyPart.setText(text);

      MimeBodyPart fileBodyPart = new MimeBodyPart();
      fileBodyPart.attachFile(file);

      emailContent.addBodyPart(textBodyPart);
      emailContent.addBodyPart(fileBodyPart);

      mimeMessage.setContent(emailContent);
      mailSender.send(mimeMessage);
    } catch (Exception ex) {
      logMailError(toArray, ex);
      return;
    }
    logMailSuccess(toArray);
  }

  /**
   * Sends a mail message with a simple text
   */
  public void sendSimpleMessage(String[] to, String subject, String text) {
    String toArray = Arrays.toString(to);
    logMailSending(toArray);

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(to);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(text);

    try {
      mailSender.send(simpleMailMessage);
    } catch (Exception ex) {
      logMailError(toArray, ex);
      return;
    }
    logMailSuccess(toArray);
  }

  private void logMailSending(String addresses) {
    log.debug("Sending mail to: {}", addresses);
  }

  private void logMailSuccess(String addresses) {
    log.debug("Email sent to: {}", addresses);
  }

  private void logMailError(String addresses, Exception ex) {
    log.error(
        "Unable to send mail to: {}, cause: [{}]",
        addresses,
        ExceptionUtils.getStackTrace(ex)
    );
  }
}
