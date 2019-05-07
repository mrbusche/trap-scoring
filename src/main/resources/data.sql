-- set global local_infile=1;

USE trap;
DROP TABLE IF EXISTS singles;
CREATE TABLE IF NOT EXISTS singles (
    -- CompId MEDIUMINT,
    EventId VARCHAR(6),
    Event VARCHAR(50),
    LocationId MEDIUMINT,
    Location VARCHAR(50),
    EventDate VARCHAR(16),
    SquadName VARCHAR(50),
    Team VARCHAR(50),
    Athlete VARCHAR(50),
    Athlete_Id MEDIUMINT,
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
CREATE OR REPLACE VIEW singlesData AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, 0 round3, 0 round4
                           , s.round1 + s.round2 total
                      FROM singles s
                      WHERE s.locationid > 0
                      UNION
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round3, s.round4, 0 ,0
                           , s.round3 + s.round4 total
                      FROM singles s
                      WHERE s.locationid > 0
                      UNION
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round5, s.round6, 0 ,0
                           , s.round5 + s.round6 total
                      FROM singles s
                      WHERE s.locationid > 0
                      UNION
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
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
                           (SELECT s3.athlete, CASE WHEN COUNT(DISTINCT s3.locationid) = 1 THEN 'Next' ELSE 'Four' END numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 3
                        AND CASE WHEN numberfour = 'four' THEN seqnum = 4 ELSE locationid != dontuselocid END
                  ) bananas
             WHERE fourth = 1
         )
     ) AS a
WHERE total > 0;

CREATE OR REPLACE VIEW singlesAggregate AS
SELECT athlete, classification, gender, team, SUM(total) total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM singlesData
     ) AS a
GROUP BY athlete, classification, gender
ORDER BY total DESC;

CREATE OR REPLACE VIEW singlesTeamAggregate AS
SELECT team, classification, SUM(total) total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
         FROM singlesAggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 5
GROUP BY team, classification
ORDER BY total DESC;

CREATE OR REPLACE VIEW singlesTeamScores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM singlesTeamAggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) total
                     FROM (
                              SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, athlete
                                   , total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
                              FROM singlesAggregate
                              ORDER BY team, classification, total DESC
                          ) a
                     WHERE segnum <= 5
                     GROUP BY team, classification, athlete) sdts ON sta.team = sdts.team AND sta.classification = sdts.classification
ORDER BY sta.total DESC, sdts.total DESC;

DROP TABLE IF EXISTS doubles;
CREATE TABLE IF NOT EXISTS doubles (
    EventId VARCHAR(6),
    Event VARCHAR(50),
    LocationId MEDIUMINT,
    Location VARCHAR(50),
    EventDate VARCHAR(16),
    SquadName VARCHAR(50),
    Station SMALLINT,
    Team VARCHAR(50),
    Athlete VARCHAR(50),
    Classification VARCHAR(50),
    Gender VARCHAR(6),
    Round1 TINYINT,
    Round2 TINYINT DEFAULT 0,
    Round3 TINYINT DEFAULT 0,
    Round4 TINYINT DEFAULT 0,
    Round5 TINYINT DEFAULT 0,
    Round6 TINYINT DEFAULT 0,
    Round7 TINYINT DEFAULT 0,
    Round8 TINYINT DEFAULT 0,
    FrontRun TINYINT DEFAULT 0,
    BackRun TINYINT DEFAULT 0
);

-- top 4 scores for individual rounds
CREATE OR REPLACE VIEW doublesData AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round1 total
                      FROM doubles s
                      WHERE s.locationid > 0
                      UNION
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round2 total
                      FROM doubles s
                      WHERE s.locationid > 0
                      UNION
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round3 total
                      FROM doubles s
                      WHERE s.locationid > 0
                      UNION
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
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
                           (SELECT s3.athlete, CASE WHEN COUNT(DISTINCT s3.locationid) = 1 THEN 'Next' ELSE 'Four' END numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 3
                        AND CASE WHEN numberfour = 'four' THEN seqnum = 4 ELSE locationid != dontuselocid END
                  ) bananas
             WHERE fourth = 1
         )) AS a
WHERE total > 0;

CREATE OR REPLACE VIEW doublesAggregate AS
SELECT athlete, classification, gender, team, SUM(total) total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM doublesdata
     ) a
GROUP BY athlete, classification, gender
ORDER BY total DESC;

CREATE OR REPLACE VIEW doublesTeamAggregate AS
SELECT team, classification, SUM(total) total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
         FROM doublesaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 5
GROUP BY team, classification
ORDER BY total DESC;

CREATE OR REPLACE VIEW doublesTeamScores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM doublesTeamAggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) total
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
    EventId VARCHAR(6),
    Event VARCHAR(50),
    LocationId MEDIUMINT,
    Location VARCHAR(50),
    EventDate VARCHAR(16),
    SquadName VARCHAR(50),
    Station SMALLINT,
    Team VARCHAR(50),
    Athlete VARCHAR(50),
    Classification VARCHAR(50),
    Gender VARCHAR(6),
    Round1 TINYINT,
    Round2 TINYINT DEFAULT 0,
    Round3 TINYINT DEFAULT 0,
    Round4 TINYINT DEFAULT 0,
    Round5 TINYINT DEFAULT 0,
    Round6 TINYINT DEFAULT 0,
    Round7 TINYINT DEFAULT 0,
    Round8 TINYINT DEFAULT 0,
    FrontRun TINYINT DEFAULT 0,
    BackRun TINYINT DEFAULT 0
);

-- top 4 scores
CREATE OR REPLACE VIEW handicapData AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, 0 round3, 0 round4
                           , s.round1 + s.round2 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round1 + s.round2 DESC) AS seqnum
                      FROM handicap s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round3, s.round4, 0, 0
                           , s.round3 + s.round4 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round3 + s.round4 DESC) AS seqnum
                      FROM handicap s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round5, s.round6, 0, 0
                           , s.round5 + s.round6 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round5 + s.round6 DESC) AS seqnum
                      FROM handicap s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
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
                           (SELECT s3.athlete, CASE WHEN COUNT(DISTINCT s3.locationid) = 1 THEN 'Next' ELSE 'Four' END numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 3
                        AND CASE WHEN numberfour = 'four' THEN seqnum = 4 ELSE locationid != dontuselocid END
                  ) bananas
             WHERE fourth = 1
         )) AS a
WHERE total > 0;

CREATE OR REPLACE VIEW handicapAggregate AS
SELECT athlete, classification, gender, team, SUM(total) total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM handicapdata
     ) a
GROUP BY athlete, classification, gender
ORDER BY total DESC;

CREATE OR REPLACE VIEW handicapTeamAggregate AS
SELECT team, classification, SUM(total) total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
         FROM handicapaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 5
GROUP BY team, classification
ORDER BY total DESC;

CREATE OR REPLACE VIEW handicapTeamScores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM handicapTeamAggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) total
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
    EventId VARCHAR(6),
    Event VARCHAR(50),
    LocationId MEDIUMINT,
    Location VARCHAR(50),
    EventDate VARCHAR(16),
    SquadName VARCHAR(50),
    Station SMALLINT,
    Team VARCHAR(50),
    Athlete VARCHAR(50),
    Classification VARCHAR(50),
    Gender VARCHAR(6),
    Round1 TINYINT,
    Round2 TINYINT DEFAULT 0,
    Round3 TINYINT DEFAULT 0,
    Round4 TINYINT DEFAULT 0,
    Round5 TINYINT DEFAULT 0,
    Round6 TINYINT DEFAULT 0,
    Round7 TINYINT DEFAULT 0,
    Round8 TINYINT DEFAULT 0,
    FrontRun TINYINT DEFAULT 0,
    BackRun TINYINT DEFAULT 0
);

-- top 3 scores only
CREATE OR REPLACE VIEW skeetData AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, row_number() OVER (PARTITION BY athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, 0 round3, 0 round4
                           , s.round1 + s.round2 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round1 + s.round2 DESC) AS seqnum
                      FROM skeet s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round3, s.round4, 0, 0
                           , s.round3 + s.round4 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round3 + s.round4 DESC) AS seqnum
                      FROM skeet s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round5, s.round6, 0, 0
                           , s.round5 + s.round6 total
                           , row_number() OVER (PARTITION BY athlete ORDER BY s.round5 + s.round6 DESC) AS seqnum
                      FROM skeet s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
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
                           (SELECT s3.athlete, CASE WHEN COUNT(DISTINCT s3.locationid) = 1 THEN 'Next' ELSE 'Four' END numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 2
                        AND CASE WHEN numberfour = 'four' THEN seqnum = 3 ELSE locationid != dontuselocid END
                  ) bananas
             WHERE fourth = 1
         )) AS a
WHERE total > 0;

CREATE OR REPLACE VIEW skeetAggregate AS
SELECT athlete, classification, gender, team, SUM(total) total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM skeetdata
     ) a
GROUP BY athlete, classification, gender
ORDER BY total DESC;

CREATE OR REPLACE VIEW skeetTeamAggregate AS
SELECT team, classification, SUM(total) total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
         FROM skeetaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 3
GROUP BY team, classification
ORDER BY total DESC;

CREATE OR REPLACE VIEW skeetTeamScores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM skeetTeamAggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) total
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
    EventId VARCHAR(6),
    Event VARCHAR(50),
    LocationId MEDIUMINT,
    Location VARCHAR(50),
    EventDate VARCHAR(16),
    SquadName VARCHAR(50),
    Station SMALLINT,
    Team VARCHAR(50),
    Athlete VARCHAR(50),
    Classification VARCHAR(50),
    Gender VARCHAR(6),
    Round1 TINYINT,
    Round2 TINYINT DEFAULT 0,
    Round3 TINYINT DEFAULT 0,
    Round4 TINYINT DEFAULT 0,
    Round5 TINYINT DEFAULT 0,
    Round6 TINYINT DEFAULT 0,
    Round7 TINYINT DEFAULT 0,
    Round8 TINYINT DEFAULT 0,
    FrontRun TINYINT DEFAULT 0,
    BackRun TINYINT DEFAULT 0,
    FiveStand VARCHAR(1)
);

-- top 3 scores only
CREATE OR REPLACE VIEW claysData AS
SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, seqnum
FROM (
         WITH s AS (
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, fivestand, row_number() OVER (PARTITION BY athlete ORDER BY total DESC) AS seqnum
             FROM (
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round1 total
                           , CASE WHEN fivestand = 'Y' THEN 1 ELSE 0 END AS fivestand
                      FROM clays s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round2 total
                           , CASE WHEN fivestand = 'Y' THEN 1 ELSE 0 END AS fivestand
                      FROM clays s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round3 total
                           , CASE WHEN fivestand = 'Y' THEN 1 ELSE 0 END AS fivestand
                      FROM clays s
                      UNION ALL
                      SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
                           , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
                           , s.round1, s.round2, s.round3, s.round4
                           , s.round4 total
                           , CASE WHEN fivestand = 'Y' THEN 1 ELSE 0 END AS fivestand
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
             SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, round3, round4, total, fourth, fivestand
             FROM (
                      SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY unreal.athlete ORDER BY total DESC) AS fourth
                           , CASE WHEN fivestand = 'Y' THEN 1 ELSE 0 END AS fivestand
                      FROM s,
                           (SELECT s3.athlete, CASE WHEN COUNT(DISTINCT s3.locationid) = 1 THEN 'Next' WHEN SUM(s3.fivestand) > 0 THEN 'fivestandused' ELSE 'Four' END numberfour, (SELECT DISTINCT s3.locationid) dontuselocid
                            FROM s
                                     INNER JOIN s3 ON s.athlete = s3.athlete
                            GROUP BY s.athlete) unreal
                      WHERE s.athlete = unreal.athlete
                        AND seqnum > 2
                        AND CASE WHEN unreal.numberfour = 'four' THEN seqnum = 3 WHEN unreal.numberfour = 'fivestandused' THEN fivestand = 0 AND seqnum >= 3 ELSE locationid != dontuselocid END
                  ) bananas
             WHERE fourth = 1
         )
     ) AS a
WHERE total > 0;

CREATE OR REPLACE VIEW claysAggregate AS
SELECT athlete, classification, gender, team, SUM(total) total
FROM (
         SELECT athlete, classification, gender, total, team
         FROM claysdata
     ) a
GROUP BY athlete, classification, gender
ORDER BY total DESC;

CREATE OR REPLACE VIEW claysTeamAggregate AS
SELECT team, classification, SUM(total) total
FROM (
         SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER
             BY total DESC ) AS segnum
         FROM claysaggregate
         ORDER BY team, classification, total DESC
     ) a
WHERE segnum <= 3
GROUP BY team, classification
ORDER BY total DESC;

CREATE OR REPLACE VIEW claysTeamScores AS
SELECT sta.team, sdts.classification, athlete, sdts.total indtotal, sta.total teamtotal
FROM claysTeamAggregate sta
         INNER JOIN (SELECT team, classification, athlete, SUM(total) total
                     FROM (
                              SELECT team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END classification, athlete, total, row_number() OVER (PARTITION BY team, CASE WHEN classification IN ('Senior/Jr. Varsity', 'Senior/Varsity', 'Junior Varsity') THEN 'Varsity' WHEN classification IN ('Intermediate Entry', 'Intermediate Advanced') THEN 'Intermediate Entry' ELSE classification END ORDER BY total DESC ) AS segnum
                              FROM claysaggregate
                              ORDER BY team, classification, total DESC
                          ) a
                     WHERE segnum <= 3
                     GROUP BY team, classification, athlete) sdts ON sta.team = sdts.team AND sta.classification = sdts.classification
ORDER BY sta.total DESC, sdts.total DESC;

CREATE OR REPLACE VIEW allData AS
    SELECT eventid, event, locationid, location, eventdate, squadname, '' station, team, athlete, classification, gender, round1, round2, round3, round4, round5, round6, round7, round8, 0 as frontrun, 0 as backrun, 'N' as fivestand, 'singles' as type
    FROM singles
    UNION
    SELECT *, 'N', 'doubles' as type
    FROM doubles
    UNION
    SELECT *, 'N', 'handicap' as type
    FROM handicap
    UNION
    SELECT *, 'N', 'skeet' as type
    FROM skeet
    UNION
    SELECT *, 'clays' as type
    FROM clays;

CREATE OR REPLACE VIEW allTeamScores AS
    SELECT *, 'singles' type
    FROM singlesTeamScores
    UNION
    SELECT *, 'doubles'
    FROM doublesTeamScores
    UNION
    SELECT *, 'handicap'
    FROM handicapTeamScores
    UNION
    SELECT *, 'skeet'
    FROM skeetTeamScores
    UNION
    SELECT *, 'clays'
    FROM claysTeamScores;
