package victor.encurtadourl.app.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection="TB_URL")
public class EncurtadorURLModel {

    @Id
    private String idURL;

    private String url;
    private String encutrador;
    private LocalDate data;

    public String getIdURL() {
        return idURL;
    }

    public void setIdURL(String idURL) {
        this.idURL = idURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEncutrador() {
        return encutrador;
    }

    public void setEncutrador(String encutrador) {
        this.encutrador = encutrador;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
