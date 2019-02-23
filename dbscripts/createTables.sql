set global local_infile=1;

USE trap;
CREATE TABLE IF NOT EXISTS singles (
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
    Round2 TINYINT,
    Round3 TINYINT,
    Round4 TINYINT,
    Round5 TINYINT,
    Round6 TINYINT,
    Round7 TINYINT,
    Round8 TINYINT,
    FrontRun TINYINT,
    BackRun TINYINT,
    RegisterDate VARCHAR(16),
    RegisteredBy VARCHAR(50),
    ShirtSize VARCHAR(5),
    ATAId VARCHAR(50),
    NSSAId VARCHAR(8),
    NSCAId VARCHAR(8),
    SCTPPayment VARCHAR(16),
    SCTPConsent VARCHAR(16),
    ATAPayment VARCHAR(16),
    NSCAPayment VARCHAR(16),
    NSSAPaymenT VARCHAR(16)
);

-- top 4 scores
CREATE OR REPLACE VIEW singlesData AS
WITH s AS (
  SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
    , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
  , s.round1, s.round2, s.round3, s.round4
  , GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(s.round1 + s.round2, s.round2 + s.round3), s.round3 + s.round4), s.round4 + s.round5), s.round5 + s.round6), s.round6 + s.round7), s.round7 + s.round8) total
  , row_number() OVER (PARTITION BY athlete ORDER BY GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(s.round1 + s.round2, s.round2 + s.round3), s.round3 + s.round4), s.round4 + s.round5), s.round5 + s.round6), s.round6 + s.round7), s.round7 + s.round8) DESC) AS seqnum
  FROM singles s
    WHERE s.locationid > 0
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
  SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY athlete ORDER BY total DESC) AS fourth
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
);

CREATE OR REPLACE VIEW singlesAggregate AS
SELECT athlete, classification, gender, team, SUM(total) total
FROM (
  SELECT athlete, classification, gender, total, team
  FROM singlesdata
) a
GROUP BY athlete, classification, gender
ORDER BY total DESC;

CREATE OR REPLACE VIEW singlesTeamAggregate AS
SELECT team, classification, SUM(total) total
FROM (
  SELECT team, classification, total, row_number() OVER (PARTITION BY team, classification ORDER BY total DESC ) AS segnum
  FROM singlesaggregate
  ORDER BY team, classification, total DESC
) a
WHERE segnum <= 5
GROUP BY team, classification
ORDER BY total DESC;

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
    Round2 TINYINT,
    Round3 TINYINT,
    Round4 TINYINT,
    Round5 TINYINT,
    Round6 TINYINT,
    Round7 TINYINT,
    Round8 TINYINT,
    FrontRun TINYINT,
    BackRun TINYINT,
    RegisterDate VARCHAR(16),
    RegisteredBy VARCHAR(50),
    ShirtSize VARCHAR(5),
    ATAId VARCHAR(50),
    NSSAId VARCHAR(8),
    NSCAId VARCHAR(8),
    SCTPPayment VARCHAR(16),
    SCTPConsent VARCHAR(16),
    ATAPayment VARCHAR(16),
    NSCAPayment VARCHAR(16),
    NSSAPaymenT VARCHAR(16)
);

-- top 4 scores for individual rounds
CREATE OR REPLACE VIEW doublesData AS
WITH s AS (
  SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
  , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
  , s.round1, s.round2, s.round3, s.round4
  , GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(s.round1, s.round2), s.round3), s.round4), s.round5), s.round6), s.round7), s.round8) total
  , row_number() OVER (PARTITION BY athlete ORDER BY GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(s.round1, s.round2), s.round3), s.round4), s.round5), s.round6), s.round7), s.round8) DESC) AS seqnum
  FROM doubles s
    WHERE s.locationid > 0
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
  SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY athlete ORDER BY total DESC) AS fourth
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
);

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
  SELECT team, classification, total, row_number() OVER (PARTITION BY team, classification ORDER BY total DESC ) AS segnum
  FROM doublesaggregate
  ORDER BY team, classification, total DESC
) a
WHERE segnum <= 5
GROUP BY team, classification
ORDER BY total DESC;

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
    Round2 TINYINT,
    Round3 TINYINT,
    Round4 TINYINT,
    Round5 TINYINT,
    Round6 TINYINT,
    Round7 TINYINT,
    Round8 TINYINT,
    FrontRun TINYINT,
    BackRun TINYINT,
    RegisterDate VARCHAR(16),
    RegisteredBy VARCHAR(50),
    ShirtSize VARCHAR(5),
    ATAId VARCHAR(50),
    NSSAId VARCHAR(8),
    NSCAId VARCHAR(8),
    SCTPPayment VARCHAR(16),
    SCTPConsent VARCHAR(16),
    ATAPayment VARCHAR(16),
    NSCAPayment VARCHAR(16),
    NSSAPaymenT VARCHAR(16)
);

-- top 4 scores
CREATE OR REPLACE VIEW handicapData AS
WITH s AS (
  SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
  , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
  , s.round1, s.round2, s.round3, s.round4
  , GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(s.round1 + s.round2, s.round2 + s.round3), s.round3 + s.round4), s.round4 + s.round5), s.round5 + s.round6), s.round6 + s.round7), s.round7 + s.round8) total
  , row_number() OVER (PARTITION BY athlete ORDER BY GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(s.round1 + s.round2, s.round2 + s.round3), s.round3 + s.round4), s.round4 + s.round5), s.round5 + s.round6), s.round6 + s.round7), s.round7 + s.round8) DESC) AS seqnum
  FROM handicap s
    WHERE s.locationid > 0
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
  SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY athlete ORDER BY total DESC) AS fourth
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
);

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
  SELECT team, classification, total, row_number() OVER (PARTITION BY team, classification ORDER BY total DESC ) AS segnum
  FROM handicapaggregate
  ORDER BY team, classification, total DESC
) a
WHERE segnum <= 5
GROUP BY team, classification
ORDER BY total DESC;

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
    Round2 TINYINT,
    Round3 TINYINT,
    Round4 TINYINT,
    Round5 TINYINT,
    Round6 TINYINT,
    Round7 TINYINT,
    Round8 TINYINT,
    FrontRun TINYINT,
    BackRun TINYINT,
    RegisterDate VARCHAR(16),
    RegisteredBy VARCHAR(50),
    ShirtSize VARCHAR(5),
    ATAId VARCHAR(50),
    NSSAId VARCHAR(8),
    NSCAId VARCHAR(8),
    SCTPPayment VARCHAR(16),
    SCTPConsent VARCHAR(16),
    ATAPayment VARCHAR(16),
    NSCAPayment VARCHAR(16),
    NSSAPaymenT VARCHAR(16)
);

-- top 4 scores only
CREATE OR REPLACE VIEW skeetData AS
WITH s AS (
  SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, replace(s.team, 'Club', 'Team') AS team, s.athlete, s.gender
    , CASE WHEN s.classification = 'Senior/Varsity' THEN 'Varsity' WHEN s.classification = 'Senior/Jr. Varsity' THEN 'Junior Varsity' WHEN s.classification = 'Intermediate/Advanced' THEN 'Intermediate Advanced' WHEN s.classification = 'Intermediate/Entry Level' THEN 'Intermediate Entry' WHEN s.classification = 'Rookie' THEN 'Rookie' ELSE s.classification END classification
  , s.round1, s.round2, s.round3, s.round4
  , GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(s.round1 + s.round2, s.round2 + s.round3), s.round3 + s.round4), s.round4 + s.round5), s.round5 + s.round6), s.round6 + s.round7), s.round7 + s.round8) total
  , row_number() OVER (PARTITION BY athlete ORDER BY GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(GREATEST(s.round1 + s.round2, s.round2 + s.round3), s.round3 + s.round4), s.round4 + s.round5), s.round5 + s.round6), s.round6 + s.round7), s.round7 + s.round8) DESC) AS seqnum
  FROM skeet s
    WHERE s.locationid > 0
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
      SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, round3, round4, total, seqnum, row_number() OVER (PARTITION BY athlete ORDER BY total DESC) AS fourth
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
);

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
  SELECT team, classification, total, row_number() OVER (PARTITION BY team, classification ORDER BY total DESC ) AS segnum
  FROM skeetaggregate
  ORDER BY team, classification, total DESC
) a
WHERE segnum <= 3
GROUP BY team, classification
ORDER BY total DESC;

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
    Round2 TINYINT,
    Round3 TINYINT,
    Round4 TINYINT,
    Round5 TINYINT,
    Round6 TINYINT,
    Round7 TINYINT,
    Round8 TINYINT,
    FrontRun TINYINT,
    BackRun TINYINT,
    RegisterDate VARCHAR(16),
    RegisteredBy VARCHAR(50),
    ShirtSize VARCHAR(5),
    ATAId VARCHAR(50),
    NSSAId VARCHAR(8),
    NSCAId VARCHAR(8),
    SCTPPayment VARCHAR(16),
    SCTPConsent VARCHAR(16),
    ATAPayment VARCHAR(16),
    NSCAPayment VARCHAR(16),
    NSSAPaymenT VARCHAR(16)
);


CREATE OR REPLACE VIEW allData AS
SELECT *, 'singles' as type
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
FROM clays;
