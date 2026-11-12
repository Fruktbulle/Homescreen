# Startskärm för Android TV

Egen startskärm för Sony Bravia med klocka, bakgrundsbild, appknappar, HDMI-ingångar och Inställningar.

## Så här gör du

1. Skapa ett nytt publikt repo på GitHub, t.ex. `startskarm`, och ladda upp alla filer i den här mappen. Behåll mapparna.
2. Gå till fliken **Actions** och vänta tills bygget är grönt. Första bygget tar några minuter.
3. APK:n hamnar i `apk/startskarm.apk`. Öppna filen på GitHub och kopiera den råa länken:
   `https://github.com/DITT-NAMN/startskarm/raw/main/apk/startskarm.apk`
4. På TV:n: installera appen **Downloader**, skriv in länken, installera och välj **Öppna**.
5. Välj appen som hemskärm under Inställningar > Appar > Standardappar (eller när TV:n frågar vilken hemskärm du vill använda).

## Anpassa

- **Appar:** ändra listan `apps` i `MainActivity.kt`. Paketnamnet hittar du i Play Butiken i webbläsaren, i appens webbadress efter `id=`.
- **Bakgrund:** byt filen `app/src/main/res/drawable-nodpi/background.jpg`. Ett namn måste vara `background.jpg`, annars byter du namn i koden.
- **Färger, storlekar, text:** allt finns i `MainActivity.kt`.

## Att tänka på

- Varje bygg signeras med en ny nyckel, så du kan inte uppdatera över en tidigare version. Avinstallera den gamla appen först.
- Repot är publikt, så bakgrundsbilden och koden syns för alla.
- Knappen för HDMI-ingångar är ett försök. Om den inte öppnar rätt meny, skriv vad som händer så justerar vi den.
