package com.hotel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Gestió de reserves d'un hotel.
 */
public class App {

    // --------- CONSTANTS I VARIABLES GLOBALS ---------

    // Tipus d'habitació
    public static final String TIPUS_ESTANDARD = "Estàndard";
    public static final String TIPUS_SUITE = "Suite";
    public static final String TIPUS_DELUXE = "Deluxe";

    // Serveis addicionals
    public static final String SERVEI_ESMORZAR = "Esmorzar";
    public static final String SERVEI_GIMNAS = "Gimnàs";
    public static final String SERVEI_SPA = "Spa";
    public static final String SERVEI_PISCINA = "Piscina";

    // Capacitat inicial
    public static final int CAPACITAT_ESTANDARD = 30;
    public static final int CAPACITAT_SUITE = 20;
    public static final int CAPACITAT_DELUXE = 10;

    // IVA
    public static final float IVA = 0.21f;

    // Scanner únic
    public static Scanner sc = new Scanner(System.in);

    // HashMaps de consulta
    public static HashMap<String, Float> preusHabitacions = new HashMap<String, Float>();
    public static HashMap<String, Integer> capacitatInicial = new HashMap<String, Integer>();
    public static HashMap<String, Float> preusServeis = new HashMap<String, Float>();

    // HashMaps dinàmics
    public static HashMap<String, Integer> disponibilitatHabitacions = new HashMap<String, Integer>();
    public static HashMap<Integer, ArrayList<String>> reserves = new HashMap<Integer, ArrayList<String>>();

    // Generador de nombres aleatoris per als codis de reserva
    public static Random random = new Random();

    // --------- MÈTODE MAIN ---------

    /**
     * Mètode principal. Mostra el menú en un bucle i gestiona l'opció triada
     * fins que l'usuari decideix eixir.
     */
    public static void main(String[] args) {
        inicialitzarPreus();

        int opcio = 0;
        do {
            mostrarMenu();
            opcio = llegirEnter("Seleccione una opció: ");
            gestionarOpcio(opcio);
        } while (opcio != 6);

        System.out.println("Eixint del sistema... Gràcies per utilitzar el gestor de reserves!");
    }

    // --------- MÈTODES DEMANATS ---------

    /**
     * Configura els preus de les habitacions, serveis addicionals i
     * les capacitats inicials en els HashMaps corresponents.
     */
    public static void inicialitzarPreus() {
        // Preus habitacions
        preusHabitacions.put(TIPUS_ESTANDARD, 50f);
        preusHabitacions.put(TIPUS_SUITE, 100f);
        preusHabitacions.put(TIPUS_DELUXE, 150f);

        // Capacitats inicials
        capacitatInicial.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        capacitatInicial.put(TIPUS_SUITE, CAPACITAT_SUITE);
        capacitatInicial.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Disponibilitat inicial (comença igual que la capacitat)
        disponibilitatHabitacions.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        disponibilitatHabitacions.put(TIPUS_SUITE, CAPACITAT_SUITE);
        disponibilitatHabitacions.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Preus serveis
        preusServeis.put(SERVEI_ESMORZAR, 10f);
        preusServeis.put(SERVEI_GIMNAS, 15f);
        preusServeis.put(SERVEI_SPA, 20f);
        preusServeis.put(SERVEI_PISCINA, 25f);
    }

    /**
     * Mostra el menú principal amb les opcions disponibles per a l'usuari.
     */
    public static void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Reservar una habitació");
        System.out.println("2. Alliberar una habitació");
        System.out.println("3. Consultar disponibilitat");
        System.out.println("4. Llistar reserves per tipus");
        System.out.println("5. Obtindre una reserva");
        System.out.println("6. Ixir");
    }

    /**
     * Processa l'opció seleccionada per l'usuari i crida el mètode corresponent.
     */
    public static void gestionarOpcio(int opcio) {
      switch (opcio) {
        case 1:
          reservarHabitacio();
          break;
        case 2:
          alliberarHabitacio();
          break;
        case 3:
          consultarDisponibilitat();
          break;
        case 4:
          obtindreReservaPerTipus();
          break;
        case 5:
          obtindreReserva();
          break;
        case 6:
          break;
        default:
          System.out.println("Opció no vàlida");
          break;
      }
    }

    /**
     * Gestiona tot el procés de reserva: selecció del tipus d'habitació,
     * serveis addicionals, càlcul del preu total i generació del codi de reserva.
     */
    public static void reservarHabitacio() {
        System.out.println("\n===== RESERVAR HABITACIÓ =====");

        String tipus = seleccionarTipusHabitacioDisponible();
        ArrayList<String> serveis = seleccionarServeis();
        float total = calcularPreuTotal(tipus, serveis);
        int codi = generarCodiReserva();

        disponibilitatHabitacions.put(tipus, disponibilitatHabitacions.get(tipus) - 1);

        ArrayList<String> dades = new ArrayList<>();
        dades.add(tipus);
        dades.add(String.valueOf(total));

        for (String s : serveis) {
            dades.add(s);
        }
        reserves.put(codi, dades);

        System.out.println("\nTOTAL: " + total + "€");
        System.out.println("\nReserva creada amb èxit!");
        System.out.println("\nCodi de reserva: " + codi);
    }

    /**
     * Pregunta a l'usuari un tipus d'habitació en format numèric i
     * retorna el nom del tipus.
     */
    public static String seleccionarTipusHabitacio() {
        int tipus = llegirEnter("Seleccione un tipus: ");

        switch(tipus) {
            default:
                return TIPUS_ESTANDARD;
            case 2:
                return TIPUS_SUITE;
            case 3:
                return TIPUS_DELUXE;
        } 
    }

    /**
     * Mostra la disponibilitat i el preu de cada tipus d'habitació,
     * demana a l'usuari un tipus i només el retorna si encara hi ha
     * habitacions disponibles. En cas contrari, retorna null.
     */
    public static String seleccionarTipusHabitacioDisponible() {
        System.out.println("\nTipus d'habitació disponibles:");

        System.out.print("1 ");
        mostrarInfoTipus(TIPUS_ESTANDARD);
        System.out.print("2 ");
        mostrarInfoTipus(TIPUS_SUITE);
        System.out.print("3 ");
        mostrarInfoTipus(TIPUS_DELUXE);

        String tipus = seleccionarTipusHabitacio();

        if(disponibilitatHabitacions.get(tipus) <= 0) {
          return null;
        }

        return tipus;
    }

    /**
     * Permet triar serveis addicionals (entre 0 i 4, sense repetir) i
     * els retorna en un ArrayList de String.
     */
    public static ArrayList<String> seleccionarServeis() {
        System.out.println("\nServeis addicionals (0-4):");
        System.out.println("0 - Finalitzar");
        System.out.println("1 - " + SERVEI_ESMORZAR + " (" + preusServeis.get(SERVEI_ESMORZAR) + "€)");
        System.out.println("2 - " + SERVEI_GIMNAS + " (" + preusServeis.get(SERVEI_GIMNAS) + "€)");
        System.out.println("3 - " + SERVEI_SPA + " (" + preusServeis.get(SERVEI_SPA) + "€)");
        System.out.println("4 - " + SERVEI_PISCINA + " (" + preusServeis.get(SERVEI_PISCINA) + "€)");
        
        ArrayList<String> resultat = new ArrayList<>();

        while (resultat.size() < 4) {
            System.out.print("\nVol afegir un servei? (s/n): ");

            char resposta = sc.next().charAt(0);
            if (resposta == 'n') break;
            
            int tipus = llegirEnter("\nSeleccione servei: ");
            if(tipus == 0) break;

            if(resultat.contains(tipus))  {
                System.out.println("Ya has triat este servei abans.");
                continue;
            }

            String servei;
            switch (tipus) {
                case 1:
                    servei = SERVEI_ESMORZAR;
                    break;
                case 2:
                    servei = SERVEI_GIMNAS;
                    break;
                case 3:
                    servei = SERVEI_SPA;
                    break;
                case 4:
                    servei = SERVEI_PISCINA;
                    break;
                default:
                    servei = null;
            }

            resultat.add(servei);
            System.out.println("Servei afegit: " + servei);
        }


        return resultat;
    }

    /**
     * Calcula i retorna el cost total de la reserva, incloent l'habitació,
     * els serveis seleccionats i l'IVA.
     */
    public static float calcularPreuTotal(String tipusHabitacio, ArrayList<String> serveisSeleccionats) {
        System.out.println("\nCalculem el total...");

        System.out.println("\nPreu habitació: " + preusHabitacions.get(tipusHabitacio) + "€");

        // Serveis
        float totalServeis = 0f;
        if(serveisSeleccionats.size() > 0) {
            System.out.print("\nServeis: ");

            for (String servei : serveisSeleccionats) {
                Float preuServei = preusServeis.get(servei);
                totalServeis += preuServei;
                System.out.print(servei + " (" + preuServei + "€) ");
            }
            System.out.print("\n");
        }

        // Subtotal
        float subtotal = preusHabitacions.get(tipusHabitacio) + totalServeis;
        System.out.println("\nSubtotal: " + subtotal + "€");

        // IVA (21%)
        float iva = subtotal * 0.21f;
        System.out.println("\nIVA (21%): " + iva + "€");

        return subtotal + iva;
    }

    /**
     * Genera i retorna un codi de reserva únic de tres xifres
     * (entre 100 i 999) que no estiga repetit.
     */
    public static int generarCodiReserva() {
        Random random = new Random(); 
        int code;
        do {
           code = 100 + random.nextInt(900);
        } while (reserves.containsKey(code));

        return code;
    }

    /**
     * Permet alliberar una habitació utilitzant el codi de reserva
     * i actualitza la disponibilitat.
     */
    public static void alliberarHabitacio() {
        System.out.println("\n===== ALLIBERAR HABITACIÓ =====");
        int codi = llegirEnter("Introdueix el codi de reserva: ");

        if(!reserves.containsKey(codi)) {
            System.out.println("\nReserva no trobada!"); 
            return;
        }

        ArrayList<String> dades = reserves.get(codi);
        String tipus = dades.get(0);

        System.out.println("\nReserva trobada!"); 

        disponibilitatHabitacions.put(tipus, disponibilitatHabitacions.get(tipus) + 1);
        reserves.remove(codi);

        System.out.println("\nHabitació alliberada correctament.");
        System.out.println("\nDisponibilitat actualitzada.");
    }

    /**
     * Mostra la disponibilitat actual de les habitacions (lliures i ocupades).
     */
    public static void consultarDisponibilitat() {
        System.out.println("\n===== DISPONIBILITAT D'HABITACIONS =====");
        System.out.println("\nTipus\t\tLliures\tOcupades");

        mostrarDisponibilitatTipus(TIPUS_ESTANDARD);
        mostrarDisponibilitatTipus(TIPUS_SUITE);
        mostrarDisponibilitatTipus(TIPUS_DELUXE);
    }

    /**
     * Funció recursiva. Mostra les dades de totes les reserves
     * associades a un tipus d'habitació.
     */
    public static void llistarReservesPerTipus(int[] codis, String tipus) {
        if (codis == null || codis.length == 0) {
            return;
        }

        int codi = codis[0];
        if (reserves.containsKey(codi)) {
            ArrayList<String> dades = reserves.get(codi);
            if (dades.size() > 0 && dades.get(0).equals(tipus)) {
                mostrarDadesReserva(codi);
            }
        }

        int[] newCodis = new int[codis.length - 1];
        System.arraycopy(codis, 1, newCodis, 0, newCodis.length);
        llistarReservesPerTipus(newCodis, tipus);
    }

    /**
     * Permet consultar els detalls d'una reserva introduint el codi.
     */
    public static void obtindreReserva() {
        System.out.println("\n===== CONSULTAR RESERVA =====");
        int codi = llegirEnter("Introdueix el codi de reserva: ");

        if (!reserves.containsKey(codi)) {
            System.out.println("No s'ha trobat cap reserva amb aquest codi.");
            return;
        }

        mostrarDadesReserva(codi);
    }

    /**
     * Mostra totes les reserves existents per a un tipus d'habitació
     * específic.
     */
    public static void obtindreReservaPerTipus() {
        System.out.println("\n===== CONSULTAR RESERVES PER TIPUS =====");
        System.out.println("\nSeleccione tipus:");
        System.out.println("1. " + TIPUS_ESTANDARD);
        System.out.println("2. " + TIPUS_SUITE);
        System.out.println("3. " + TIPUS_DELUXE);

        String tipus = seleccionarTipusHabitacio();

        int[] codis = new int[reserves.keySet().size()];
        int i = 0;
        for (Integer c : reserves.keySet()) {
            codis[i++] = c;
        }

        System.out.println("\nReserves del tipus \"" + tipus + "\":");
        llistarReservesPerTipus(codis, tipus);
    }

    /**
     * Consulta i mostra en detall la informació d'una reserva.
     */
    public static void mostrarDadesReserva(int codi) {
        if (!reserves.containsKey(codi)) {
            System.out.println("Codi no encontrado: " + codi);
            return;
        }

        ArrayList<String> dades = reserves.get(codi);

        System.out.println("\nDades de la reserva:");
        System.out.println("- Tipus d'habitació: " + dades.get(0));
        System.out.println("- Cost total: " + dades.get(1) + "€");

        if (dades.size() > 2) {
            System.out.println("- Serveis addicionals:");
            for (int i = 2; i < dades.size(); i++) {
                System.out.println("   * " + dades.get(i));
            }
        }
    }

    // --------- MÈTODES AUXILIARS (PER MILLORAR LEGIBILITAT) ---------

    /**
     * Llig un enter per teclat mostrant un missatge i gestiona possibles
     * errors d'entrada.
     */
    static int llegirEnter(String missatge) {
        int valor = 0;
        boolean correcte = false;
        while (!correcte) {
                System.out.print(missatge);
                valor = sc.nextInt();
                correcte = true;
        }
        return valor;
    }

    /**
     * Mostra per pantalla informació d'un tipus d'habitació: preu i
     * habitacions disponibles.
     */
    static void mostrarInfoTipus(String tipus) {
        int disponibles = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        float preu = preusHabitacions.get(tipus);
        System.out.println("- " + tipus + " (" + disponibles + " disponibles de " + capacitat + ") - " + preu + "€");
    }

    /**
     * Mostra la disponibilitat (lliures i ocupades) d'un tipus d'habitació.
     */
    static void mostrarDisponibilitatTipus(String tipus) {
        int lliures = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        int ocupades = capacitat - lliures;

        String etiqueta = tipus;
        if (etiqueta.length() < 8) {
            etiqueta = etiqueta + "\t"; // per a quadrar la taula
        }

        System.out.println(etiqueta + "\t" + lliures + "\t" + ocupades);
    }
}
