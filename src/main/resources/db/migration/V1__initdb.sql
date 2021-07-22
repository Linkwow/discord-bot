create sequence attributes_sequence increment by 1 start 1;
create sequence categories_sequence increment by 1 start 1;
create sequence factions_sequence increment by 1 start 1;
create sequence races_sequence increment by 1 start 1;
create sequence units_sequence increment by 1 start 1;
create sequence weapons_sequence increment by 1 start 1;

create table races
(
    id   serial primary key,
    name text not null unique
);

create table factions
(
    id     serial primary key,
    name   text     not null unique,
    race   smallint not null references races (id)
);

create table categories
(
    id   serial primary key,
    unit_category text not null
);

create table attributes
(
    id          serial primary key,
    description text not null unique
);

create table weapons
(
    id serial primary key,
    type text not null unique
);

create table units
(
    id                     serial primary key,
    name                   text     not null unique,
    faction                smallint references factions (id),
    category               smallint references categories (id),
    cost                   smallint not null,
    upkeep                 smallint not null,
    health                 smallint not null,
    leadership             smallint not null,
    speed                  smallint not null,
    melee_attack           smallint not null,
    melee_defence          smallint not null,
    charge_bonus           smallint not null,
    missile_resistance     smallint not null,
    magic_resistance       smallint not null,
    armor_protection       smallint not null,
    weapon_type            smallint references weapons(id),
    weapon_damage          smallint not null,
    armour_piercing_damage smallint not null,
    melee_interval         smallint not null,
    magical_attack         smallint not null,
    range                  smallint not null,
    unit_size              smallint not null,
    turns                  smallint not null
);

create table attributes_units
(
    attribute_id smallint references attributes (id),
    unit_id      smallint references units (id)
);




