-- =============================================================
-- Seed: Sports data (Olympic + Popular Indian Sports)
-- Only inserts if not already present
-- =============================================================

INSERT IGNORE INTO sports (name, description, category, is_olympic_sport, icon_url, matches_per_level, wins_required_to_advance) VALUES
-- Track & Field
('Athletics - 100m Sprint',    'Sprint racing on a straight 100-meter track.',                      'TRACK_AND_FIELD', true,  '/icons/athletics.png',    5, 3),
('Athletics - Long Jump',      'Jump as far as possible from a running start.',                     'TRACK_AND_FIELD', true,  '/icons/longjump.png',     5, 3),
('Athletics - Javelin Throw',  'Throw a javelin spear for maximum distance.',                       'TRACK_AND_FIELD', true,  '/icons/javelin.png',      5, 3),
('Marathon',                   'Long-distance running race over 42.195 km.',                        'TRACK_AND_FIELD', true,  '/icons/marathon.png',     5, 3),

-- Aquatics
('Swimming - Freestyle',       '100m freestyle swimming in a pool.',                                'AQUATICS',        true,  '/icons/swimming.png',     5, 3),
('Swimming - Butterfly',       '100m butterfly stroke swimming.',                                   'AQUATICS',        true,  '/icons/butterfly.png',    5, 3),
('Diving',                     'Perform acrobatic dives from platform or springboard.',             'AQUATICS',        true,  '/icons/diving.png',       5, 3),

-- Combat
('Boxing',                     'Compete in timed rounds of boxing against an opponent.',            'COMBAT',          true,  '/icons/boxing.png',       5, 3),
('Wrestling - Freestyle',      'Pin your opponent to the mat using wrestling techniques.',          'COMBAT',          true,  '/icons/wrestling.png',    5, 3),
('Judo',                       'Throw or takedown your opponent using judo techniques.',            'COMBAT',          true,  '/icons/judo.png',         5, 3),
('Taekwondo',                  'Score points by landing kicks and punches on the opponent.',        'COMBAT',          true,  '/icons/taekwondo.png',    5, 3),
('Fencing',                    'Score points by landing touches with your weapon.',                 'COMBAT',          true,  '/icons/fencing.png',      5, 3),

-- Racket Sports
('Badminton',                  'Rally a shuttlecock over a net using rackets.',                     'RACKET',          true,  '/icons/badminton.png',    5, 3),
('Table Tennis',               'Fast-paced ping pong played on a table.',                          'RACKET',          true,  '/icons/tabletennis.png',  5, 3),
('Tennis',                     'Rally a tennis ball over a net on a full-sized court.',            'RACKET',          true,  '/icons/tennis.png',       5, 3),

-- Team Sports
('Football (Soccer)',          'Score goals by kicking the ball into the opponent''s net.',         'TEAM',            true,  '/icons/football.png',     5, 3),
('Basketball',                 'Score by shooting the ball through the opponent''s hoop.',          'TEAM',            true,  '/icons/basketball.png',   5, 3),
('Volleyball',                 'Score points by landing the ball in the opponent''s court.',        'TEAM',            true,  '/icons/volleyball.png',   5, 3),
('Hockey (Field)',             'Score goals using a stick and ball on a field.',                    'TEAM',            true,  '/icons/fieldhockey.png',  5, 3),
('Rugby Sevens',               'Score tries by carrying the ball across the opponent''s line.',     'TEAM',            true,  '/icons/rugby.png',        5, 3),

-- Gymnastics
('Artistic Gymnastics',        'Perform routines on apparatus like floor, beam, and bars.',         'GYMNASTICS',      true,  '/icons/gymnastics.png',   5, 3),
('Rhythmic Gymnastics',        'Perform routines with apparatus like ribbon, hoop, and ball.',      'GYMNASTICS',      true,  '/icons/rhythmic.png',     5, 3),
('Trampoline',                 'Perform acrobatic tricks while bouncing on a trampoline.',          'GYMNASTICS',      true,  '/icons/trampoline.png',   5, 3),

-- Shooting
('10m Air Rifle',              'Shoot a target at 10 meters with an air rifle.',                    'SHOOTING',        true,  '/icons/rifle.png',        5, 3),
('25m Pistol',                 'Shoot a target at 25 meters with a pistol.',                        'SHOOTING',        true,  '/icons/pistol.png',       5, 3),
('Archery',                    'Hit a target with arrows from a set distance.',                     'SHOOTING',        true,  '/icons/archery.png',      5, 3),

-- Cycling
('Cycling - Road Race',        'Cycle long distances on open roads for fastest time.',              'CYCLING',         true,  '/icons/cycling.png',      5, 3),
('Cycling - Track Sprint',     'Short-distance sprint cycling on a velodrome track.',               'CYCLING',         true,  '/icons/cyclingtrack.png', 5, 3),

-- Traditional / Popular Indian Sports
('Cricket',                    'Score runs by hitting the ball and running between wickets.',        'TEAM',            false, '/icons/cricket.png',      5, 3),
('Kabaddi',                    'Tag opponents and return to your half without being tackled.',       'TEAM',            false, '/icons/kabaddi.png',      5, 3),
('Kho-Kho',                    'Chase and tag opponents in this traditional Indian sport.',          'TEAM',            false, '/icons/khokho.png',       5, 3),
('Carrom',                     'Flick coins into pockets on a carrom board.',                       'TRADITIONAL',     false, '/icons/carrom.png',       5, 3),
('Chess',                      'Outmaneuver your opponent and checkmate their king.',               'TRADITIONAL',     false, '/icons/chess.png',        5, 3),
('Rowing',                     'Row a boat across a course as fast as possible.',                   'AQUATICS',        true,  '/icons/rowing.png',       5, 3),
('Weightlifting',              'Lift the maximum weight above your head in clean & jerk / snatch.', 'COMBAT',          true,  '/icons/weightlifting.png',5, 3);

-- =============================================================
-- Set sport-specific score types (runs every startup — idempotent)
-- =============================================================

-- Track & Field
UPDATE sports SET score_type = 'TIME_LOWER_WINS'      WHERE name = 'Athletics - 100m Sprint';
UPDATE sports SET score_type = 'DISTANCE_HIGHER_WINS' WHERE name = 'Athletics - Long Jump';
UPDATE sports SET score_type = 'DISTANCE_HIGHER_WINS' WHERE name = 'Athletics - Javelin Throw';
UPDATE sports SET score_type = 'TIME_LOWER_WINS'      WHERE name = 'Marathon';

-- Aquatics
UPDATE sports SET score_type = 'TIME_LOWER_WINS'      WHERE name = 'Swimming - Freestyle';
UPDATE sports SET score_type = 'TIME_LOWER_WINS'      WHERE name = 'Swimming - Butterfly';
UPDATE sports SET score_type = 'ARTISTIC_SCORE'       WHERE name = 'Diving';
UPDATE sports SET score_type = 'TIME_LOWER_WINS'      WHERE name = 'Rowing';

-- Combat
UPDATE sports SET score_type = 'COMBAT'               WHERE name = 'Boxing';
UPDATE sports SET score_type = 'COMBAT'               WHERE name = 'Wrestling - Freestyle';
UPDATE sports SET score_type = 'COMBAT'               WHERE name = 'Judo';
UPDATE sports SET score_type = 'COMBAT'               WHERE name = 'Taekwondo';
UPDATE sports SET score_type = 'POINTS'               WHERE name = 'Fencing';
UPDATE sports SET score_type = 'WEIGHTLIFTING'        WHERE name = 'Weightlifting';

-- Racket sports
UPDATE sports SET score_type = 'SETS'                 WHERE name = 'Badminton';
UPDATE sports SET score_type = 'SETS'                 WHERE name = 'Table Tennis';
UPDATE sports SET score_type = 'SETS'                 WHERE name = 'Tennis';

-- Team sports
UPDATE sports SET score_type = 'GOALS'                WHERE name = 'Football (Soccer)';
UPDATE sports SET score_type = 'POINTS'               WHERE name = 'Basketball';
UPDATE sports SET score_type = 'SETS'                 WHERE name = 'Volleyball';
UPDATE sports SET score_type = 'GOALS'                WHERE name = 'Hockey (Field)';
UPDATE sports SET score_type = 'POINTS'               WHERE name = 'Rugby Sevens';

-- Gymnastics
UPDATE sports SET score_type = 'ARTISTIC_SCORE'       WHERE name = 'Artistic Gymnastics';
UPDATE sports SET score_type = 'ARTISTIC_SCORE'       WHERE name = 'Rhythmic Gymnastics';
UPDATE sports SET score_type = 'ARTISTIC_SCORE'       WHERE name = 'Trampoline';

-- Shooting
UPDATE sports SET score_type = 'ACCURACY'             WHERE name = '10m Air Rifle';
UPDATE sports SET score_type = 'ACCURACY'             WHERE name = '25m Pistol';
UPDATE sports SET score_type = 'ACCURACY'             WHERE name = 'Archery';

-- Cycling
UPDATE sports SET score_type = 'TIME_LOWER_WINS'      WHERE name = 'Cycling - Road Race';
UPDATE sports SET score_type = 'TIME_LOWER_WINS'      WHERE name = 'Cycling - Track Sprint';

-- Traditional / Indian sports
UPDATE sports SET score_type = 'CRICKET'              WHERE name = 'Cricket';
UPDATE sports SET score_type = 'KABADDI'              WHERE name = 'Kabaddi';
UPDATE sports SET score_type = 'POINTS'               WHERE name = 'Kho-Kho';
UPDATE sports SET score_type = 'POINTS'               WHERE name = 'Carrom';
UPDATE sports SET score_type = 'POINTS'               WHERE name = 'Chess';
