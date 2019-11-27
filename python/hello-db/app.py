# encoding: UTF-8
import argparse

import json
from datetime import date

## Веб сервер
import cherrypy

# Драйвер PostgreSQL
import psycopg2 as pg_driver

# ORM
# Можно использовать SQL Alchemy
from peewee import *


parser = argparse.ArgumentParser(description='Hello DB web application')
parser.add_argument('--pg-host', help='PostgreSQL host name', default='localhost')
parser.add_argument('--pg-port', help='PostgreSQL port', default=5432)
parser.add_argument('--pg-user', help='PostgreSQL user', default='postgres')
parser.add_argument('--pg-password', help='PostgreSQL password', default='')
parser.add_argument('--pg-database', help='PostgreSQL database', default='postgres')

args = parser.parse_args()

db = PostgresqlDatabase(args.pg_database, user=args.pg_user, host=args.pg_host, password=args.pg_password)

# Классы ORM модели
class PlanetEntity(Model):
    id = PrimaryKeyField()
    distance = DecimalField()
    name = TextField()
    class Meta:
        database = db
        db_table = "planet"

class FlightEntity(Model):
    id = PrimaryKeyField()
    date = DateField()
    planet = ForeignKeyField(PlanetEntity, related_name='flights')
    class Meta:
        database = db
        db_table = "flight"


@cherrypy.expose
class App(object):

    @cherrypy.expose
    def hello(self):
    	return "Hello DB"

    @cherrypy.expose
    def planets(self, planet_id = None):
        db = pg_driver.connect(user="postgres", password="", host="localhost")
        cur = db.cursor()
        if planet_id is None:
            cur.execute("SELECT F.id, F.date, P.name, P.distance FROM Flight F JOIN Planet P ON F.planet_id = P.id")
        else:
            cur.execute("SELECT F.id, F.date, P.name, P.distance FROM Flight F JOIN Planet P ON F.planet_id = P.id WHERE P.id = %s", planet_id)

        result = []
        flights = cur.fetchall()
        for f in flights:
            result.append({"flight_id": f[0], "date": str(f[1]), "planet_name": f[2], "distance": str(f[3])})
        return json.dumps(result)

if __name__ == '__main__':
    cherrypy.quickstart(App())
