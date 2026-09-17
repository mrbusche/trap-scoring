# Trap Scoring App

## Scoring

| Discipline     | Scoring        | Events | Locations | Birds |
|----------------|----------------|--------|-----------|-------|
| Singles        | 2 rounds of 25 | 5      | 3         | 250   |
| Doubles        | 1 round of 50  | 5      | 3         | 250   |
| Handicap       | 2 rounds of 25 | 5      | 3         | 250   |
| American Skeet | 2 rounds of 25 | 5      | 3         | 250   |
| Doubles Skeet  | 1 round of 50  | 5      | 3         | 250   |
| Sporting Clay  | 1 round of 100 | 4      | 3         | 400   |
| 5-Stand        | 2 rounds of 25 | 5      | 3         | 250   |

## Downloading the daily generated report

Every day at 6 AM UTC the report is [generated and uploaded](https://github.com/mrbusche/trap-scoring/actions/workflows/daily-standings.yml).

1. Login to GitHub
2. Click on the top [Daily Standings](https://github.com/mrbusche/trap-scoring/actions/workflows/daily-standings.yml) link
3. Download `league-data-${todays-date}.xlsx`

## Generating standings on demand

- Download jar file from the [latest release](https://github.com/mrbusche/trap-scoring/releases)
- You will need [Java 25](https://adoptium.net/) to run the jar file
- Open command prompt to directory with downloaded jar file
- Run `java -jar trap-8.1.0.jar` (make sure you have the correct filename from the release)
- There will be output as the process generates the file
- When finished there will be a file named `leaguedata-${date-string}` generated in the directory you ran the command from

## Online metabase links

[Singles](https://metabase.sssfonline.com/public/question/77009b20-98d6-4a72-978f-b9a7915d6f67) | [Doubles](https://metabase.sssfonline.com/public/question/93e760dc-5eb3-4dc8-99b0-888940fa6cc2) | [Handicap](https://metabase.sssfonline.com/public/question/1b7b0a6b-9bc9-4961-9682-3456a1cb4639) | [Skeet](https://metabase.sssfonline.com/public/question/493ddc58-a891-4614-999f-3947d14afd76) | [Sporting Clays](https://metabase.sssfonline.com/public/question/43eaa17c-abf7-4cc7-81eb-4a8885b6e6d6) | [Five Stand](https://metabase.sssfonline.com/public/question/a84359f2-b915-4abe-b967-2cc4294b42eb) | [Doubles Skeet](https://metabase.sssfonline.com/public/question/4f209d11-17eb-49cf-b279-fc1f5ac3217a)
