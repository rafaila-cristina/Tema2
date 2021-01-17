# Tema2
>Aplicație de vreme

## Cuprins
1. [Introducere](#introducere)
2. [Utilizare](#p1)
    1. [Exemplu fișier intrare](#p2)
3. [Diagrame UML](#p3)
    1. [Diagrama de clase](#p4)
    2. [Diagrama de secvență](#p5)
3. [Realizator](#p6)


## Introducere <a name="introducere"></a>
Această aplicație își propune să afișeze starea curentă a vremii pentru o locație aleasă de utilizator. Pentru selectarea locației dorite utilizatorul are la dispoziție două liste, prima conținând numele țărilor disponibile in aplicație, iar cea de-a doua listă conținând orașe aparținând țării selectate. Datele de intrare sunt furnizate prin intermediul unui fișier. Se fac request-uri catre API-ul [OpenWeatherMap](https://openweathermap.org/api) pentru a prelua datele legate de vreme.
Aplicația folosește JavaFX si Gson.

## Utilizare <a name="p1"></a>
Fișierul trebuie să aibă denumire „Cities.txt” si trebuie să aibă următorul format:
ID_locație\tOraș\tlatitudine\tlongitudine\tcod_țară

### Exemplu fișier intrare <a name="p2"></a>

6942372	Miercurea Ciuc	46.357948	25.80405	RO<br/>
683506	Bucharest	44.432251	26.10626	RO<br/>
2643743	London	51.50853	-0.12574	GB<br/>
2643797	Lochwinnoch	55.795212	-4.63034	GB<br/>
4887398	Chicago	41.850029	-87.650047	US<br/>
5304640	Miami	33.39922	-110.868721	US<br/>
6455259	Paris	48.856461	2.35236	FR<br/>
6455270	Bressuire	46.849998	-0.48333	FR<br/>

## Diagrame UML <a name="p3"></a>

### Diagrama de clase <a name="p4"></a>
![enter image description here](https://github.com/rafaila-cristina/Tema2/blob/InitProject/UML/Diagrama%20de%20clase.png?raw=true)

### Diagrama de secvență<a name="p5"></a>
![enter image description here](https://github.com/rafaila-cristina/Tema2/blob/InitProject/UML/Diagrama%20de%20secventa.png?raw=true)

## Realizator <a name="p6"></a>
Rafailă Cristina-Oana <br/>
Grupa: C114A
