package ch.hsr.isf.serepo.client.simple;

import ch.hsr.isf.serepo.client.simple.api.entities.User;

public interface Settings {

  String SEREPO_URL = "http://localhost:8080/serepo";
  
  User USER = new User("simple-client", "simple-client@serepo.com");
  
}
