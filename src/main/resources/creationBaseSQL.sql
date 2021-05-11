createdb generic-food

create user "user" password 'user';

CREATE TABLE groupe (id INTEGER NOT NULL PRIMARY KEY, nom VARCHAR(64));
CREATE TABLE sousgroupe (id INTEGER NOT NULL PRIMARY KEY, groupe_id INTEGER REFERENCES groupe(id), nom VARCHAR(64));
CREATE TABLE aliment (id INTEGER NOT NULL PRIMARY KEY, nom VARCHAR(128), nom_sc VARCHAR(128), sousgroupe_id INTEGER REFERENCES sousgroupe(id));

GRANT ALL ON aliment TO "user";
GRANT ALL ON groupe TO "user";
GRANT ALL ON sousgroupe TO "user";
