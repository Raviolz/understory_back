package raviolz.understory_back.services;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Booking;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.exceptions.InternalServerException;

@Service
public class EmailService {

    private final String domainName;
    private final String apiKey;
    private final String from;
    private final String baseUrl;

    public EmailService(@Value("${mailgun.domain}") String domainName,
                        @Value("${mailgun.api.key}") String apiKey,
                        @Value("${mailgun.from}") String from,
                        @Value("${mailgun.baseurl}") String baseUrl) {
        this.domainName = domainName;
        this.apiKey = apiKey;
        this.from = from;
        this.baseUrl = baseUrl;
    }

    public void sendRegistrationEmail(User recipient) {
        String message = """
                Ciao %s,
                
                l'archivio è aperto.
                
                Non tutto ciò che merita di essere osservato viene indicato chiaramente.
                
                Buona esplorazione,
                Understory
                """.formatted(recipient.getName());

        HttpResponse<JsonNode> response = Unirest.post(this.baseUrl + "/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", this.from)
                .queryString("to", recipient.getEmail())
                .queryString("subject", "Accesso confermato")
                .queryString("text", message)
                .asJson();

        if (response.getStatus() >= 400) {
            throw new InternalServerException("Errore durante l'invio dell'email");
        }
    }


    public void sendBookingConfirmationEmail(Booking booking) {
        String message = """
                Ciao %s,
                
                la tua prenotazione è confermata.
                
                Reward: %s
                Luogo: %s
                Data: %s
                Persone: %d
                
                Mostra questa email o il reward dalla tua area personale quando arrivi.
                
                Buona esplorazione,
                Understory
                """.formatted(
                booking.getUserReward().getUser().getName(),
                booking.getUserReward().getReward().getTitle(),
                booking.getUserReward().getReward().getBusiness().getName(),
                booking.getBookingDate(),
                booking.getPeopleCount()
        );

        HttpResponse<JsonNode> response = Unirest.post(this.baseUrl + "/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", this.from)
                .queryString("to", booking.getUserReward().getUser().getEmail())
                .queryString("subject", "Prenotazione confermata")
                .queryString("text", message)
                .asJson();

        if (response.getStatus() >= 400) {
            throw new InternalServerException("Errore durante l'invio dell'email di conferma prenotazione");
        }
    }

    public void sendCustomEmail(String to, String subject, String text) {
        HttpResponse<JsonNode> response = Unirest.post(this.baseUrl + "/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", this.from)
                .queryString("to", to)
                .queryString("subject", subject)
                .queryString("text", text)
                .asJson();

        if (response.getStatus() >= 400) {
            throw new InternalServerException("Errore durante l'invio dell'email");
        }
    }
}