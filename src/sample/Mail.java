package sample;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Mail {
    private String recipient;
    private String sender;
    private String subject;
    private String text;
    private String password;
    private Properties props;



    private File file;
    private File photo;


    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public Mail(){
        props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
    }

    public void send(){
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(sender));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            //Заголовок письма
            message.setSubject(subject);
            //Содержимое
            //message.setText(text);


            Multipart multipart = new MimeMultipart();

           MimeBodyPart attachment1 = new MimeBodyPart();
           attachment1.attachFile(file);

            MimeBodyPart attachment2 = new MimeBodyPart();
            attachment2 .attachFile(photo);

            MimeBodyPart mtext = new MimeBodyPart();
            mtext.setText(text);

            multipart.addBodyPart(attachment1);
            multipart.addBodyPart(attachment2);
            multipart.addBodyPart(mtext);

            message.setContent(multipart);

            //Отправляем сообщение
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setText(String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return "Mail{" +
                "recipient='" + recipient + '\'' +
                ", sender='" + sender + '\'' +
                ", password='" + password + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

}
