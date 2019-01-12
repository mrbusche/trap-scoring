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

CREATE OR REPLACE VIEW singlesData AS
WITH s AS (
	SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
    , CASE WHEN s.classification IN ('Senior/Varsity','Senior/Jr. Varsity') THEN 'Varsity' ELSE s.classification END classification
	, s.round1, s.round2
	, s.round1 + s.round2 total
	, row_number() OVER (PARTITION BY athlete ORDER BY round1 + round2 DESC) AS seqnum
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
	SELECT eventid, event, locationid, location, squadname, team, athlete, gender, classification, round1, round2, total, fourth
    FROM (
	SELECT eventid, event, locationid, location, squadname, team, unreal.athlete, gender, classification, round1, round2, total, seqnum, row_number() OVER (PARTITION BY athlete ORDER BY total DESC) AS fourth
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

/*CREATE OR REPLACE VIEW singlesData AS
WITH s AS (
	SELECT s.eventid, s.event, s.locationid, s.location, s.squadname, s.team, s.athlete, s.gender
    , CASE WHEN s.classification IN ('Senior/Varsity','Senior/Jr. Varsity') THEN 'Varsity' ELSE s.classification END classification
	, s.round1, s.round2
	, s.round1 + s.round2 total
	, row_number() OVER (PARTITION BY athlete ORDER BY round1 + round2 DESC) AS seqnum
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
	SELECT s.*
	FROM s
	WHERE (
		(SELECT COUNT(distinct locationid) FROM s3) > 1 AND seqnum = 4 ) OR
		((SELECT COUNT(distinct locationid) FROM s3) = 1 AND seqnum = (
		SELECT MIN(seqnum) FROM s where locationid not in (SELECT locationid FROM s3))
	)
);*/

CREATE OR REPLACE VIEW singlesAggregate AS
SELECT athlete, classification, gender, team, SUM(total) total
FROM (
	SELECT athlete, classification, gender, total, team
	FROM singlesdata
) a
GROUP BY athlete, classification, gender
ORDER BY total DESC;

CREATE OR REPLACE VIEW singlesTeamAggregate AS
SELECT team, gender, classification, SUM(total) total
FROM (
	SELECT team, gender, classification, total, row_number() OVER (PARTITION BY team ORDER BY total DESC ) AS segnum
	FROM singlesaggregate
	ORDER BY team, total DESC
) a
WHERE segnum <= 5
GROUP BY team, gender, classification
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
