package com.vmware.talentboost.ics.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table
@JsonPropertyOrder({"id", "url", "addedOn", "tags", "width", "height"})
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "url", unique = true, nullable = false)
    private String url;

    @Column(name = "added_on")
    private Timestamp addedOn;

    @Column(name = "service")
    private String service;

    @Column(name = "width")
    private double width;

    @Column(name = "height")
    private double height;

    @OneToMany(mappedBy = "image", fetch = FetchType.EAGER)
    @JsonIgnore
    @JsonProperty("connections")
    private List<Connection> connections;

    public Image(int id, String url, Timestamp addedOn, double width, double height) {
        this.id = id;
        this.url = url;
        this.addedOn = addedOn;
        this.service = "Imagga";
        this.width = width;
        this.height = height;
        this.connections = new ArrayList<>();
    }

    public Image() {
        this.connections = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Timestamp addedOn) {
        this.addedOn = addedOn;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Image{" +
            "id=" + id +
            ", url='" + url + '\'' +
            ", addedOn=" + addedOn +
            ", service='" + service + '\'' +
            ", width=" + width +
            ", height=" + height +
            '}';
    }

    public Image(String url, Timestamp addedOn, String service, double width, double height) {
        this.url = url;
        this.addedOn = addedOn;
        this.service = service;
        this.width = width;
        this.height = height;
    }

    public List<Pair<String, Integer>> getConnections() {
        Map<String, Integer> result = new HashMap<>();
        List<Pair<String, Integer>> myResult = new ArrayList<>();

        for (Connection connection: this.connections) {
            result.put(connection.getTag().getName(), (int)connection.getConfidence());
        }
        result = result.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (Map.Entry<String, Integer> curr: result.entrySet()) {
            Pair<String, Integer> currPair = Pair.of(curr.getKey(), curr.getValue());
            myResult.add(currPair);
        }

        return myResult;
    }

    @JsonIgnore
    public List<Connection> getImageConnections() {
        return this.connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public void addConnection(Connection connection) {
        this.connections.add(connection);
    }
}
