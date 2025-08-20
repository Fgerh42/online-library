package com.fabricio.onlinelibrary;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.Executions; 
import org.zkoss.bind.annotation.Init;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fabricio.onlinelibrary.models.Book;

public class BooksVM {
    private Book editBook = new Book();
    private List<Book> bookList;
    static String baseURL = "https://sa.interact.com.br/sa/rs/";
    private boolean dialogVisible = false;

    public Book getEditBook() { return editBook; }
    public boolean isDialogVisible() { return dialogVisible; }

    @Init
    public void init() {
        bookList = getBooksFromApi();
    }

    public List<Book> getBookList() {
        return bookList;
    }

    private List<Book> getBooksFromApi() {
        try {
            URL url = new URL(baseURL + "books");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();
            Book[] booksArray = mapper.readValue(response.toString(), Book[].class);
            return Arrays.asList(booksArray);

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();  
        }
    }

    public String getDialogTitle() {
        if (editBook.getCode() == null) {
            return "Novo livro";
        }
        if (editBook.getCode() != null && editBook.getName() != null && !editBook.getName().isEmpty()) {
            return editBook.getName();
        }
        return "Livro";
    }

    private void printToConsole(String msg) {
        msg = msg.replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "");
        Clients.evalJavaScript("console.log('" + msg + "');");
    }

    @Command
    @NotifyChange({"editBook", "dialogVisible", "dialogTitle"})
    public void openBookDialog(@BindingParam("book") Book book) {
        if (book == null) {
            editBook = new Book();
        } else {
            editBook = book;
        }
        dialogVisible = true;
    }

    @Command
    @NotifyChange({"dialogVisible", "bookList"})
    public void closeDialog() {
        dialogVisible = false;
    }

    @Command
    @NotifyChange({"bookList", "dialogVisible"})
    public void saveBook() {
        try {  

            URL url = new URL(baseURL + "books");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                os.write(editBook.getName().getBytes("UTF-8"));
                os.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == 200 || responseCode == 201) {
                Clients.showNotification("Livro salvo com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, "top_center", 2000);
                bookList = getBooksFromApi();
            } else {
                Clients.showNotification("Erro ao salvar livro: " + responseCode, Clients.NOTIFICATION_TYPE_ERROR, null, "top_center", 2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Clients.showNotification("Erro inesperado ao salvar livro", Clients.NOTIFICATION_TYPE_ERROR, null, "top_center", 2000);
        }

        dialogVisible = false;
    } 
    @Command
    @NotifyChange("bookList")
    public void bookBook(@BindingParam("book") Book book) {
        try { 
            boolean newBookedState = !book.isBooked();

            String bookedStateURL = newBookedState?"1":"0";
 
            URL url = new URL(baseURL + "books/" + book.getCode() + "/" + bookedStateURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");

            int responseCode = con.getResponseCode();
            if (responseCode == 200 || responseCode == 201) {
                Clients.showNotification(
                    newBookedState ? "Livro reservado!" : "Reserva cancelada!",
                    Clients.NOTIFICATION_TYPE_INFO,
                    null, "top_center", 2000
                );
                bookList = getBooksFromApi();
            } else {
                Clients.showNotification("Erro ao atualizar reserva: " + responseCode,
                    Clients.NOTIFICATION_TYPE_ERROR, null, "top_center", 2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Clients.showNotification("Erro inesperado ao atualizar reserva",
                Clients.NOTIFICATION_TYPE_ERROR, null, "top_center", 2000);
        }
    }

    
}
