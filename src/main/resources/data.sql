set global local_infile=1;

DROP TABLE IF EXISTS singles;
CREATE TABLE IF NOT EXISTS singles (
   CompId MEDIUMINT,
   EventId VARCHAR(6),
   Event VARCHAR(50),
   LocationId MEDIUMINT,
   Location VARCHAR(50),
   EventDate VARCHAR(16),
   SquadName VARCHAR(50),
   Team VARCHAR(50),
   Athlete VARCHAR(50),
   AthleteId MEDIUMINT,
   Classification VARCHAR(50),
   Gender VARCHAR(6),
   Round1 TINYINT DEFAULT 0,
   Round2 TINYINT DEFAULT 0,
   Round3 TINYINT DEFAULT 0,
   Round4 TINYINT DEFAULT 0,
   Round5 TINYINT DEFAULT 0,
   Round6 TINYINT DEFAULT 0,
   Round7 TINYINT DEFAULT 0,
   Round8 TINYINT DEFAULT 0
);

-- top 4 scores
DROP VIEW IF EXISTS singlesdata;
CREATE VIEW singlesdata AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY team, athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, 0 round3, 0 round4
                           , s.round1 + s.round2 total
                      FROM singles s
                      WHERE s.locationid > 0
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round3, s.round4, 0 ,0
                           , s.round3 + s.round4 total
                      FROM singles s
                      WHERE s.locationid > 0
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round5, s.round6, 0 ,0
                           , s.round5 + s.round6 total
                      FROM singles s
                      WHERE s.locationid > 0
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round7, s.round8, 0 ,0
                           , s.round7 + s.round8 total
                      FROM singles s
                      WHERE s.locationid > 0
                  ) a
             ORDER BY athlete, total DESC
         ),
              s3 AS (
                  SELECT s.*
                  FROM s
                  where seqnum <= 3
              )
         SELECT *
         FROM s3
         UNION ALL
         (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, fourth
             FROM (
                      SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY unreal.athlete ORDER BY total DESC) AS fourth
                      FROM s,
                           (SELECT s3.athlete, IF(COUNT(DISTINCT s3.locationid) = 1, 'Next', 'Four') numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 3
                        AND IF(numberfour = 'four', seqnum = 4, locationid != dontuselocid)
                  ) bananas
             WHERE fourth = 1
         )
     ) AS a
WHERE total > 0;

DROP VIEW IF EXISTS singlesaggregate;
DROP TABLE IF EXISTS singlesaggregate;
CREATE VIEW singlesaggregate AS
SELECT athlete, classification, gender, team, SUM(total) AS total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM singlesdata
     ) AS a
GROUP BY athlete, classification, gender, team
ORDER BY total DESC;

DROP VIEW IF EXISTS singlesteamaggregate;
DROP TABLE IF EXISTS singlesteamaggregate;
CREATE VIEW singlesteamaggregate AS
SELECT team, classification, SUM(total) AS total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
         FROM singlesaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 5
GROUP BY team, classification
ORDER BY total DESC;

DROP VIEW IF EXISTS singlesteamscores;
DROP TABLE IF EXISTS singlesteamscores;
CREATE VIEW singlesteamscores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM singlesteamaggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) total
                     FROM (
                              SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, athlete
                                   , total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
                              FROM singlesaggregate
                              ORDER BY team, classification, total DESC
                          ) a
                     WHERE segnum <= 5
                     GROUP BY team, classification, athlete) sdts ON sta.team = sdts.team AND sta.classification = sdts.classification
ORDER BY sta.total DESC, sdts.total DESC;

DROP TABLE IF EXISTS doubles;
CREATE TABLE IF NOT EXISTS doubles (
   CompId MEDIUMINT,
   EventId VARCHAR(6),
   Event VARCHAR(50),
   LocationId MEDIUMINT,
   Location VARCHAR(50),
   EventDate VARCHAR(16),
   SquadName VARCHAR(50),
   Team VARCHAR(50),
   Athlete VARCHAR(50),
   AthleteId MEDIUMINT,
   Classification VARCHAR(50),
   Gender VARCHAR(6),
   Round1 TINYINT,
   Round2 TINYINT DEFAULT 0,
   Round3 TINYINT DEFAULT 0,
   Round4 TINYINT DEFAULT 0,
   Round5 TINYINT DEFAULT 0,
   Round6 TINYINT DEFAULT 0,
   Round7 TINYINT DEFAULT 0,
   Round8 TINYINT DEFAULT 0
);

-- top 4 scores for individual rounds
DROP VIEW IF EXISTS doublesdata;
DROP TABLE IF EXISTS doublesdata;
CREATE VIEW doublesdata AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY team, athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round1 total
                      FROM doubles s
                      WHERE s.locationid > 0
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round2 total
                      FROM doubles s
                      WHERE s.locationid > 0
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round3 total
                      FROM doubles s
                      WHERE s.locationid > 0
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round4 total
                      FROM doubles s
                      WHERE s.locationid > 0
                  ) a
             ORDER BY athlete, total DESC
         ),
              s3 AS (
                  SELECT s.*
                  FROM s
                  where seqnum <= 3
              )
         SELECT *
         FROM s3
         UNION ALL
         (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, fourth
             FROM (
                      SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY unreal.athlete ORDER BY total DESC) AS fourth
                      FROM s,
                           (SELECT s3.athlete, IF(COUNT(DISTINCT s3.locationid) = 1, 'Next', 'Four') numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 3
                        AND IF(numberfour = 'four', seqnum = 4, locationid != dontuselocid)
                  ) bananas
             WHERE fourth = 1
         )) AS a
WHERE total > 0;

DROP VIEW IF EXISTS doublesaggregate;
DROP TABLE IF EXISTS doublesaggregate;
CREATE VIEW doublesaggregate AS
SELECT athlete, classification, gender, team, SUM(total) AS total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM doublesdata
     ) a
GROUP BY athlete, classification, gender, team
ORDER BY total DESC;

DROP VIEW IF EXISTS doublesteamaggregate;
DROP TABLE IF EXISTS doublesteamaggregate;
CREATE VIEW doublesteamaggregate AS
SELECT team, classification, SUM(total) AS total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
         FROM doublesaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 5
GROUP BY team, classification
ORDER BY total DESC;

DROP VIEW IF EXISTS doublesteamscores;
DROP TABLE IF EXISTS doublesteamscores;
CREATE VIEW doublesteamscores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM doublesteamaggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) AS total
                     FROM (
                              SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, athlete, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
                              FROM doublesaggregate
                              ORDER BY team, classification, total DESC
                          ) a
                     WHERE segnum <= 5
                     GROUP BY team, classification, athlete) sdts ON sta.team = sdts.team AND sta.classification = sdts.classification
ORDER BY sta.total DESC, sdts.total DESC;

DROP TABLE IF EXISTS handicap;
CREATE TABLE IF NOT EXISTS handicap (
    CompId MEDIUMINT,
    EventId VARCHAR(6),
    Event VARCHAR(50),
    LocationId MEDIUMINT,
    Location VARCHAR(50),
    EventDate VARCHAR(16),
    SquadName VARCHAR(50),
    Team VARCHAR(50),
    Athlete VARCHAR(50),
    AthleteId MEDIUMINT,
    Classification VARCHAR(50),
    Gender VARCHAR(6),
    Round1 TINYINT,
    Round2 TINYINT DEFAULT 0,
    Round3 TINYINT DEFAULT 0,
    Round4 TINYINT DEFAULT 0,
    Round5 TINYINT DEFAULT 0,
    Round6 TINYINT DEFAULT 0,
    Round7 TINYINT DEFAULT 0,
    Round8 TINYINT DEFAULT 0
);

-- top 4 scores
DROP VIEW IF EXISTS handicapdata;
DROP TABLE IF EXISTS handicapdata;
CREATE VIEW handicapdata AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY team, athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, 0 round3, 0 round4
                           , s.round1 + s.round2 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round1 + s.round2 DESC) AS seqnum
                      FROM handicap s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round3, s.round4, 0, 0
                           , s.round3 + s.round4 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round3 + s.round4 DESC) AS seqnum
                      FROM handicap s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round5, s.round6, 0, 0
                           , s.round5 + s.round6 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round5 + s.round6 DESC) AS seqnum
                      FROM handicap s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round7, s.round8, 0, 0
                           , s.round7 + s.round8 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round7 + s.round8 DESC) AS seqnum
                      FROM handicap s
                      WHERE s.locationid > 0
                  ) a
             ORDER BY athlete, total DESC
         ),
              s3 AS (
                  SELECT s.*
                  FROM s
                  where seqnum <= 3
              )
         SELECT *
         FROM s3
         UNION ALL
         (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, fourth
             FROM (
                      SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY unreal.athlete ORDER BY total DESC) AS fourth
                      FROM s,
                           (SELECT s3.athlete, IF(COUNT(DISTINCT s3.locationid) = 1, 'Next', 'Four') numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 3
                        AND IF(numberfour = 'four', seqnum = 4, locationid != dontuselocid)
                  ) bananas
             WHERE fourth = 1
         )) AS a
WHERE total > 0;

DROP VIEW IF EXISTS handicapaggregate;
DROP TABLE IF EXISTS handicapaggregate;
CREATE VIEW handicapaggregate AS
SELECT athlete, classification, gender, team, SUM(total) AS total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM handicapdata
     ) a
GROUP BY athlete, classification, gender, team
ORDER BY total DESC;

DROP VIEW IF EXISTS handicapteamaggregate;
DROP TABLE IF EXISTS handicapteamaggregate;
CREATE VIEW handicapteamaggregate AS
SELECT team, classification, SUM(total) AS total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
         FROM handicapaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 5
GROUP BY team, classification
ORDER BY total DESC;

DROP VIEW IF EXISTS handicapteamscores;
DROP TABLE IF EXISTS handicapteamscores;
CREATE VIEW handicapteamscores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM handicapteamaggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) AS total
                     FROM (
                              SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, athlete, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
                              FROM handicapaggregate
                              ORDER BY team, classification, total DESC
                          ) a
                     WHERE segnum <= 5
                     GROUP BY team, classification, athlete) sdts ON sta.team = sdts.team AND sta.classification = sdts.classification
ORDER BY sta.total DESC, sdts.total DESC;

DROP TABLE IF EXISTS skeet;
CREATE TABLE IF NOT EXISTS skeet (
    CompId MEDIUMINT,
    EventId VARCHAR(6),
    Event VARCHAR(50),
    LocationId MEDIUMINT,
    Location VARCHAR(50),
    EventDate VARCHAR(16),
    SquadName VARCHAR(50),
    Team VARCHAR(50),
    Athlete VARCHAR(50),
    AthleteId MEDIUMINT,
    Classification VARCHAR(50),
    Gender VARCHAR(6),
    Round1 TINYINT,
    Round2 TINYINT DEFAULT 0,
    Round3 TINYINT DEFAULT 0,
    Round4 TINYINT DEFAULT 0,
    Round5 TINYINT DEFAULT 0,
    Round6 TINYINT DEFAULT 0,
    Round7 TINYINT DEFAULT 0,
    Round8 TINYINT DEFAULT 0
);

-- top 3 scores only
DROP VIEW IF EXISTS skeetdata;
DROP TABLE IF EXISTS skeetdata;
CREATE VIEW skeetdata AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY team, athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, 0 round3, 0 round4
                           , s.round1 + s.round2 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round1 + s.round2 DESC) AS seqnum
                      FROM skeet s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round3, s.round4, 0, 0
                           , s.round3 + s.round4 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round3 + s.round4 DESC) AS seqnum
                      FROM skeet s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round5, s.round6, 0, 0
                           , s.round5 + s.round6 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round5 + s.round6 DESC) AS seqnum
                      FROM skeet s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round7, s.round8, 0, 0
                           , s.round7 + s.round8 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round7 + s.round8 DESC) AS seqnum
                      FROM skeet s
                      WHERE s.locationid > 0
                  ) a
             ORDER BY athlete, total DESC
         ),
              s3 AS (
                  SELECT s.*
                  FROM s
                  where seqnum <= 2
              )
         SELECT *
         FROM s3
         UNION ALL
         (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, fourth
             FROM (
                      SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY unreal.athlete ORDER BY total DESC) AS fourth
                      FROM s,
                           (SELECT s3.athlete, IF(COUNT(DISTINCT s3.locationid) = 1, 'Next', 'Four') numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 2
                        AND IF(numberfour = 'four', seqnum = 3, locationid != dontuselocid)
                  ) bananas
             WHERE fourth = 1
         )) AS a
WHERE total > 0;

DROP VIEW IF EXISTS skeetaggregate;
DROP TABLE IF EXISTS skeetaggregate;
CREATE VIEW skeetaggregate AS
SELECT athlete, classification, gender, team, SUM(total) AS total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM skeetdata
     ) a
GROUP BY athlete, classification, gender, team
ORDER BY total DESC;

DROP VIEW IF EXISTS skeetteamaggregate;
DROP TABLE IF EXISTS skeetteamaggregate;
CREATE VIEW skeetteamaggregate AS
SELECT team, classification, SUM(total) AS total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
         FROM skeetaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 3
GROUP BY team, classification
ORDER BY total DESC;

DROP VIEW IF EXISTS skeetteamscores;
DROP TABLE IF EXISTS skeetteamscores;
CREATE VIEW skeetteamscores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM skeetteamaggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) AS total
                     FROM (
                              SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, athlete, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
                              FROM skeetaggregate
                              ORDER BY team, classification, total DESC
                          ) a
                     WHERE segnum <= 3
                     GROUP BY team, classification, athlete) sdts ON sta.team = sdts.team AND sta.classification = sdts.classification
ORDER BY sta.total DESC, sdts.total DESC;

DROP TABLE IF EXISTS clays;
CREATE TABLE IF NOT EXISTS clays (
    CompId MEDIUMINT,
    EventId VARCHAR(6),
    Event VARCHAR(50),
    LocationId MEDIUMINT,
    Location VARCHAR(50),
    EventDate VARCHAR(16),
    SquadName VARCHAR(50),
    Team VARCHAR(50),
    Athlete VARCHAR(50),
    AthleteId MEDIUMINT,
    Classification VARCHAR(50),
    Gender VARCHAR(6),
    Round1 TINYINT,
    Round2 TINYINT DEFAULT 0,
    Round3 TINYINT DEFAULT 0,
    Round4 TINYINT DEFAULT 0,
    Round5 TINYINT DEFAULT 0,
    Round6 TINYINT DEFAULT 0,
    Round7 TINYINT DEFAULT 0,
    Round8 TINYINT DEFAULT 0
);

-- top 3 scores only
DROP VIEW IF EXISTS claysdata;
DROP TABLE IF EXISTS claysdata;
CREATE VIEW claysdata AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY team, athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round1 total
                      FROM clays s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round2 total
                      FROM clays s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round3 total
                      FROM clays s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round4 total
                      FROM clays s
                      WHERE s.locationid > 0
                  ) a
             ORDER BY athlete, total DESC
         ),
              s3 AS (
                  SELECT s.*
                  FROM s
                  where seqnum <= 2
              )
         SELECT *
         FROM s3
         UNION ALL
         (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, fourth
             FROM (
                      SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY unreal.athlete ORDER BY total DESC) AS fourth
                      FROM s,
                           (SELECT s3.athlete, IF(COUNT(DISTINCT s3.locationid) = 1, 'Next', 'Four') numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 2
                        AND IF(unreal.numberfour = 'four', seqnum = 3, locationid != dontuselocid)
                  ) bananas
             WHERE fourth = 1
         )
     ) AS a
WHERE total > 0;

DROP VIEW IF EXISTS claysaggregate;
DROP TABLE IF EXISTS claysaggregate;
CREATE VIEW claysaggregate AS
SELECT athlete, classification, gender, team, SUM(total) AS total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM claysdata
     ) a
GROUP BY athlete, classification, gender, team
ORDER BY total DESC;

DROP VIEW IF EXISTS claysteamaggregate;
DROP TABLE IF EXISTS claysteamaggregate;
CREATE VIEW claysteamaggregate AS
SELECT team, classification, SUM(total) AS total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER
             BY total DESC ) AS segnum
         FROM claysaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 3
GROUP BY team, classification
ORDER BY total DESC;

DROP VIEW IF EXISTS claysteamscores;
DROP TABLE IF EXISTS claysteamscores;
CREATE VIEW claysteamscores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM claysteamaggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) total
                     FROM (
                              SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, athlete, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
                              FROM claysaggregate
                              ORDER BY team, classification, total DESC
                          ) a
                     WHERE segnum <= 3
                     GROUP BY team, classification, athlete) sdts ON sta.team = sdts.team AND sta.classification = sdts.classification
ORDER BY sta.total DESC, sdts.total DESC;

DROP TABLE IF EXISTS fivestand;
CREATE TABLE IF NOT EXISTS fivestand (
    CompId MEDIUMINT,
    EventId VARCHAR(6),
    Event VARCHAR(50),
    LocationId MEDIUMINT,
    Location VARCHAR(50),
    EventDate VARCHAR(16),
    SquadName VARCHAR(50),
    Team VARCHAR(50),
    Athlete VARCHAR(50),
    AthleteId MEDIUMINT,
    Classification VARCHAR(50),
    Gender VARCHAR(6),
    Round1 TINYINT DEFAULT 0,
    Round2 TINYINT DEFAULT 0,
    Round3 TINYINT DEFAULT 0,
    Round4 TINYINT DEFAULT 0,
    Round5 TINYINT DEFAULT 0,
    Round6 TINYINT DEFAULT 0,
    Round7 TINYINT DEFAULT 0,
    Round8 TINYINT DEFAULT 0
);

-- top 3 scores only
DROP VIEW IF EXISTS fivestanddata;
DROP TABLE IF EXISTS fivestanddata;
CREATE VIEW fivestanddata AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY team, athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round1 + s.round2 + s.round3 + s.round4 total
                      FROM fivestand s
                      WHERE s.locationid > 0
                  ) a
             ORDER BY athlete, total DESC
         ),
              s3 AS (
                  SELECT s.*
                  FROM s
                  where seqnum <= 2
              )
         SELECT *
         FROM s3
         UNION ALL
         (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, fourth
             FROM (
                      SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY unreal.athlete ORDER BY total DESC) AS fourth
                      FROM s,
                           (SELECT s3.athlete, IF(COUNT(DISTINCT s3.locationid) = 1, 'Next', 'Four') numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 2
                        AND IF(unreal.numberfour = 'four', seqnum = 3, locationid != dontuselocid)
                  ) bananas
             WHERE fourth = 1
         )
     ) AS a
WHERE total > 0;

DROP VIEW IF EXISTS fivestandaggregate;
DROP TABLE IF EXISTS fivestandaggregate;
CREATE VIEW fivestandaggregate AS
SELECT athlete, classification, gender, team, SUM(total) AS total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM fivestanddata
     ) a
GROUP BY athlete, classification, gender, team
ORDER BY total DESC;

DROP VIEW IF EXISTS fivestandteamaggregate;
DROP TABLE IF EXISTS fivestandteamaggregate;
CREATE VIEW fivestandteamaggregate AS
SELECT team, classification, SUM(total) AS total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER
             BY total DESC ) AS segnum
         FROM fivestandaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 3
GROUP BY team, classification
ORDER BY total DESC;

DROP VIEW IF EXISTS fivestandteamscores;
DROP TABLE IF EXISTS fivestandteamscores;
CREATE VIEW fivestandteamscores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM fivestandteamaggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) total
                     FROM (
                              SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, athlete, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
                              FROM fivestandaggregate
                              ORDER BY team, classification, total DESC
                          ) a
                     WHERE segnum <= 3
                     GROUP BY team, classification, athlete) sdts ON sta.team = sdts.team AND sta.classification = sdts.classification
ORDER BY sta.total DESC, sdts.total DESC;

DROP TABLE IF EXISTS doublesskeet;
CREATE TABLE IF NOT EXISTS doublesskeet (
    CompId MEDIUMINT,
    EventId VARCHAR(6),
    Event VARCHAR(50),
    LocationId MEDIUMINT,
    Location VARCHAR(50),
    EventDate VARCHAR(16),
    SquadName VARCHAR(50),
    Team VARCHAR(50),
    Athlete VARCHAR(50),
    AthleteId MEDIUMINT,
    Classification VARCHAR(50),
    Gender VARCHAR(6),
    Round1 TINYINT,
    Round2 TINYINT DEFAULT 0,
    Round3 TINYINT DEFAULT 0,
    Round4 TINYINT DEFAULT 0,
    Round5 TINYINT DEFAULT 0,
    Round6 TINYINT DEFAULT 0,
    Round7 TINYINT DEFAULT 0,
    Round8 TINYINT DEFAULT 0
);

-- top 3 scores only
DROP VIEW IF EXISTS doublesskeetdata;
DROP TABLE IF EXISTS doublesskeetdata;
CREATE VIEW doublesskeetdata AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY team, athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, 0 round3, 0 round4
                           , s.round1 + s.round2 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round1 + s.round2 DESC) AS seqnum
                      FROM doublesskeet s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round3, s.round4, 0, 0
                           , s.round3 + s.round4 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round3 + s.round4 DESC) AS seqnum
                      FROM doublesskeet s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round5, s.round6, 0, 0
                           , s.round5 + s.round6 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round5 + s.round6 DESC) AS seqnum
                      FROM doublesskeet s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round7, s.round8, 0, 0
                           , s.round7 + s.round8 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round7 + s.round8 DESC) AS seqnum
                      FROM doublesskeet s
                      WHERE s.locationid > 0
                  ) a
             ORDER BY athlete, total DESC
         ),
              s3 AS (
                  SELECT s.*
                  FROM s
                  where seqnum <= 2
              )
         SELECT *
         FROM s3
         UNION ALL
         (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, fourth
             FROM (
                      SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY unreal.athlete ORDER BY total DESC) AS fourth
                      FROM s,
                           (SELECT s3.athlete, IF(COUNT(DISTINCT s3.locationid) = 1, 'Next', 'Four') numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 2
                        AND IF(numberfour = 'four', seqnum = 3, locationid != dontuselocid)
                  ) bananas
             WHERE fourth = 1
         )) AS a
WHERE total > 0;

DROP VIEW IF EXISTS doublesskeetaggregate;
DROP TABLE IF EXISTS doublesskeetaggregate;
CREATE VIEW doublesskeetaggregate AS
SELECT athlete, classification, gender, team, SUM(total) AS total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM doublesskeetdata
     ) a
GROUP BY athlete, classification, gender, team
ORDER BY total DESC;

DROP VIEW IF EXISTS doublesskeetteamaggregate;
DROP TABLE IF EXISTS doublesskeetteamaggregate;
CREATE VIEW doublesskeetteamaggregate AS
SELECT team, classification, SUM(total) AS total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
         FROM doublesskeetaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 3
GROUP BY team, classification
ORDER BY total DESC;

DROP VIEW IF EXISTS doublesskeetteamscores;
DROP TABLE IF EXISTS doublesskeetteamscores;
CREATE VIEW doublesskeetteamscores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM doublesskeetteamaggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) AS total
                     FROM (
                              SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, athlete, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
                              FROM doublesskeetaggregate
                              ORDER BY team, classification, total DESC
                          ) a
                     WHERE segnum <= 3
                     GROUP BY team, classification, athlete) sdts ON sta.team = sdts.team AND sta.classification = sdts.classification
ORDER BY sta.total DESC, sdts.total DESC;

DROP VIEW IF EXISTS alldata;
DROP TABLE IF EXISTS alldata;
CREATE VIEW alldata AS
SELECT compid, eventid, event, locationid, location, eventdate, squadname, team, athlete, athleteid, classification, gender, round1, round2, round3, round4, round5, round6, round7, round8, 'singles' as type
FROM singles
UNION
SELECT *, 'doubles' as type
FROM doubles
UNION
SELECT *, 'handicap' as type
FROM handicap
UNION
SELECT *, 'skeet' as type
FROM skeet
UNION
SELECT *, 'clays' as type
FROM clays
UNION
SELECT *, 'fivestand' as type
FROM fivestand
UNION
SELECT *, 'doublesskeet' as type
FROM doublesskeet;

DROP VIEW IF EXISTS allteamscores;
DROP TABLE IF EXISTS allteamscores;
CREATE VIEW allteamscores AS
SELECT *, 'singles' type
FROM singlesteamscores
UNION
SELECT *, 'doubles'
FROM doublesteamscores
UNION
SELECT *, 'handicap'
FROM handicapteamscores
UNION
SELECT *, 'skeet'
FROM skeetteamscores
UNION
SELECT *, 'clays'
FROM claysteamscores
UNION
SELECT *, 'fivestand'
FROM fivestandteamscores
UNION
SELECT *, 'doublesskeet'
FROM doublesskeetteamscores;

DROP VIEW IF EXISTS allindividualscores;
DROP TABLE IF EXISTS allindividualscores;
CREATE VIEW allindividualscores AS
SELECT *, 'singles' type
FROM singlesaggregate
UNION
SELECT *, 'doubles'
FROM doublesaggregate
UNION
SELECT *, 'handicap'
FROM handicapaggregate
UNION
SELECT *, 'skeet'
FROM skeetaggregate
UNION
SELECT *, 'clays'
FROM claysaggregate
UNION
SELECT *, 'fivestand'
FROM fivestandaggregate
UNION
SELECT *, 'doublesskeet'
FROM doublesskeetaggregate;

CREATE INDEX singlesClassGen ON singles(classification, gender);
CREATE INDEX doublesClassGen ON doubles(classification, gender);
CREATE INDEX handicapClassGen ON handicap(classification, gender);
CREATE INDEX skeetClassGen ON skeet(classification, gender);
CREATE INDEX claysClassGen ON clays(classification, gender);
CREATE INDEX fivestandClassGen ON fivestand(classification, gender);
CREATE INDEX doublesskeetClassGen ON doublesskeet(classification, gender);
