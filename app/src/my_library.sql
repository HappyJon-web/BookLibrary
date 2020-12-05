BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "my_library" (
	"_id"	INTEGER,
	"book_title"	TEXT,
	"book_author"	TEXT,
	"book_pages"	INTEGER,
	"book_fav"	INTEGER,
	PRIMARY KEY("_id" AUTOINCREMENT)
);
INSERT INTO "my_library" ("_id","book_title","book_author","book_pages","book_fav") VALUES (1,'Lord of the Rings','J.K. Howlings',290,0),
 (2,'The Hunger Games','Suzanne Collins',359,0),
 (3,'Erica','S. Riley',359,0),
 (4,'If Malaysia was Anime','Ernest Ng',112,0),
 (6,'When I was a kid','Boey',223,0),
 (7,'Diary of a Wimpy Kid','Jeff Kinney',229,0),
 (8,'Come Follow Me','Ian Malins',112,0),
 (9,'Maneater','Oswald Green',114,0),
 (10,'Hope''s Story','Jonathan Ang',32,0);
COMMIT;
