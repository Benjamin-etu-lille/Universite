package fr.univlille.s302.model;

import java.util.List;
import java.util.Objects;

import com.opencsv.bean.CsvBindByName;

/**
 * Classe {@code Pokemon} représentant une instance de données pour un Pokémon.
 *
 * Cette classe implémente l'interface {@link Data} et permet de manipuler les
 * attributs d'un Pokémon, y compris son nom, son type et ses caractéristiques
 * (comme les points de vie, l'attaque, etc.). Elle permet également de comparer
 * les Pokémon entre eux.
 *
 * @author Louis Bedu
 * @version 1.0
 */
public class Pokemon implements Data {
    
    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "attack")
    private int attack;
    @CsvBindByName(column = "base_egg_steps")
    private int base_egg_steps;
    @CsvBindByName(column = "capture_rate")
    private double capture_rate;
    @CsvBindByName(column = "defense")
    private int defense;
    @CsvBindByName(column = "experience_growth")
    private int experience_growth;
    @CsvBindByName(column = "hp")
    private int hp;
    @CsvBindByName(column = "sp_attack")
    private int sp_attack;
    @CsvBindByName(column = "sp_defense")
    private int sp_defense;
    @CsvBindByName(column = "type1")
    private String type1;
    @CsvBindByName(column = "type2")
    private String type2;
    @CsvBindByName(column = "speed")
    private double speed;
    @CsvBindByName(column = "is_legendary")
    private boolean isLegendary;


    public Pokemon() {}

    public Pokemon(String name, int attack, int base_egg_steps, double capture_rate, int defense, int experience_growth, int hp, int sp_attack, int sp_defense, String type1, String type2, double speed, boolean is_legendary) {
        this.name = name;
        this.attack = attack;
        this.base_egg_steps = base_egg_steps;
        this.capture_rate = capture_rate;
        this.defense = defense;
        this.experience_growth = experience_growth;
        this.hp = hp;
        this.sp_attack = sp_attack;
        this.sp_defense = sp_defense;
        this.type1 = type1;
        this.type2 = type2;
        this.speed = speed;
        this.isLegendary = is_legendary;
    }

    public Pokemon(int attack, int base_egg_steps, double capture_rate, int defense, int experience_growth, int hp, int sp_attack, int sp_defense, double speed) {
        this("Unknown", attack, base_egg_steps, capture_rate, defense, experience_growth, hp, sp_attack, sp_defense, "Unknown", "", speed, false);
    }

    @Override
    public void setNewUnknownData(List<String> attributes) {
        this.name = "Unknown";
        this.attack = Integer.parseInt(attributes.get(0));
        this.base_egg_steps = Integer.parseInt(attributes.get(1));
        this.capture_rate = Double.parseDouble(attributes.get(2));
        this.defense = Integer.parseInt(attributes.get(3));
        this.experience_growth = Integer.parseInt(attributes.get(4));
        this.hp = Integer.parseInt(attributes.get(5));
        this.sp_attack = Integer.parseInt(attributes.get(6));
        this.sp_defense = Integer.parseInt(attributes.get(7));
        this.type1 = "Unknown";
        this.type2 = "";
        this.speed = Double.parseDouble(attributes.get(8));
        this.isLegendary = false;
    }

    @Override
    public String toString() {
        return "Pokemon [name=" + name
            + ", attack=" + attack
            + ", base_egg_steps=" + base_egg_steps
            + ", capture_rate="+ capture_rate
            + ", defense=" + defense
            + ", experience_growth=" + experience_growth
            + ", hp=" + hp
            + ", sp_attack=" + sp_attack
            + ", sp_defense=" + sp_defense
            + ", type1=" + type1
            + ", type2=" + type2
            + ", speed=" + speed
            + ", is_legendary=" + isLegendary + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pokemon pokemon)) return false;
        return Objects.equals(this.name, pokemon.name)
            && Integer.compare(this.attack, pokemon.attack) == 0
            && Integer.compare(this.base_egg_steps, pokemon.base_egg_steps) == 0
            && Double.compare(this.capture_rate, pokemon.capture_rate) == 0
            && Integer.compare(this.defense, pokemon.defense) == 0
            && Integer.compare(this.experience_growth, pokemon.experience_growth) == 0
            && Integer.compare(this.hp, pokemon.hp) == 0
            && Integer.compare(this.sp_attack, pokemon.sp_attack) == 0
            && Integer.compare(this.sp_defense, pokemon.sp_defense) == 0
            && Objects.equals(this.type1, pokemon.type1)
            && Objects.equals(this.type2, pokemon.type2)
            && Double.compare(this.speed, pokemon.speed) == 0
            && Objects.equals(this.isLegendary, pokemon.isLegendary);
    }

    @Override
    public String getType() {
        if(this.isLegendary) return "Legendary"; 
        if(this.name.equals("Unknown")) return "Unknown";
        return "Not Legendary";
    }

    @Override
    public void setType(String type) {
        if(type.equals("Legendary")) this.isLegendary = true;
        if(type.equals("Not Legendary")) this.isLegendary = false;
    }

    @Override
    public double getAttributeByName(String name) {
        if (name.equals("Attack")) return this.attack;
        if (name.equals("Base Egg Steps")) return this.base_egg_steps;
        if (name.equals("Capture Rate")) return this.capture_rate;
        if (name.equals("Defense")) return this.defense;
        if (name.equals("Experience Growth")) return this.experience_growth;
        if (name.equals("Hp")) return this.hp;
        if (name.equals("Sp Attack")) return this.sp_attack;
        if (name.equals("Sp Defense")) return this.sp_defense;
        if (name.equals("Speed")) return this.speed;
        return -1;
    }

    @Override
    public String[] getAttributes() {
        return new String[]{"Attack", "Base Egg Steps", "Capture Rate", "Defense", "Experience Growth", "Hp", "Sp Attack", "Sp Defense", "Speed"};
    }

    @Override
    public double[] attibutesToArray() {
        return new double[]{this.attack, this.base_egg_steps, this.capture_rate, this.defense, this.experience_growth, this.hp, this.sp_attack, this.sp_defense, this.speed};
    }
}
