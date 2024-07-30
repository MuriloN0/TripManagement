package com.rocketseat.planner.trip;


import com.rocketseat.planner.activity.ActivityData;
import com.rocketseat.planner.activity.ActivityRequestPayLoad;
import com.rocketseat.planner.activity.ActivityResponse;
import com.rocketseat.planner.activity.ActivityService;
import com.rocketseat.planner.link.LinkData;
import com.rocketseat.planner.link.LinkRequestPayLoad;
import com.rocketseat.planner.link.LinkResponse;
import com.rocketseat.planner.link.LinkService;
import com.rocketseat.planner.participant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController //
@RequestMapping("/trips") // Mapeia o endpoint que ele fica ouvindo;
// COm isso, toda vez que alguém mandar uma requisição para o "/trips", ele sabe rque tem que executar a class abaixo;
public class TripController {

    //Vamos usar o partipants Services. Segue a declaração
    @Autowired // Serve para o Spring fazer a injeção de dependencia para nós
    private ParticipantService paticipantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private TripRepository repository;

    //--------------- TRIP -----------------------------------------------------------------------------------------
    // nóa vamos mapear o body para recebermos as informações da viagem;
    //Vamos criar uma classe para representar essa request
    @PostMapping // Falando que vai ser usado no método post.
    //Post vai postar dados novos.
     public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayLoad payLoad){
         Trip newTrip = new Trip(payLoad);

         this.repository.save(newTrip); // Salvar a viagem que queremos;

         this.paticipantService.registerParticipantsToEvent(payLoad.emails_to_invite(), newTrip);

         return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
     }

     //Gwt vai recuperar os dados.
     @GetMapping("/{id}")
     public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id){
        Optional<Trip> trip = this.repository.findById(id);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
     }

     //Put para atualização de informações
     @PutMapping("/{id}")
     public ResponseEntity<Trip> updateTrip(@PathVariable UUID id,@RequestBody TripRequestPayLoad payLoad){
         Optional<Trip> trip = this.repository.findById(id);

         if(trip.isPresent()){
             Trip rawTrip = trip.get();
             rawTrip.setEndsAt(LocalDateTime.parse(payLoad.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
             rawTrip.setStartsAt(LocalDateTime.parse(payLoad.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
             rawTrip.setDestination(payLoad.destination());

             this.repository.save(rawTrip);

             return ResponseEntity.ok(rawTrip);
         }

         return ResponseEntity.notFound().build();
     }

     //confirmação da viagem.
     @GetMapping("/{id}/confirm")
     public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id){
         Optional<Trip> trip = this.repository.findById(id);

         if(trip.isPresent()){
             Trip rawTrip = trip.get();
             rawTrip.setIsConfirmed(true);


             this.repository.save(rawTrip);
             this.paticipantService.triggerConfirmationEmailToPaticipants(id);

             return ResponseEntity.ok(rawTrip);
         }

         return ResponseEntity.notFound().build();
     }

    //--------------- ACTIVITY -----------------------------------------------------------------------------------------
     @PostMapping("/{id}/activities") // Falando que vai ser usado no método post.
     //Post vai postar dados novos.
     public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id,  @RequestBody ActivityRequestPayLoad payLoad){
        Optional<Trip> trip = this.repository.findById(id);

         if(trip.isPresent()){
             Trip rawTrip = trip.get();

             ActivityResponse activityResponse = this.activityService.registerActivity(payLoad, rawTrip);

             return ResponseEntity.ok(activityResponse);
         }

         return ResponseEntity.notFound().build();
     }

     @GetMapping("/{id}/activities")
     public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){
         List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);

         return ResponseEntity.ok(activityDataList);
     }

     //--------------- PARTICIPANT -----------------------------------------------------------------------------------------
     @PostMapping("/{id}/invite") // Falando que vai ser usado no método post.
        //Post vai postar dados novos.
     public ResponseEntity<ParticicpantCreateResponse> inviteParticipant(@PathVariable UUID id,  @RequestBody ParticipantRequestPayLoad payLoad){
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()){
                Trip rawTrip = trip.get();

                List<String> participantToInvite = new ArrayList<>();
                participantToInvite.add(payLoad.email());

                ParticicpantCreateResponse ParticipantResponse = this.paticipantService.registerParticipantToEvent(payLoad.email(), rawTrip);

                if(rawTrip.getIsConfirmed()) this.paticipantService.triggerConformationEmailToParticipant(payLoad.email());

                return ResponseEntity.ok(ParticipantResponse);
        }

        return ResponseEntity.notFound().build();
     }


     @GetMapping("/{id}/participants")
     public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id){

         List<ParticipantData> participantList = this.paticipantService.getAllParticipantsFromEvent(id);

         return ResponseEntity.ok(participantList);
     }

    //--------------- LINK -----------------------------------------------------------------------------------------
    @PostMapping("/{id}/links") // Falando que vai ser usado no método post.
    //Post vai postar dados novos.
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayLoad payLoad){
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            LinkResponse linkResponse = this.linkService.registerLink(payLoad, rawTrip);

            return ResponseEntity.ok(linkResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLink(@PathVariable UUID id){
        List<LinkData> LinkDataList = this.linkService.getAllLinkFromId(id);

        return ResponseEntity.ok(LinkDataList);
    }
}
