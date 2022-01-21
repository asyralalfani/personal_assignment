import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static HashMap<Integer,String> data = new HashMap<>();
    private final String[] data_key = {"Nama", "Alamat", "Nomor Handphone", "Tgl Lahir [dd-mm-yyyy]", "Status [kawin/belum kawin]", "Nomor KTP"};
    private final String[] main_menu = {"Lihat Data Diri", "Update Data Diri", "Keluar"};

    static boolean forRegex(String format, String value) {
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    static int getCurrentYear() {
        Date d = new Date();
        int year = d.getYear();
        return year + 1900;
    }

    static String errorHandling(String input, String value) {
        String error = "";
        try {
            if (input.equals("Menu")) {
                boolean match = forRegex("^[0-9]+$", value);
                if (!match) {
                    throw new InputMismatchException("Hanya boleh berupa angka");
                }
            }
            if (input.equals("Nomor Handphone")) {
                boolean match = forRegex("^[0-9]+$", value);
                if (!match) {
                    throw new InputMismatchException("Hanya boleh berupa angka");
                }
                if (value.length() < 9 | value.length() > 14) {
                    throw new NumberFormatException("Nomor handphone harus diantara 9-14 digit");
                }
            }
            if (input.equals("Tgl Lahir [dd-mm-yyyy]")) {
                boolean match = forRegex("^\\d{2}-\\d{2}-\\d{4}$", value);
                if (!match) {
                    throw new NumberFormatException("Format salah, gunakan format dd-mm-yyyy");
                }
                String [] split = value.split("-");
                if (Integer.parseInt(split[0]) > 31) {
                    throw new NumberFormatException("Tanggal lahir harus 1-31");
                }
                if (Integer.parseInt(split[1]) > 12) {
                    throw new NumberFormatException("Bulan lahir harus 1-12");
                }
                if (Integer.parseInt(split[2]) > getCurrentYear()) {
                    throw new NumberFormatException("Tahun lahir tidak boleh melebihi " + getCurrentYear());
                }
            }
            if (input.equals("Status [kawin/belum kawin]")) {
                boolean match = value.equals("kawin") | value.equals("belum kawin");
                if (!match) {
                    throw new InputMismatchException("Data yang harus dimasukkan kawin atau belum kawin");
                }
            }
            if (input.equals("Nomor KTP")) {
                boolean match = forRegex("^[0-9]+$", value);
                if (!match) {
                    throw new InputMismatchException("Hanya boleh berupa angka");
                }
                if (value.length() != 16) {
                    throw new NumberFormatException("Nomor KTP harus diantara 16 digit");
                }
            }
        } catch (InputMismatchException | NumberFormatException e) {
            error = e.toString();
        }
        return error;
    }

    static void printError(String error) {
        System.out.println("============================== Error ==============================");
        System.out.println(error);
        System.out.println("===================================================================");
    }

    static void showData() {
        Main variable = new Main();
        System.out.println("=============== Data Diri ===============");
        String [] months = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli",
                            "Agustus", "September", "Oktober", "November", "Desember"};
        for (int i = 0; i < variable.data_key.length; i++) {
            String key = variable.data_key[i];
            String value = data.get(i+1);
            if (key.equals("Tgl Lahir [dd-mm-yyyy]")) {
                String [] split = value.split("-");
                value = split[0] + " " + months[Integer.parseInt(split[1]) - 1] + " " + split[2];
            }
            System.out.println(key + " : " + value);
        }
    }

    static void initialData() {
        Main variable = new Main();
        Scanner scanner;
        String confirm = "y";

        //set initial value for hashmap
        for (int i = 0; i < variable.data_key.length; i++) {
            data.put(i+1, "-");
        }

        do {
            scanner = new Scanner(System.in);

            for (int i = 0; i < variable.data_key.length; i++) {
                String key = variable.data_key[i];
                if (key.equals("Nama") || key.equals("Nomor Handphone") || key.equals("Tgl Lahir [dd-mm-yyyy]")) {
                    String error,input;
                    do {
                        System.out.print(key + " : ");
                        input = scanner.nextLine();
                        error = errorHandling(key,input);
                        if (!error.isEmpty()) {
                            printError(error);
                        }
                    } while (!error.isEmpty());

                    //set value to hash map based on key name
                    data.put(i+1, input);
                }
            }

            do {
                if (!confirm.equals("n") && !confirm.equals("y")) printError("Masukkan Y atau N");

                System.out.print("Apakah Anda yakin dengan data tersebut? [Y|N] : ");
                confirm = scanner.next().toLowerCase();
                confirm = String.valueOf(confirm.charAt(0));

            } while(!confirm.equals("n") && !confirm.equals("y"));

        } while (confirm.equals("n"));

        //show main menu
        menu:
        while (true) {
            System.out.println("=============== Menu Utama ===============");
            for (int i = 0; i < variable.main_menu.length; i++) {
                System.out.println(i + 1 + ". " + variable.main_menu[i]);
            }

            String error1,choose;
            do {
                System.out.print("Pilih menu? [1/2/3] : ");
                choose = scanner.next();
                error1 = errorHandling("Menu", choose);
                if (!error1.isEmpty()) {
                    printError(error1);
                }
            } while (!error1.isEmpty());

            switch (Integer.parseInt(choose)) {
                case 1 -> showData();
                case 2 -> {
                    System.out.println("=============== Edit Data ===============");
                    for (int i = 0; i < variable.data_key.length; i++) {
                        System.out.println(i + 1 + ". " + variable.data_key[i]);
                    }
                    System.out.println("7. Cancel");
                    String error2,choose_edit;
                    do {
                        System.out.print("Pilih menu? [1/2/3/4/5/6/7] : ");
                        choose_edit = scanner.next();
                        error2 = errorHandling("Menu", choose_edit);
                        if (!error2.isEmpty()) {
                            printError(error2);
                        }
                    } while (!error2.isEmpty());

                    int new_choose_edit = Integer.parseInt(choose_edit);
                    if (new_choose_edit == 7) continue menu;

                    scanner.nextLine();

                    String error,new_data;
                    do {
                        String key = variable.data_key[new_choose_edit-1];
                        System.out.print("Data baru untuk " + key.toLowerCase() + " : ");
                        new_data = scanner.nextLine();
                        error = errorHandling(key, new_data);
                        if (!error.isEmpty()) {
                            printError(error);
                        }
                    } while (!error.isEmpty());

                    //set new data to hashmap
                    data.put(new_choose_edit, new_data);

                    System.out.println("Update berhasil");
                    continue menu;
                }
                case 3 -> {
                    System.out.println("Yeay, aplikasi telah berhenti");
                    System.exit(0);
                }
                default -> System.out.println("Pilihan tidak tersedia");
            }
        }
    }

    public static void main(String[] args) {
        initialData();
    }
}
