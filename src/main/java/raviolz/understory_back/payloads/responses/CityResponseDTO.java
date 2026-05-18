package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.City;

import java.util.UUID;

public record CityResponseDTO(UUID id, String name, String country, double longitude, double latitude,
                              String description, String coverImageUrl
) {
    public static CityResponseDTO fromEntity(City city) { // il metodo find mi restituisce una entity con il metodo from entity riesco a tenere il controller piu' pulito
        return new CityResponseDTO( // perche' il dto sa ocme costruirsi partendo dalla entity .. evito di ripetermi. Il service restituisce l entity il controller la trasforma
                // service logica interna /= controller: cosa espongo
                city.getId(),
                city.getName(),
                city.getCountry(),
                city.getLongitude(),
                city.getLatitude(),
                city.getDescription(),
                city.getCoverImageUrl()
        );
    }
}