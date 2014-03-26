drop table tb_music;
drop table tb_artist;
drop table tb_gamedata;

CREATE TABLE tb_music (
     m_id MEDIUMINT NOT NULL AUTO_INCREMENT,
     m_name CHAR(30) NOT NULL,
	 m_path varchar(255) NOT NULL,
	 artist MEDIUMINT NOT NULL,
     PRIMARY KEY (m_id)
);

CREATE TABLE tb_artist (
     a_id MEDIUMINT NOT NULL,
     a_name CHAR(30) NOT NULL,
     PRIMARY KEY (a_id));

CREATE TABLE tb_gamedata (
	g_id tinyint not null auto_increment,
	g_version int not null,
	PRIMARY KEY (g_id)
);