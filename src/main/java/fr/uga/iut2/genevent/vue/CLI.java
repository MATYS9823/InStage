package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.controleur.Controleur;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

import fr.uga.iut2.genevent.modele.Concert;
import fr.uga.iut2.genevent.modele.Organisateur;
import org.apache.commons.validator.routines.EmailValidator;

public class CLI extends IHM {

    private static final int MAX_ESSAIS = 3;
    private final Controleur controleur;

    public CLI(Controleur controleur) {
        this.controleur = controleur;
    }

//-----  Commandes  ------------------------------------------------------------

    // commandes quand on est PAS connecté
    private static enum CommandeDeconnecte {
        QUITTER(0, "Quitter"),
        SE_CONNECTER(1, "Se connecter"),
        CREER_COMPTE(2, "Créer un compte organisateur"),
        ;

        private final int code;
        private final String description;
        private static final Map<Integer, CommandeDeconnecte> valueByCode = new HashMap<>();

        static {
            for (CommandeDeconnecte cmd : CommandeDeconnecte.values()) {
                CommandeDeconnecte.valueByCode.put(cmd.code, cmd);
            }
        }

        public static CommandeDeconnecte valueOfCode(int code) {
            CommandeDeconnecte result = CommandeDeconnecte.valueByCode.get(code);
            if (result == null) throw new IllegalArgumentException("Code invalide");
            return result;
        }

        private CommandeDeconnecte(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public String synopsis() {
            return this.code + " — " + this.description;
        }
    }

    // commandes quand on EST connecté
    private static enum CommandeConnecte {
        SE_DECONNECTER(0, "Se déconnecter"),
        CREER_CONCERT(1, "Créer un concert"),
        AFFICHER_CONCERT(2,"afficher un concert"),
        ;

        private final int code;
        private final String description;
        private static final Map<Integer, CommandeConnecte> valueByCode = new HashMap<>();

        static {
            for (CommandeConnecte cmd : CommandeConnecte.values()) {
                CommandeConnecte.valueByCode.put(cmd.code, cmd);
            }
        }

        public static CommandeConnecte valueOfCode(int code) {
            CommandeConnecte result = CommandeConnecte.valueByCode.get(code);
            if (result == null) throw new IllegalArgumentException("Code invalide");
            return result;
        }

        private CommandeConnecte(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public String synopsis() {
            return this.code + " — " + this.description;
        }
    }

//-----  Dialogues  ------------------------------------------------------------

    private void dialogueDeconnecte() {
        CommandeDeconnecte cmd;
        do {
            CLI.afficher("===== GenEvent =====");
            CLI.afficher(synopsisEnum(CommandeDeconnecte.values(), c -> c.synopsis()));
            CLI.afficher("====================");
            CLI.afficher("Saisir l'identifiant de l'action :");
            cmd = CLI.lireAvecErreurs(token -> {
                try {
                    return Optional.of(CommandeDeconnecte.valueOfCode(Integer.parseUnsignedInt(token)));
                } catch (IllegalArgumentException e) {
                    CLI.afficher("Choix invalide.");
                    return Optional.empty();
                }
            });

            switch (cmd) {
                case QUITTER:
                    break;
                case SE_CONNECTER:
                    this.controleur.saisirConnexion();
                    // si connecté, on bascule sur le menu connecté
                    if (this.controleur.estConnecte()) {
                        dialogueConnecte();
                    }
                    break;
                case CREER_COMPTE:
                    this.controleur.saisirOrganisateur();
                    break;
                default:
                    assert false : "Commande inconnue.";
            }
        } while (cmd != CommandeDeconnecte.QUITTER && !this.controleur.estConnecte());
    }

    private void dialogueConnecte() {
        CommandeConnecte cmd;
        do {
            CLI.afficher("===== GenEvent [connecté] =====");
            CLI.afficher(synopsisEnum(CommandeConnecte.values(), c -> c.synopsis()));
            CLI.afficher("===============================");
            CLI.afficher("Saisir l'identifiant de l'action :");
            cmd = CLI.lireAvecErreurs(token -> {
                try {
                    return Optional.of(CommandeConnecte.valueOfCode(Integer.parseUnsignedInt(token)));
                } catch (IllegalArgumentException e) {
                    CLI.afficher("Choix invalide.");
                    return Optional.empty();
                }
            });

            switch (cmd) {
                case SE_DECONNECTER:
                    this.controleur.seDeconnecter();
                    break;
                case CREER_CONCERT:
                    this.controleur.saisirConcert();
                    break;
                case AFFICHER_CONCERT:
                    this.controleur.afficherconcerts();
                    break;
                default:
                    assert false : "Commande inconnue.";
            }
        } while (cmd != CommandeConnecte.SE_DECONNECTER);
    }

    private InfosConnexion dialogueSaisirConnexion() {
        CLI.afficher("== Connexion ==");
        String email = CLI.lireEmail();
        String mdp;

        do {
            CLI.afficher("Saisir le mot de passe (ou '1' pour retrouver votre mot de passe) :");
            mdp = CLI.lireNom();
            if (mdp.equals("1")) {
                Organisateur org = this.controleur.getOrganisateur(email);
                if (org != null) {
                    CLI.afficher("Votre mot de passe est : " + org.getMdp());
                } else {
                    CLI.afficher("Aucun compte trouvé pour cet email.");
                }

            }
        } while (mdp.equals("1")) ;
        return new InfosConnexion(email, mdp);
    }

    private InfosOrganisateur dialogueSaisirOrganisateur() {
        CLI.afficher("== Créer un compte organisateur ==");
        String email = CLI.lireEmail();
        CLI.afficher("Saisir le nom :");
        String nom = CLI.lireNom();
        CLI.afficher("Saisir le prénom :");
        String prenom = CLI.lireNom();
        CLI.afficher("Saisir le mot de passe :");
        String mdp = CLI.lireNom();
        return new InfosOrganisateur(email, nom, prenom, mdp);  // ← ordre du constructeur
    }

    private InfosNouveauConcert dialogueSaisirNouveauConcert(final Set<String> nomsExistants) {
        CLI.afficher("== Créer un concert ==");
        CLI.afficher("Saisir le nom du concert :");
        String nom = CLI.lireNom(nomsExistants, true);
        CLI.afficher("Saisir le lieu :");
        String lieu = CLI.lireNom();
        CLI.afficher("Date de début :");
        LocalDate dateDebut = LocalDate.parse(CLI.lireDate().toString());
        CLI.afficher("Date de fin :");
        LocalDate dateFin = LocalDate.parse(CLI.lireDate().toString());
        CLI.afficher("artistes");
        String artistes = CLI.lireNom();
        return new InfosNouveauConcert(nom, dateDebut, dateFin, lieu,artistes);
    }



//-----  Implémentation des méthodes abstraites  -------------------------------

    @Override
    public final void demarrerInteraction() {
        // point d'entrée : toujours déconnecté au départ
        dialogueDeconnecte();

        // si on quitte depuis le menu connecté, on repasse par déconnecté
        while (this.controleur.estConnecte()) {
            dialogueDeconnecte();
        }
    }

    @Override
    public final void informerUtilisateur(final String msg, final boolean succes) {
        CLI.afficher((succes ? "[OK]" : "[KO]") + " " + msg);
    }

    @Override
    public void saisirConnexion() {
        InfosConnexion infos = dialogueSaisirConnexion();
        this.controleur.seConnecter(infos);
    }



    @Override
    public void saisirOrganisateur() {
        InfosOrganisateur infos = dialogueSaisirOrganisateur();
        this.controleur.creerOrganisateur(infos);
    }

    @Override
    public void saisirNouveauConcert(final Set<String> nomsExistants) {
        InfosNouveauConcert infos = dialogueSaisirNouveauConcert(nomsExistants);
        this.controleur.creerConcert(infos);
    }

    @Override
    public void saisirInscription() {
        saisirOrganisateur(); // même chose que créer un compte
    }

     @Override
     public void afficherConcerts(Map<String, Concert> concerts) {
         if (concerts.isEmpty()) {
             System.out.println("Aucun concert enregistré.");
             return;
         }
         for (Concert c : concerts.values()) {
             System.out.println("- " + c.getNom() + " | " + c.getLieu()
                     + " | " + c.getDate_debut() + " → " + c.getDate_fin());
         }
     }



    //-----  Primitives  -----------------------------------------------------------

    private static <T> String synopsisEnum(T[] values, Function<T, String> toSynopsis) {
        StringBuilder sb = new StringBuilder();
        for (T v : values) {
            sb.append("  ").append(toSynopsis.apply(v)).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private static <T> T lireAvecErreurs(final Function<String, Optional<T>> parseFunction) {
        Optional<T> result = Optional.empty();
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < CLI.MAX_ESSAIS && result.isEmpty(); ++i) {
            result = parseFunction.apply(in.next());
        }
        return result.orElseThrow(() -> new Error("Erreur de lecture (" + CLI.MAX_ESSAIS + " essais infructueux)."));
    }

    private static Optional<String> parseEmail(final String token) {
        EmailValidator validator = EmailValidator.getInstance(false, false);
        if (validator.isValid(token)) {
            return Optional.of(token.toLowerCase());
        }
        CLI.afficher("Adresse email invalide.");
        return Optional.empty();
    }

    private static String lireEmail() {
        CLI.afficher("Saisir une adresse email :");
        return CLI.lireAvecErreurs(CLI::parseEmail);
    }

    private static Optional<String> parseNouveauNom(final String token, final Set<String> nomsConnus) {
        if (nomsConnus.contains(token)) {
            CLI.afficher("Le nom existe déjà.");
            return Optional.empty();
        }
        return Optional.of(token);
    }

    private static String lireNom(final Set<String> nomsConnus, final boolean nouveau) {
        if (nouveau && !nomsConnus.isEmpty()) {
            CLI.afficher("Noms déjà pris : " + String.join(", ", nomsConnus));
        }
        return CLI.lireAvecErreurs(token -> CLI.parseNouveauNom(token, nomsConnus));
    }

    private static String lireNom() {
        return CLI.lireNom(Collections.emptySet(), true);
    }

    private static java.time.LocalDate lireDate() {
        CLI.afficher("Saisir une date (format YYYY-MM-DD) :");
        return CLI.lireAvecErreurs(token -> {
            try {
                return Optional.of(java.time.LocalDate.parse(token));
            } catch (java.time.format.DateTimeParseException e) {
                CLI.afficher("Date invalide.");
                return Optional.empty();
            }
        });
    }

    private static void afficher(final String msg) {
        System.out.println(msg);
        System.out.flush();
    }
}