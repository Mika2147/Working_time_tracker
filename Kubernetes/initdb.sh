kubectl exec -n working-time-measurement --stdin --tty users-postgres-0 -- /bin/bash
#psql -U admin
#CREATE DATABASE users;
#\c users
#CREATE EXTENSION "uuid-ossp";
#CREATE TABLE users ( id UUID NOT NULL PRIMARY KEY, name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, isadmin BOOLEAN NOT NULL );
#INSERT INTO users (id, name, password, isadmin) VALUES ( uuid_generate_v4() , 'admin', '21232f297a57a5a743894a0e4a801fc3', true );

kubectl exec -n working-time-measurement --stdin --tty mongodb-time-0 -- bash
#mongosh --username user --password password
#use TimeEntries
#db.createCollection("workdays");
#