package org.example.final2;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import java.io.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

/**
 * @author [Kami]
 * @version 1.0
 */

public class RentalMobil extends Application {

    private ObservableList<String> mobilList = FXCollections.observableArrayList("BMW", "FORD", "JEEP");
    private ComboBox<String> mobilComboBox = new ComboBox<>(mobilList);
    private TextField durasiTextField = new TextField();
    private Label totalBiayaLabel = new Label();
    private Map<String, Double> hargaMobilMap = new HashMap<>();
    private DatePicker tanggalPenyewaanPicker = new DatePicker();
    private DatePicker tanggalPengembalianPicker = new DatePicker();
    private TextField nomorHandphoneTextField = new TextField();
    private TextField namaPenyewaTextField = new TextField();
    private AtomicInteger nomorPemesanan = new AtomicInteger(1);

    @Override
    public void start(Stage primaryStage) {
        hargaMobilMap.put("BMW", 200.0);
        hargaMobilMap.put("FORD", 500.0);
        hargaMobilMap.put("JEEP", 800.0);

        primaryStage.setTitle("Aplikasi Penyewaan Mobil");
        primaryStage.initStyle(StageStyle.DECORATED);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));

        Label titleLabel = new Label("SELAMAT DATANG DI LAYANAN PENYEWAAN MOBIL");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: red; -fx-padding: 10px;-fx-background-radius: 10;");
        titleLabel.setEffect(new DropShadow(10, Color.BLACK));
        gridPane.add(titleLabel, 0, 0, 2, 1);

        Label namaPenyewaLabel = new Label("Nama Penyewa :");
        namaPenyewaLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        gridPane.add(namaPenyewaLabel, 0, 1);
        gridPane.add(namaPenyewaTextField, 1, 1);

        Label nomorHandphoneLabel = new Label("Nomor Handphone :");
        nomorHandphoneLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        gridPane.add(nomorHandphoneLabel, 0, 2);
        gridPane.add(nomorHandphoneTextField, 1, 2);

        Label mobilLabel = new Label("Pilih Mobil :");
        mobilLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        gridPane.add(mobilLabel, 0, 3);
        gridPane.add(mobilComboBox, 1, 3);

        Label durasiLabel = new Label("Durasi Sewa (hari) :");
        durasiLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        gridPane.add(durasiLabel, 0, 4);
        gridPane.add(durasiTextField, 1, 4);
        durasiTextField.setPromptText("Masukkan durasi sewa dalam hari");


        addDatePickers(gridPane);

        Button hitungButton = new Button("Hitung Biaya");
        hitungButton.setOnAction(e -> hitungBiaya());
        hitungButton.setOnMouseEntered(e -> hitungButton.setStyle("-fx-background-color: red; -fx-text-fill: white;"));
        hitungButton.setOnMouseExited(e -> hitungButton.setStyle(""));
        hitungButton.setEffect(new DropShadow(10, Color.BLACK));
        gridPane.add(hitungButton, 1, 7);

        Button buatStrukButton = new Button("Buat Struk");
        buatStrukButton.setOnAction(e -> buatStruk());
        buatStrukButton.setOnMouseEntered(e -> buatStrukButton.setStyle("-fx-background-color: green; -fx-text-fill: white;"));
        buatStrukButton.setOnMouseExited(e -> buatStrukButton.setStyle(""));
        buatStrukButton.setEffect(new DropShadow(10, Color.BLACK));
        gridPane.add(buatStrukButton, 1, 10);

        Label totalLabel = new Label("Total Biaya :");
        totalLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        gridPane.add(totalLabel, 0, 8);
        gridPane.add(totalBiayaLabel, 1, 8);

        Scene scene = new Scene(gridPane, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        Label keteranganLabel = new Label("Keterangan:");
        keteranganLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        gridPane.add(keteranganLabel, 0, 9);
        TextArea keteranganTextArea = new TextArea();
        keteranganTextArea.setPrefWidth(200);
        keteranganTextArea.setPrefHeight(130);
        gridPane.add(keteranganTextArea, 1, 9);
        keteranganTextArea.setText("* Harga sewa sudah termasuk asuransi.\n* Harga sewa belum termasuk biaya parkir dan tol.\n* Harga sewa dapat berubah sewaktu-waktu.\n* Harga sewa belum termasuk bensin.\n\n* Pembatalan pemesanan sebelum 24 jam dikenakan biaya 50%.\n* Pembatalan pemesanan setelah 24 jam dikenakan biaya 100%.");
    }

    private void addDatePickers(GridPane gridPane) {
        Label penyewaanLabel = new Label("Tanggal Penyewaan :");
        penyewaanLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        gridPane.add(penyewaanLabel, 0, 5);
        gridPane.add(tanggalPenyewaanPicker, 1, 5);

        Label pengembalianLabel = new Label("Tanggal Pengembalian :");
        pengembalianLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        gridPane.add(pengembalianLabel, 0, 6);
        gridPane.add(tanggalPengembalianPicker, 1, 6);
    }

    private long hitungDurasi() {
        LocalDate tanggalPenyewaan = tanggalPenyewaanPicker.getValue();
        LocalDate tanggalPengembalian = tanggalPengembalianPicker.getValue();

        if (tanggalPenyewaan != null && tanggalPengembalian != null) {
            return ChronoUnit.DAYS.between(tanggalPenyewaan, tanggalPengembalian);
        }

        return 0;
    }

    private void hitungBiaya() {
        try {
            int durasi = Integer.parseInt(durasiTextField.getText());
            String selectedMobil = mobilComboBox.getValue();
            double hargaSewaPerHari = hargaMobilMap.get(selectedMobil);
            double totalBiaya = durasi * hargaSewaPerHari;

            String formattedTotalBiaya = String.format("Rp %.3f", totalBiaya);

            totalBiayaLabel.setText(formattedTotalBiaya);
            totalBiayaLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        } catch (NumberFormatException e) {
            totalBiayaLabel.setText("Masukkan durasi yang valid");
        }
    }

    private void buatStruk() {
        try {
            int nomor = nomorPemesanan.getAndIncrement();
            String namaPenyewa = namaPenyewaTextField.getText(); // Mendapatkan nilai nama penyewa
            String nomorHandphone = nomorHandphoneTextField.getText(); // Mendapatkan nilai nomor handphone

            FileWriter fileWriter = new FileWriter("struk.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println("--------------------------------------------------------------------");
            printWriter.println("                    STRUK PEMBAYARAN SEWA MOBIL                     ");
            printWriter.println("--------------------------------------------------------------------");
            printWriter.println("Nama Penyewa        : " + namaPenyewa);
            printWriter.println("Nomor Handphone     : " + nomorHandphone);
            printWriter.println("Nomor Pemesanan     : " + nomor);
            printWriter.println("Mobil               : " + mobilComboBox.getValue());
            printWriter.println("Durasi Sewa         : " + durasiTextField.getText() + " hari");
            printWriter.println("Total Biaya         : " + totalBiayaLabel.getText());
            printWriter.println("Tanggal Penyewaan   : " + tanggalPenyewaanPicker.getValue());
            printWriter.println("Tanggal Pengembalian: " + tanggalPengembalianPicker.getValue());
            printWriter.println("--------------------------------------------------------------------");
            printWriter.println("Terima kasih telah menggunakan layanan kami!");
            printWriter.close();

            // Buat window baru untuk menampilkan struk
            Stage stage = new Stage();
            stage.setTitle("Struk Pembayaran");
            stage.setResizable(false);

            // Baca isi file dan tampilkan dalam TextArea
            FileReader fileReader = new FileReader("struk.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder strukContent = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                strukContent.append(line).append("\n");
            }
            bufferedReader.close();

            // Tambahkan teks ke window
            Label label = new Label("Struk Pembayaran Sewa Mobil");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            TextArea textArea = new TextArea();
            textArea.setText(strukContent.toString());
            textArea.setEditable(false);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setPadding(new Insets(20));

            gridPane.add(label, 0, 0);
            gridPane.add(textArea, 0, 1);

            Scene scene = new Scene(gridPane, 500, 400);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Terjadi kesalahan saat membuat struk");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
