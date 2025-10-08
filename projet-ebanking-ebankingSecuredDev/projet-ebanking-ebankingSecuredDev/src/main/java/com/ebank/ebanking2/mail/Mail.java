package com.ebank.ebanking2.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class Mail {


        @Autowired
        private JavaMailSender send;

    public Mail(JavaMailSender send) {
        this.send = send;
    }

    public void javasend(String email, String generatetoken, String subject, String body) throws MessagingException {
            MimeMessage m=send.createMimeMessage();

            String html="<style>\r\n"
                    + "        /* Reset styles for email clients */\r\n"
                    + "        body {\r\n"
                    + "            margin: 0;\r\n"
                    + "            padding: 0;\r\n"
                    + "            font-family: 'Arial', sans-serif;\r\n"
                    + "            background-color: #f5f7fb;\r\n"
                    + "            color: #333333;\r\n"
                    + "            line-height: 1.6;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        /* Container styles */\r\n"
                    + "        .email-container {\r\n"
                    + "            max-width: 600px;\r\n"
                    + "            margin: 0 auto;\r\n"
                    + "            padding: 20px;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        /* Header styles */\r\n"
                    + "        .header {\r\n"
                    + "            text-align: center;\r\n"
                    + "            padding: 30px 0;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        .logo {\r\n"
                    + "            width: 150px;\r\n"
                    + "            height: auto;\r\n"
                    + "            margin-bottom: 20px;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        /* Main content styles */\r\n"
                    + "        .content {\r\n"
                    + "            background-color: #ffffff;\r\n"
                    + "            padding: 40px;\r\n"
                    + "            border-radius: 10px;\r\n"
                    + "            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\r\n"
                    + "            margin-bottom: 20px;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        h1 {\r\n"
                    + "            color: #1976d2;\r\n"
                    + "            font-size: 24px;\r\n"
                    + "            margin-bottom: 20px;\r\n"
                    + "            text-align: center;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        p {\r\n"
                    + "            margin-bottom: 20px;\r\n"
                    + "            color: #555555;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        /* Button styles */\r\n"
                    + "        .button-container {\r\n"
                    + "            text-align: center;\r\n"
                    + "            margin: 30px 0;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        .button {\r\n"
                    + "            display: inline-block;\r\n"
                    + "            padding: 15px 30px;\r\n"
                    + "            background-color: #1976d2;\r\n"
                    + "            color: #ffffff;\r\n"
                    + "            text-decoration: none;\r\n"
                    + "            border-radius: 5px;\r\n"
                    + "            font-weight: bold;\r\n"
                    + "            text-align: center;\r\n"
                    + "            transition: background-color 0.3s ease;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        .button:hover {\r\n"
                    + "            background-color: #1565c0;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        /* Security notice styles */\r\n"
                    + "        .security-notice {\r\n"
                    + "            background-color: #f8f9fa;\r\n"
                    + "            padding: 15px;\r\n"
                    + "            border-radius: 5px;\r\n"
                    + "            margin-top: 20px;\r\n"
                    + "            border-left: 4px solid #ffd700;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        .security-notice p {\r\n"
                    + "            margin: 0;\r\n"
                    + "            font-size: 14px;\r\n"
                    + "            color: #666666;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        /* Footer styles */\r\n"
                    + "        .footer {\r\n"
                    + "            text-align: center;\r\n"
                    + "            padding: 20px;\r\n"
                    + "            color: #666666;\r\n"
                    + "            font-size: 12px;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        .social-links {\r\n"
                    + "            margin-bottom: 20px;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        .social-link {\r\n"
                    + "            display: inline-block;\r\n"
                    + "            margin: 0 10px;\r\n"
                    + "            color: #1976d2;\r\n"
                    + "            text-decoration: none;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        .help-text {\r\n"
                    + "            margin-top: 20px;\r\n"
                    + "            font-size: 14px;\r\n"
                    + "            color: #888888;\r\n"
                    + "        }\r\n"
                    + "\r\n"
                    + "        /* Responsive design */\r\n"
                    + "        @media screen and (max-width: 600px) {\r\n"
                    + "            .email-container {\r\n"
                    + "                padding: 10px;\r\n"
                    + "            }\r\n"
                    + "\r\n"
                    + "            .content {\r\n"
                    + "                padding: 20px;\r\n"
                    + "            }\r\n"
                    + "\r\n"
                    + "            h1 {\r\n"
                    + "                font-size: 20px;\r\n"
                    + "            }\r\n"
                    + "        }\r\n"
                    + "    </style>\r\n"
                    + "\r\n"
                    + "<body>\r\n"
                    + "    <div class=\"email-container\">\r\n"

                    + "\r\n"
                    + "        <div class=\"content\">\r\n"
                    + "            <h1>Définissez de token</h1>\r\n"
                    + "            \r\n"
                    + "            <p>Bonjour [Prénom],</p>\r\n"
                    + "            \r\n"
                    + "            <p>Bienvenue dans notre application de gestion RH ! Pour commencer à utiliser votre compte, veuillez définir votre mot de passe en cliquant sur le bouton ci-dessous :</p>\r\n"
                    + "\r\n"
                    + "            <div class=\"button-container\">\r\n"
                    + "                <a href=\"link\" class=\"button\">Définir mon mot de passe</a>\r\n"
                    + "            </div>\r\n"
                    + "\r\n"
                    + "            <p>Ce lien expiirera dans 24 heures pour des raisons de sécurité. Si vous n'avez pas demandé à définir votre mot de passe, veuillez ignorer cet email.</p>\r\n"
                    + "\r\n"
                    + "            <div class=\"security-notice\">\r\n"
                    + "                <p><strong>🔒 Note de sécurité :</strong> Nous ne vous demanderons jamais votre token par email. Si vous recevez un tel message, ne cliquez pas sur les liens et contactez-nous immédiatement.</p>\r\n"
                    + "            </div>\r\n"
                    + "        </div>\r\n"
                    + "\r\n"
                    + "        <div class=\"footer\">\r\n"
                    + "            <div class=\"social-links\">\r\n"
                    + "                <a href=\"#\" class=\"social-link\">LinkedIn</a>\r\n"
                    + "                <a href=\"#\" class=\"social-link\">Twitter</a>\r\n"
                    + "                <a href=\"#\" class=\"social-link\">Facebook</a>\r\n"
                    + "            </div>\r\n"
                    + "\r\n"
                    + "            <p>© 2024 Votre Entreprise. Tous droits réservés.</p>\r\n"
                    + "            \r\n"
                    + "            <div class=\"help-text\">\r\n"
                    + "                <p>Besoin d'aide ? Contactez notre équipe support à support@votreentreprise.com</p>\r\n"
                    + "            </div>\r\n"
                    + "        </div>\r\n"
                    + "    </div>\r\n"
                    + "</body>";
            String dynamicUrl = generatetoken;
            String finalHtml = html.replace("Définir mon mot de passe", dynamicUrl);
            MimeMessageHelper message=new MimeMessageHelper(m,true,"UTF-8");

            message.setTo("bilallaariny01@gmail.com");
            message.setSubject(subject);
            message.setText(finalHtml,true);
            send.send(m);
            System.out.print("salut");

        }

    public void sendTokenEmail(String email, String token, String subject,String type) throws MessagingException {
        MimeMessage message = send.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String html ="<html>" +
                "<body style='font-family: Arial, sans-serif; color: #000;'>"
                + "<h2 style='color: #004080;'>Confirmation de votre " + type + "</h2>"
                + "<p>Vous avez demandé la génération d’un nouveau code de sécurité (" + type + ") pour votre compte bancaire.</p>"
                + "<p>Voici votre code de vérification à usage unique :</p>"
                + "<div style='background-color:#004080; padding: 20px; text-align: center; font-size: 24px; font-weight: bold; color:#ffffff; border-radius: 5px;'>"
                + token
                + "</div>"
                + "<p style='margin-top: 20px;'>Ne partagez jamais ce code avec qui que ce soit. La banque ne vous demandera jamais ce code par téléphone ou par e-mail.</p>"
                + "<p>Si vous n’êtes pas à l’origine de cette demande, veuillez contacter immédiatement notre service client.</p>"
                + "<p>Service client : <a href='mailto:bankinho.mybyhi@gmail.com'>bankinho.mybyhi@gmail.com</a></p>"
                + "<p style='margin-top: 30px;'>Merci de faire confiance à <strong>Bankino</strong>.</p>"
                + "</body></html>";


        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(html, true);

        send.send(message);
        System.out.println("Token email envoyé à " + email);
    }

}
