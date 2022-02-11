# javafx-allegro-advanced-search #
Program dla sprzedających pozwalający na szczegółowe wyszukiwanie ofert oraz ich masową modyfikację (WIP).
Aplikacja pozwala na filtrowanie ofert według wszystkich dostępnych parametrów dla ofert w danej kategorii.
Systemowa wyszukiwarka ofert na stronie www Allegro pozwala na filtrowanie ofert przy pomocy kilku ogólnych filtrów.

## Konfiguracja Allegro: ##
W pliku "app.properties" należy podać własny "client_id" oraz "client_secret" (https://apps.developer.allegro.pl/new)

## Konfiguracja DB: ##
W pliku "persistence.xml" należy skonfigurować dostęp do bazy danych

### TODO: ###
* masowa edycja ofert
* wyświetlenie miniaturki danej oferty wraz z tytułem w oknie podglądu
* dodanie możliwości sprawdzenia etapu na jakim są zmiany
* informacja w czasie rzeczywistym o statusie aktualnej zmiany
* code cleanup
