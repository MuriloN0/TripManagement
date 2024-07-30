package com.rocketseat.planner.trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity //Usamos esse @ para falar que essa classe representa uma entidade.
@Table(name = "trips") // Aqui é para ele mapear e saber qual o nome da nossa tabela.
@Getter
@Setter // Para gerar automaticamente os getters e setters;
@NoArgsConstructor
@AllArgsConstructor // colocamos para quando formos fazer consultas aos objetos do tipo trip, ele ja vai construir automaticamento;
public class Trip {
    @Id // Esse vai dizer que o ID é a chave primaria;
    @GeneratedValue (strategy = GenerationType.AUTO) // Esse vai gerar o valor automáticamente;
    private UUID id;

    @Column (nullable = false) // Colocamos esse @ para indifcar que se trata de uma coluna na tabela;
    private String destination;

    @Column (name = "starts_at", nullable = false) // Agora usamos esse para falar que o nome da nossa coluna é aquele;
    private LocalDateTime startsAt;

    @Column (name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column (name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column (name = "owner_name", nullable = false)
    private String ownerName;

    @Column (name = "owner_email", nullable = false)
    private String ownerEmail;

    // Abaixo construimos um construtor publico;
     public Trip(TripRequestPayLoad data){
         this.destination = data.destination();
         this.isConfirmed = false;
         this.ownerEmail = data.owner_email();
         this.ownerName = data.owner_name();
         this.startsAt = LocalDateTime.parse(data.starts_at(), DateTimeFormatter.ISO_DATE_TIME); //Vamos fazer aqui uma transformação para o localDateTime usando a capaciade de parse dele.
         this.endsAt = LocalDateTime.parse(data.ends_at(), DateTimeFormatter.ISO_DATE_TIME);


     }


    public UUID getId() { //Deixei esse get, pois o comando não criou automatico.
        return id;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }


    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerEmail() {

        return ownerEmail;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public boolean getIsConfirmed() {

         return isConfirmed;
    }
}
