package com.tek.core.service;

import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_MAIL_SERVICE;
import static com.tek.core.constants.TekCoreBeanNames.TEK_CORE_WEB_MVC_FILE_TIMESTAMP;
import static java.lang.String.join;

import com.tek.core.aop.CanSendMail;
import com.tek.core.config.directory.TekTmpDirConfiguration;
import com.tek.core.dto.FileAttachmentDto;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;

//TODO noreplay address handling and principal info
//TODO see if we can provide error timestamp to file
//TODO testing with charset and/or encoding
//TODO see if we can remove the TekTmpDirFileService dependency and create an exception msg on the fly

/**
 * Utility service to send mail.
 * <p>
 * The service uses the {@link Async} feature to assure the email is sent in a non thread-blocking
 * response for the caller.
 *
 * @author MarcoPagan
 */
@ConditionalOnBean(TekTmpDirConfiguration.class)
@Service(TEK_CORE_MAIL_SERVICE)
@Async
@Slf4j
public class TekMailService {

  @Autowired
  @Qualifier(TEK_CORE_WEB_MVC_FILE_TIMESTAMP)
  private SimpleDateFormat df;

  @Autowired private TekTmpDirFileService tmpFileService;
  @Autowired private ApplicationContext context;
  @Autowired private JavaMailSender mailSender;

  private final String newLine = System.getProperty("line.separator");

  @SuppressWarnings("unused")
  @Value("${spring.mail.username}")
  private String host;

  /**
   * Sends a {@link Exception} message as an attachment, including {@link ServletWebRequest} as
   * text.
   * <p>Requires a Servlet Context to be executed.</p>
   */
  @CanSendMail
  public void sendRequestExceptionMessage(
      @NonNull ServletWebRequest servletWebRequest,
      @NonNull Exception exception
  ) throws IOException {
    final var request = servletWebRequest.getRequest();
    final var requestUrl = request.getRequestURL().toString();
    final var to = new String[]{host};
    final var addresses = Arrays.toString(to);
    String subject = context.getApplicationName();
    File file;
    try {
      file = tmpFileService.createInTmpDir(df.format(new Date()) + "_exception.txt");
    } catch (IOException e) {
      log.error("Could not create file", e);
      throw new IOException("Could not send mail", e);
    }
    String text = join("")
        .concat("Exception on: " + subject)
        .concat(newLine)
        .concat("Request URL: " + requestUrl);
    logMailSending(addresses);
    try (
        final var out = new BufferedWriter(new FileWriter(file, true));
        final var pWriter = new PrintWriter(out, true);
    ) {
      exception.printStackTrace(pWriter);
      final var attachment = new FileAttachmentDto(file, null, "UTF-8");
      sendWithAttachment(to, subject, text, attachment);
    } catch (Exception ex) {
      logMailError(Arrays.toString(to), ex);
    }
    logMailSuccess(addresses);
  }

  /**
   * Sends a mail message with a text and an attachment
   */
  @CanSendMail
  public void sendWithAttachment(
      @NonNull String[] to,
      @NonNull String subject,
      @NonNull String text,
      @NonNull FileAttachmentDto dto
  ) {
    var toArray = Arrays.toString(to);
    logMailSending(toArray);
    try {
      String addresses = join(",", to);
      final var mimeMessage = mailSender.createMimeMessage();
      mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresses));
      mimeMessage.setSubject(subject);
      Multipart emailContent = new MimeMultipart();
      final var textBodyPart = new MimeBodyPart();
      textBodyPart.setText(text);
      final var fileBodyPart = new MimeBodyPart();
      fileBodyPart.attachFile(dto.getFile(), dto.getContentType(), dto.getEncoding());
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
  @CanSendMail
  public void sendSimpleMessage(
      @NonNull String[] to,
      @NonNull String subject,
      @NonNull String text
  ) {
    final var toArray = Arrays.toString(to);
    logMailSending(toArray);
    final var simpleMailMessage = new SimpleMailMessage();
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

  private void logMailSending(@NonNull String addresses) {
    log.debug("Sending mail to: {}", addresses);
  }

  private void logMailSuccess(@NonNull String addresses) {
    log.debug("Email sent to: {}", addresses);
  }

  private void logMailError(@NonNull String addresses, @NonNull Exception ex) {
    log.error(
        "Unable to send mail to: {}, cause: [{}]",
        addresses,
        ExceptionUtils.getStackTrace(ex)
    );
  }
}
