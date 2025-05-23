# Trap Scoring App

## Scoring

| Discipline     | Scoring        | Events | Locations | Birds |
|----------------|----------------|--------|-----------|-------|
| Singles        | 2 rounds of 25 | 4      | 2         | 200   |
| Doubles        | 1 round of 50  | 4      | 2         | 200   |
| Handicap       | 2 rounds of 25 | 4      | 2         | 200   |
| American Skeet | 2 rounds of 25 | 4      | 2         | 200   |
| Doubles Skeet  | 1 round of 50  | 4      | 2         | 200   |
| Sporting Clay  | 1 round of 100 | 3      | 2         | 300   |
| 5-Stand        | 1 round of 50  | 4      | 2         | 200   |

## Downloading the daily generated report

Every day at 6 AM UTC the report is [generated and uploaded](https://github.com/mrbusche/trap-scoring/actions/workflows/daily-standings.yml).

1. Login to GitHub
2. Click on the top [Daily Standings](https://github.com/mrbusche/trap-scoring/actions/workflows/daily-standings.yml) link
3. Download `trap-league-standings`
4. Extract the zip file
5. Report will be named `league-data-${todays-date}.xlsx`

## Generating standings on demand

- Download jar file from the [latest release](https://github.com/mrbusche/trap-scoring/releases)
- You will need [Java 24](https://adoptium.net/) to run the jar file
- Open command prompt to directory with downloaded jar file
- Run `java -jar trap-8.1.0.jar` (make sure you have the correct filename from the release)
- There will be output as the process generates the file
- When finished there will be a file named `leaguedata-${date-string}` generated in the directory you ran the command from

## Online metabase links

[Singles](https://metabase.sssfonline.com/public/question/8648faf9-42e8-4a9c-b55d-2f251349de7f) | [Doubles](https://metabase.sssfonline.com/public/question/5d5a78a5-2356-477f-b1b8-fe6ee11d25b1) | [Handicap](https://metabase.sssfonline.com/public/question/69ca55d9-3e18-45bc-b57f-73aeb205ece8) | [Skeet](https://metabase.sssfonline.com/public/question/c697d744-0e06-4c3f-a640-fea02f9c9ecd) | [Sporting Clays](https://metabase.sssfonline.com/public/question/2c6edb1a-a7ee-43c2-8180-ad199a57be55) | [Five Stand](https://metabase.sssfonline.com/public/question/3c5aecf2-a9f2-49b2-a11f-36965cb1a964) | [Doubles Skeet](https://metabase.sssfonline.com/public/question/bdd61066-6e29-4242-b6e9-adf286c2c4ae.csv)
