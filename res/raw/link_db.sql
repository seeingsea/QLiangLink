CREATE TABLE t_user (userId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, beans INTEGER );
CREATE TABLE t_level (levelId INTEGER PRIMARY KEY UNIQUE, enabled INTEGER, passed INTEGER, unlockCost INTEGER, maxTime INTEGER, score INTEGER, imageNum INTEGER, rowNum INTEGER, colNum INTEGER , iconSet TEXT );
INSERT INTO [t_level] ([levelId], [enabled], [passed], [unlockCost], [maxTime], [score], [imageNum], [rowNum], [colNum], [iconSet]) VALUES (1, 0, 0, 40, 90, 0, 6, 10, 7, 'a');
INSERT INTO [t_level] ([levelId], [enabled], [passed], [unlockCost], [maxTime], [score], [imageNum], [rowNum], [colNum], [iconSet]) VALUES (2, 0, 0, 40, 90, 0, 6, 10, 7, 'a');
INSERT INTO [t_level] ([levelId], [enabled], [passed], [unlockCost], [maxTime], [score], [imageNum], [rowNum], [colNum], [iconSet]) VALUES (3, 0, 0, 60, 120, 0, 10, 11, 8, 'c');
INSERT INTO [t_level] ([levelId], [enabled], [passed], [unlockCost], [maxTime], [score], [imageNum], [rowNum], [colNum], [iconSet]) VALUES (4, 0, 0, 60, 120, 0, 10, 11, 8, 'c');
INSERT INTO [t_level] ([levelId], [enabled], [passed], [unlockCost], [maxTime], [score], [imageNum], [rowNum], [colNum], [iconSet]) VALUES (5, 0, 0, 90, 120, 0, 15, 10, 7, 'd');
INSERT INTO [t_level] ([levelId], [enabled], [passed], [unlockCost], [maxTime], [score], [imageNum], [rowNum], [colNum], [iconSet]) VALUES (6, 0, 0, 120, 150, 0, 25, 11, 8, 'd');