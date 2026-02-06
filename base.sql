-- CREATE TABLE Client (
--     idClient SERIAL PRIMARY KEY,
--     nom VARCHAR(50) NOT NULL,
--     prenom VARCHAR(50) NOT NULL,
--     mot_de_passe_hash VARCHAR(255),
--     email VARCHAR(50) UNIQUE NOT NULL,
--     numero VARCHAR(10)
-- );


CREATE TABLE Hotel(
    idHotel SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    lieu VARCHAR(50)
);

CREATE TABLE Reservation (
    idReservation SERIAL PRIMARY KEY,
    idClient INT NOT NULL,
    nombre INT,
    date_reservation TIMESTAMP,
    idHotel INT,
    FOREIGN KEY (idHotel) REFERENCES Hotel(idHotel)
);





CREATE TABLE Vehicule (
    idVehicule SERIAL PRIMARY KEY,
    numero VARCHAR(10) NOT NULL,
    carburant VARCHAR(20),
    capacite INT
);

CREATE TABLE Parametre (
    idParametre SERIAL PRIMARY KEY,
    idVehicule INT NOT NULL,
    temps_attente INT,
    vitesse_moyenne INT,
    FOREIGN KEY (idVehicule) REFERENCES Vehicule(idVehicule)
);

CREATE TABLE Regroupement (
    idRegroupement SERIAL PRIMARY KEY,
    idReservation INT NOT NULL,
    debut_regroupement TIMESTAMP,
    FOREIGN KEY (idReservation) REFERENCES Reservation(idReservation)
);

CREATE TABLE Assignation (
    idAssignation SERIAL PRIMARY KEY,
    idVehicule INT NOT NULL,
    idRegroupement INT NOT NULL,
    debut_assignation TIMESTAMP,
    fin_assignation TIMESTAMP,
    FOREIGN KEY (idVehicule) REFERENCES Vehicule(idVehicule),
    FOREIGN KEY (idRegroupement) REFERENCES Regroupement(idRegroupement)
);


