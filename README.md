# Understory — Backend

> **Esplorare una città significa anche scoprire ciò che normalmente rimane sotto la superficie.**

🇮🇹 **Italiano**  
🇬🇧 [English version below](#english-version)

---

## Il progetto

**Understory** è una piattaforma full stack nata con l'obiettivo di rendere
l'esplorazione urbana — e, più in generale, la scoperta di un luogo —
meno passiva e più coinvolgente.

L'idea è quella di raccontare soprattutto **storie meno conosciute, curiosità
locali e piccoli dettagli** che spesso rimangono ai margini dei percorsi
turistici tradizionali, oppure di proporre luoghi già noti attraverso una
chiave di lettura differente.

A differenza delle classiche guide turistiche, Understory accompagna l'utente
all'interno di **piccole esperienze narrative e interattive**. Ogni percorso
conduce progressivamente verso una prova o una micro-sfida: completandola,
l'utente sblocca la parte conclusiva della storia e può far progredire
il proprio profilo.

Il nome **Understory** richiama proprio questa idea: qualcosa che si trova
sotto la superficie, uno strato nascosto della città che può emergere
osservando, seguendo una traccia e mettendosi in gioco.

Il progetto non nasce come videogioco, ma come **strumento divulgativo e
didattico interattivo**, integrando anche attività locali e ricompense
per creare un collegamento tra esperienza digitale e territorio reale.

> Per il flusso utente completo, l'interfaccia e gli screenshot del progetto:  
> [Understory Frontend](https://github.com/Raviolz/understory_front)

---

## ⚙️ Backend

Il backend di Understory è una **REST API sviluppata con Java e Spring Boot**.

Gestisce la logica applicativa, l'autenticazione, la persistenza dei dati,
la progressione degli utenti, il gameplay, le ricompense, le prenotazioni,
gli upload di immagini e il backoffice amministrativo.

L'applicazione segue una struttura a livelli:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

DTO dedicati separano inoltre i payload in ingresso dalle response restituite
al frontend.

---

### Tecnologie utilizzate

| Tecnologia             | Utilizzo                                       |
|------------------------|------------------------------------------------|
| **Java**               | Linguaggio principale del backend              |
| **Spring Boot**        | Configurazione e avvio dell'applicazione       |
| **Spring Web MVC**     | Controller REST ed endpoint HTTP               |
| **Spring Data JPA**    | Accesso e persistenza dei dati                 |
| **Hibernate / JPA**    | Mapping tra entità Java e database relazionale |
| **PostgreSQL**         | Database relazionale                           |
| **Spring Security**    | Autenticazione e autorizzazione                |
| **JWT / JJWT**         | Token di autenticazione stateless              |
| **BCrypt**             | Hash delle password                            |
| **Jakarta Validation** | Validazione dei payload                        |
| **Lombok**             | Riduzione del boilerplate                      |
| **Cloudinary**         | Upload e hosting delle immagini                |
| **Mailgun**            | Invio delle email tramite API HTTP             |
| **Maven**              | Gestione delle dipendenze e build              |

---

### 🧱 Struttura del progetto

La codebase separa le principali responsabilità dell'applicazione:

```text
src/main/java/raviolz/understory_back/
├── config/        # configurazioni esterne
├── controllers/   # endpoint REST
│   ├── auth/
│   ├── backoffice/
│   ├── gameplay/
│   ├── me/
│   └── publicapi/
├── entities/      # entità JPA
├── enums/         # stati e tipologie applicative
├── exceptions/    # eccezioni e gestione globale degli errori
├── payloads/      # DTO di request e response
├── repositories/  # accesso ai dati con Spring Data JPA
├── runners/       # seed iniziale di ruoli e account amministrativi
├── security/      # JWT, filtri e configurazione Spring Security
└── services/      # logica applicativa
```

Questa organizzazione permette di mantenere distinti endpoint, business logic
e persistenza.

---

### 🗄️ Modello dati e JPA

La persistenza utilizza **Spring Data JPA** e PostgreSQL.

Le entità principali comprendono:

- `User` e `Role`;
- `City`;
- `PointOfInterest`;
- `Experience` ed `ExperienceCategory`;
- `QuizGame` e `UploadGame`;
- `UserExperienceProgress`;
- `UserUploadSubmission`;
- `LocalBusiness` e `BusinessCategory`;
- `Reward` e `UserReward`;
- `Booking`.

Le relazioni JPA modellano il collegamento tra luoghi fisici, contenuti
narrativi, attività dell'utente e realtà locali.

Tra le principali relazioni:

```text
City
 ├── PointOfInterest
 │     └── Experience
 │           ├── QuizGame / UploadGame
 │           ├── UserExperienceProgress
 │           └── UserUploadSubmission
 │
 └── LocalBusiness
       └── Reward
             └── UserReward
                   └── Booking
```

Per le chiavi primarie vengono utilizzati **UUID**.

Sono inoltre presenti constraint di unicità per impedire, ad esempio,
duplicazioni dello stesso progresso, reward o upload per la stessa coppia
utente/esperienza.

---

### 🔐 Autenticazione e sicurezza

L'autenticazione è basata su **JWT** e Spring Security.

Il flusso principale è:

```text
Registrazione
    ↓
Password salvata con BCrypt
    ↓
Login
    ↓
Verifica delle credenziali
    ↓
Generazione JWT
    ↓
Authorization: Bearer <token>
    ↓
TokenFilter
    ↓
SecurityContext
```

Le password vengono codificate tramite `BCryptPasswordEncoder`, mentre il JWT
contiene l'identificativo dell'utente e viene firmato tramite chiave segreta.

Il backend utilizza una session policy **stateless**: non viene mantenuta
una sessione server tradizionale.

Un `TokenFilter`, eseguito prima del filtro standard di autenticazione,
verifica il Bearer Token, recupera l'utente dal database e popola
il `SecurityContext`.

Sono previsti tre ruoli:

| Ruolo         | Funzione                                             |
|---------------|------------------------------------------------------|
| `USER`        | Utilizzo standard dell'applicazione                  |
| `ADMIN`       | Gestione dei contenuti e del backoffice              |
| `SUPER_ADMIN` | Accesso amministrativo completo e gestione dei ruoli |

Il controllo degli accessi avviene sia nella configurazione Spring Security
sia tramite `@PreAuthorize` per alcune operazioni riservate al `SUPER_ADMIN`.

---

### 🌐 Organizzazione delle API

Gli endpoint sono suddivisi per area funzionale.

```text
/auth/*              # registrazione e login
/cities/*            # città e relativi punti di interesse
/points/*            # punti di interesse
/experiences/*       # esperienze e giochi collegati
/local-businesses/*  # attività locali
/gameplay/*          # invio e gestione delle prove
/me/*                # dati e contenuti dell'utente autenticato
/backoffice/*        # gestione amministrativa
```

L'area `/me` utilizza l'utente autenticato recuperato dal `SecurityContext`,
evitando di affidarsi a un identificativo utente fornito dal client
per le operazioni personali.

Le liste amministrative e diverse risorse utente supportano inoltre
**paginazione e ordinamento** tramite `Page`, `Pageable` e `Sort`.

---

### 🧩 Gameplay e progressione

Il backend implementa due tipologie di esperienza.

#### 🧠 Enigma

La risposta inviata dall'utente viene confrontata con quella corretta
associata al `QuizGame`.

In caso di risposta corretta, il service:

1. completa il progresso relativo all'esperienza;
2. assegna gli XP previsti;
3. aggiorna automaticamente il livello dell'utente;
4. verifica l'eventuale sblocco di una ricompensa;
5. restituisce al frontend il risultato e il testo conclusivo.

Il progresso tiene traccia dell'avvenuta assegnazione degli XP, evitando
di premiare più volte la stessa esperienza già completata.

#### 📷 Rilevamento

Per le esperienze basate su immagine, l'utente invia un file tramite
`multipart/form-data`.

Il backend:

1. verifica che il file sia un'immagine;
2. lo carica su **Cloudinary**;
3. registra una `UserUploadSubmission`;
4. mantiene la submission in attesa di revisione amministrativa.

L'approvazione della prova completa l'esperienza e attiva lo stesso flusso
di XP e ricompense utilizzato dagli enigmi.

In caso di rifiuto, l'utente può inviare una nuova immagine.

---

### 🏆 XP, livelli e ricompense

Ogni esperienza definisce una quantità di XP ottenibile al completamento.

L'entità `User` conserva:

```text
xp
level
```

Il livello viene ricalcolato automaticamente quando vengono assegnati
nuovi punti esperienza.

La logica delle ricompense è collegata ai progressi ottenuti nella città
dell'esperienza completata.

Quando viene raggiunta la condizione prevista dal service, il backend
seleziona una ricompensa attiva e valida appartenente alla città,
evitando di assegnare nuovamente reward già posseduti dallo stesso utente.

---

### 🎟️ Prenotazioni e attività locali

Le ricompense sbloccate sono rappresentate tramite `UserReward`.

Una ricompensa può essere collegata a una prenotazione, sulla quale viene
gestito un ciclo di stato:

```text
PENDING
 ├── CONFIRMED
 │      └── COMPLETED
 ├── REJECTED
 └── CANCELLED
```

Prima di creare una prenotazione, il backend verifica che:

- il reward appartenga all'utente autenticato;
- sia ancora disponibile;
- sia valido temporalmente;
- non esista già una prenotazione per lo stesso reward.

Quando un amministratore conferma la prenotazione, il reward viene marcato
come utilizzato e viene inviata un'email di conferma tramite **Mailgun**.

---

### 🖼️ Gestione delle immagini

Gli upload vengono gestiti da un service dedicato che utilizza **Cloudinary**.

Il backend verifica che il file ricevuto:

- sia presente;
- non sia vuoto;
- abbia un content type di tipo `image/*`.

Cloudinary viene utilizzato per:

- avatar degli utenti;
- immagini di città e contenuti;
- immagini gestite tramite backoffice;
- prove fotografiche inviate dagli utenti.

Nel database viene salvato l'URL sicuro restituito dal servizio.

---

### ✉️ Email

L'integrazione con **Mailgun** utilizza la relativa API HTTP.

Il backend invia email in due momenti principali:

- dopo la registrazione dell'utente;
- alla conferma di una prenotazione.

L'eventuale errore del provider email viene gestito senza annullare
la registrazione o la conferma già eseguita sul database.

---

### 🛠️ Backoffice e ruoli

Gli endpoint `/backoffice/**` richiedono ruolo `ADMIN` o `SUPER_ADMIN`.

Il backoffice permette di gestire:

- città;
- punti di interesse;
- categorie narrative;
- esperienze;
- quiz;
- giochi con upload;
- submission fotografiche;
- categorie commerciali;
- attività locali;
- ricompense;
- prenotazioni;
- utenti e ruoli.

Sono presenti operazioni CRUD e azioni specifiche come:

- pubblicazione e rimozione dalla pubblicazione;
- upload e sostituzione di immagini;
- approvazione o rifiuto delle prove;
- conferma o rifiuto delle prenotazioni;
- promozione e downgrade degli utenti.

Le operazioni più sensibili relative a categorie, utenti e ruoli possono
essere ulteriormente limitate al solo `SUPER_ADMIN`.

---

### ✅ Validazione e gestione degli errori

I DTO utilizzano **Jakarta Validation** per controllare i dati ricevuti
dall'API, con constraint come:

```text
@NotNull
@NotBlank
@Size
@Min
@Max
```

Ulteriori regole di dominio vengono applicate all'interno di entità
e service.

Un `@RestControllerAdvice` centralizza la gestione delle eccezioni
e restituisce response coerenti per casi come:

- `400 Bad Request`;
- `401 Unauthorized`;
- `403 Forbidden`;
- `404 Not Found`;
- `415 Unsupported Media Type`;
- `500 Internal Server Error`.

Alcune operazioni che aggiornano più risorse utilizzano inoltre
`@Transactional` per mantenerne la coerenza.

---

### 🌱 Seed iniziale

All'avvio dell'applicazione vengono creati, se mancanti, i tre ruoli:

```text
USER
ADMIN
SUPER_ADMIN
```

È inoltre previsto un runner per la creazione degli account amministrativi
iniziali utilizzando credenziali definite nelle variabili di configurazione.

Le password dei seed vengono salvate nel database già codificate con BCrypt.

---

### ▶️ Avvio in locale

#### Requisiti

- Java 25
- PostgreSQL
- Maven, oppure Maven Wrapper incluso nel progetto

Clonare il repository:

```bash
git clone https://github.com/Raviolz/understory_back.git
```

Entrare nella directory:

```bash
cd understory_back
```

Creare un database PostgreSQL per Understory.

Il progetto importa la configurazione locale da un file `env.properties`
nella root. Il file contiene dati sensibili ed è escluso da Git tramite
`.gitignore`.

Esempio di configurazione:

```properties
PORT=3001
DB_PORT=5432
DB_NAME=your_database
DB_USERNAME=your_username
DB_PASSWORD=your_password
jwt_secret=your_long_jwt_secret
CLOUDINARY_NAME=your_cloudinary_name
CLOUDINARY_KEY=your_cloudinary_key
CLOUDINARY_API_SECRET=your_cloudinary_secret
MG_API_KEY=your_mailgun_key
MG_DOMAIN=your_mailgun_domain
MG_EMAIL=your_sender_email
SEED_SUPERADMIN_EMAIL=your_superadmin_email
SEED_SUPERADMIN_PASSWORD=your_superadmin_password
SEED_ADMIN_ONE_EMAIL=your_admin_email
SEED_ADMIN_ONE_PASSWORD=your_admin_password
SEED_ADMIN_TWO_EMAIL=your_admin_email
SEED_ADMIN_TWO_PASSWORD=your_admin_password
SEED_ADMIN_THREE_EMAIL=your_admin_email
SEED_ADMIN_THREE_PASSWORD=your_admin_password
```

> **Non committare `env.properties` con credenziali reali.**

Avviare l'applicazione su Windows:

```bash
mvnw.cmd spring-boot:run
```

oppure su macOS/Linux:

```bash
./mvnw spring-boot:run
```

Con la configurazione utilizzata dal progetto, il backend è disponibile
all'indirizzo:

```text
http://localhost:3001
```

Per utilizzare l'applicazione completa è necessario avviare anche il frontend.

---

### 🔗 Frontend

Il frontend di Understory è sviluppato con **React, Vite e Tailwind CSS**.

**Repository frontend:**  
https://github.com/Raviolz/understory_front

---

### 🚀 Sviluppi futuri

Tra le possibili evoluzioni del progetto:

- riconoscimento automatico delle prove fotografiche tramite AI;
- ampliamento di città, esperienze e contenuti;
- nuove tipologie di gameplay;
- ampliamento della rete di attività locali;
- evoluzione del sistema di reward e prenotazioni;
- ulteriore configurazione e hardening per un eventuale deploy in produzione.

---

### 👩‍💻 Autrice

**Giorgia Ragnoli**

Understory è stato ideato e sviluppato come Capstone Project conclusivo
del percorso **Full Stack Web Development**.

---

## English version

🇬🇧 **English version coming soon.**

La documentazione in inglese verrà aggiunta successivamente.